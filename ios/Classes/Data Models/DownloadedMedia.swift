//
//  DownloadedMedia.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/03/2023.
//

import Foundation

public struct DownloadedMedia : Codable{
    var mediaId : String
    var mediaURL : URL?
    var mediaType : MediaManager.MediaType = .movie
    var path : URL{
        if let g = group {
            return FilesManager.shared.cache.appendingPathComponent(mediaType.rawValue, isDirectory: true)
                .appendingPathComponent(g.showId, isDirectory: true)
                .appendingPathComponent(g.seasonId, isDirectory: true)
                .appendingPathComponent("\(mediaId).mp4")
        }
        
        return FilesManager.shared.cache.appendingPathComponent(mediaType.rawValue, isDirectory: true).appendingPathComponent("\(mediaId).mp4")
    }
    var name : String
    var tempPath : URL
    var group : MediaGroup?
    private var data : Data?
    var object : [String:Any]? {
        set{
            if newValue != nil{
                self.data = try? JSONSerialization.data(withJSONObject: newValue!, options: .prettyPrinted)
            }
        }
        
        get{
            if let data = self.data{
                return try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]
            }
            return nil
        }
    }
    
    var seasons : [SeasonGroup] = []
    
    
    enum CodingKeys: CodingKey {
        case mediaId
        case mediaURL
        case mediaType
        case name
        case tempPath
        case group
        case data
    }
    
    init(mediaId: String, mediaURL: URL? = nil, mediaType: MediaManager.MediaType = .movie, name: String, tempPath: URL) {
        self.mediaId = mediaId
        self.mediaURL = mediaURL
        self.mediaType = mediaType
        self.name = name
        self.tempPath = tempPath
    }
    
    var mediaDownloadName : String {
        return mediaType.rawValue + "/" + "\(mediaId)"
    }
    
    func store() throws{
        try FilesManager.shared.registerDownloadedMedia(self)
    }
    
    func remove() throws{
        try FilesManager.shared.deleteMediaBy(id: mediaId, type: self.mediaType)
    }
    
    static func getByID(id : String, ofType: MediaManager.MediaType)throws->DownloadedMedia?{
        return try FilesManager.shared.getDownloadeMediaById(id, type: ofType)
    }
    
        
    static func getAll(ofType : MediaManager.MediaType)throws->[DownloadedMedia]{
        return try FilesManager.shared.getDownloadList(type: ofType)
    }
    
}



public struct SeasonGroup {
    var mediaList : [DownloadedMedia] = []
}
