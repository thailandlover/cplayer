//
//  FilesManager.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 20/12/2022.
//

import Foundation


public class FilesManager {
    private var fm = FileManager.default
    static var shared = FilesManager()
    
    private var documentsDirectory : URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let documentsDirectory = paths[0]
        return documentsDirectory
    }
    
    var cache : URL{
        return documentsDirectory.appendingPathComponent("DownloadCache")
    }
    
    
    private init() {
        do{
            try createCachingDirectory()
        }catch{
            
        }
    }
    
    private func createCachingDirectory()throws{
        let dir = documentsDirectory.appendingPathComponent("DownloadCache").path
        if !checkFolderExistance(dir: dir){
            try fm.createDirectory(at: documentsDirectory.appendingPathComponent("DownloadCache"),
                                       withIntermediateDirectories: true)
        }
        //Movies Folder
        let mDir = documentsDirectory.appendingPathComponent("DownloadCache")
            .appendingPathComponent(MediaManager.MediaType.movie.rawValue, isDirectory: true)
            .path
        
        if !checkFolderExistance(dir: mDir){
            try fm.createDirectory(at: documentsDirectory.appendingPathComponent("DownloadCache")
                .appendingPathComponent(MediaManager.MediaType.movie.rawValue, isDirectory: true),
                                       withIntermediateDirectories: true)
        }
        
        //Serise Folder
        let sDir = documentsDirectory.appendingPathComponent("DownloadCache")
            .appendingPathComponent(MediaManager.MediaType.series.rawValue, isDirectory: true)
            .path
        
        if !checkFolderExistance(dir: sDir){
            try fm.createDirectory(at: documentsDirectory.appendingPathComponent("DownloadCache")
                .appendingPathComponent(MediaManager.MediaType.series.rawValue, isDirectory: true),
                                       withIntermediateDirectories: true)
        }
    }
    
    
  
    /// Should be called after the download is finished
    /// This function is called by the DownloadedMedia Object
    func registerDownloadedMedia(_ downloadedMedia: DownloadedMedia) throws{
        
        var downloadPath = downloadedMedia.mediaDownloadName
        if let g = downloadedMedia.group {
            let tvShowFolder = String(format: "%@/%@",downloadedMedia.mediaType.version_3_value, g.showId)
            if !checkFolderExistance(dir: tvShowFolder) {
                try fm.createDirectory(at: documentsDirectory.appendingPathComponent("DownloadCache")
                    .appendingPathComponent(tvShowFolder, isDirectory: true),
                                           withIntermediateDirectories: true)
            }
            
            let seasonFolder = String(format: "%@/%@",tvShowFolder, g.seasonId)
            
            if !checkFolderExistance(dir: seasonFolder) {
                try fm.createDirectory(at: documentsDirectory.appendingPathComponent("DownloadCache")
                    .appendingPathComponent(seasonFolder, isDirectory: true),
                                           withIntermediateDirectories: true)
            }
            
            downloadPath = String(format: "%@/%@", seasonFolder, downloadedMedia.mediaId)
        }
        
        
        try self.moveDownloadedFile(atPath: downloadedMedia.tempPath,
                                toCacheUsingName: downloadPath,
                                        Extension: "mp4")
        var dmListContent = try getDMList(forType: downloadedMedia.mediaType)
            dmListContent[downloadedMedia.mediaId] = downloadedMedia
        saveDMListContent(dmListContent, forType: downloadedMedia.mediaType)
    }
    
    public func getDownloadeMediaById(_ id : String, type: MediaManager.MediaType)throws -> DownloadedMedia? {
        return try Array(self.getDMList(forType: type).values).first(where: {$0.mediaId == id})
    }
    
    public func deleteMediaBy(id : String, type: MediaManager.MediaType)throws{
        var dmListContent = try self.getDMList(forType: type)
        if let deletedItem = dmListContent.removeValue(forKey: id){
            try? fm.removeItem(at: deletedItem.tempPath)
            try? fm.removeItem(at: deletedItem.path)
            saveDMListContent(dmListContent, forType: type)
        }
    }
    
    public func getFileForMedia(mediaID: String,  type: MediaManager.MediaType)throws->URL?{
        return try DownloadedMedia.getByID(id: mediaID, ofType: type)?.path
    }
    
    public func getDownloadList(type: MediaManager.MediaType)throws ->[DownloadedMedia]{
        return try Array(self.getDMList(forType: type).values)
    }
    
    typealias SeasonIndex = [String: [DownloadedMedia]]
    public func getAllDownloadedMedia()->[DownloadedMedia]{
        do{
            let movies : [DownloadedMedia] = try Array(self.getDMList(forType: .movie).values)
            let series : [DownloadedMedia] = try Array(self.getDMList(forType: .series).values)
            var seasonIndexing : SeasonIndex = [:]
            
            for item in series {
                if let g = item.group {
                    var sList : [DownloadedMedia] = seasonIndexing[g.seasonId] ?? []
                    sList.append(item)
                    seasonIndexing[g.seasonId] = sList
                }
            }
            
            var mediaGroupIndexing : [String: DownloadedMedia] = [:]
            
            for (_, list) in seasonIndexing {
                if let item = list.first, let g = item.group {
                    var media = mediaGroupIndexing[g.showId] ?? DownloadedMedia(mediaId: item.mediaId, name: item.name, tempPath: item.tempPath)
                    media.object = item.object
                    media.mediaType = .series
                    media.group = g
                    media.seasons.append(SeasonGroup(mediaList: list))
                    mediaGroupIndexing[g.showId] = media
                }
            }
            var result : [DownloadedMedia] = []
            result.append(contentsOf: movies)
            result.append(contentsOf: mediaGroupIndexing.values)
            return result
            
            
        }catch{
            
        }
        
        
        return []
    }
    
    private func saveDMListContent(_ content: [String:DownloadedMedia], forType: MediaManager.MediaType){
        if let data = try? JSONEncoder().encode(content){
            let dmListFile = cache.appendingPathComponent(forType.rawValue, isDirectory: true).appendingPathComponent("dmList.keeImportant")
            try? data.write(to: dmListFile)
        }
    }
    
    private func getDMList(forType: MediaManager.MediaType) throws ->[String:DownloadedMedia]{
        let dmListFile = cache.appendingPathComponent(forType.rawValue, isDirectory: true).appendingPathComponent("dmList.keeImportant")
        
        if checkFileExistance(filePath: dmListFile.path){
            let data = try Data(contentsOf: dmListFile)
                let dmListContent = try JSONDecoder().decode([String:DownloadedMedia].self, from: data)
                    return dmListContent
        }
        
        return [:]
    }
    
    @discardableResult
    private func moveDownloadedFile(atPath path: URL, toCacheUsingName n: String, Extension ext: String ) throws -> URL{
        let destinationURL = cache.appendingPathComponent("\(n).\(ext)")
        try fm.moveItem(at: path, to: destinationURL)
        return destinationURL
    }
    
    private func checkFileExistance(filePath: String)->Bool{
        var isDir : ObjCBool = false
        if fm.fileExists(atPath: filePath, isDirectory: &isDir) {
            if !isDir.boolValue{
                return true
            }
        }
        return false
    }
    
    private func checkFolderExistance(dir: String)->Bool{
        var isDir : ObjCBool = false
        if fm.fileExists(atPath: dir, isDirectory: &isDir) {
            if isDir.boolValue{
                return true
            }
        }
        return false
    }
    
    
}
