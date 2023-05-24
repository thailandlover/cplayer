//
//  DownloadedMedia.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/03/2023.
//

import Foundation

enum MediaRetrivalType : String, Codable{
    case SeriseInfo = "SeriseInfo"
    case SeasonInfo = "SeasonInfo"
    case EpisodeInfo = "EpisodeInfo"
    case MovieInfo = "MovieInfo"
}

public struct DownloadedMedia : Codable{
    var mediaRetrivalType : MediaRetrivalType = .MovieInfo
    var signature : String = ""
    var mediaId : String
    var mediaURL : URL?
    var mediaType : MediaManager.MediaType = .movie
    var path : URL{
        if let g = group {
            return FilesManager.shared.cache.appendingPathComponent(mediaType.version_3_value, isDirectory: true)
                .appendingPathComponent(signature, isDirectory: true)
                .appendingPathComponent(g.showId, isDirectory: true)
                .appendingPathComponent(g.seasonId, isDirectory: true)
                .appendingPathComponent("\(mediaId).mp4")
        }
        
        return FilesManager.shared.cache.appendingPathComponent(mediaType.rawValue, isDirectory: true).appendingPathComponent(signature, isDirectory: true).appendingPathComponent("\(mediaId).mp4")
    }
    var episodeInfoPathURL : URL?{
        if self.mediaType == .series && self.mediaRetrivalType == .EpisodeInfo {
            if let g = group {
                return FilesManager.shared.cache.appendingPathComponent(mediaType.version_3_value, isDirectory: true)
                    .appendingPathComponent(g.showId, isDirectory: true)
                    .appendingPathComponent(g.seasonId, isDirectory: true)
                    .appendingPathComponent("\(mediaId).keeinfo")                
            }
        }
        return nil
    }
    
    var name : String
    var tempPath : URL?
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
    
    var seasons : [Season] = []
    
    //Use only if the media is not downloaded yet (in progress)
    var status : URLSessionTask.State = .completed
    var progress : Double = 1
    
    enum CodingKeys: CodingKey {
        case mediaId
        case mediaURL
        case mediaType
        case name
        case tempPath
        case group
        case data
        case progress
        case mediaRetrivalType
    }
    
    init(mediaId: String, mediaURL: URL? = nil, mediaType: MediaManager.MediaType = .movie, name: String, tempPath: URL?) {
        self.mediaId = mediaId
        self.mediaURL = mediaURL
        self.mediaType = mediaType
        self.name = name
        self.tempPath = tempPath
    }
    
    init(mediaId: String, mediaURL: URL? = nil, mediaType: MediaManager.MediaType = .movie, name: String, status : URLSessionTask.State, progress : Double) {
        self.mediaId = mediaId
        self.mediaURL = mediaURL
        self.mediaType = mediaType
        self.name = name
        self.status = status
        self.progress = progress

    }
    
    init(mediaId: String, mediaURL: URL? = nil, mediaType: MediaManager.MediaType = .movie, name: String, group: MediaGroup, type : MediaRetrivalType) {
        self.mediaId = mediaId
        self.mediaURL = mediaURL
        self.mediaType = mediaType
        self.name = name
        self.tempPath = nil
        self.group = group
        self.mediaRetrivalType = type
        
        if mediaRetrivalType != .MovieInfo {
            self.mediaType = .series
        }else{
            self.mediaType = .movie
        }
    }
   
    
//    var mediaDownloadName : String {
//        return mediaType.rawValue + "/" + "\(mediaId)"
//    }
    
    @discardableResult
    mutating func setUser(signature: String)->DownloadedMedia {
        self.signature = signature
        return self
    }
    
    func store(signature: String) throws{
        try FilesManager.shared.forUser(signature).registerDownloadedMedia(self)
    }
    
    func remove(signature: String) throws{
        if mediaType == .movie{
            try FilesManager.shared.forUser(signature).deleteMovieBy(id: mediaId)
        }else{
            FilesManager.shared.forUser(signature).deleteEpisode(self)
        }
        
    }
    
    static func getMovieByID(id : String, signature: String)throws->DownloadedMedia?{
        var media = try FilesManager.shared.forUser(signature).getDownloadeMovieById(id)
        return media?.setUser(signature: signature)
    }
    
    static func getEpisodeByID(id : String, season: String, series: String, signature: String)->DownloadedMedia?{
        var media = FilesManager.shared.forUser(signature).getDownloadedEpisode(id: id, season: season, series: series)
        return media?.setUser(signature: signature)
    }
    
        
    
    
    func getObjectAsJSONDictionary() -> [String : Any]? {
          if let data = try? JSONEncoder().encode(self) {
              if var dir = try? JSONSerialization.jsonObject(with: data) as? [String:Any] {
                  dir.removeValue(forKey: "data")
                  dir["object"] = self.object
                  dir["group"] = self.group?.getObjectAsJSONDictionary()
                  dir["status"] = self.status.rawValue
                  return dir
              }
          }
          return nil
      }
    
}



public struct Season {
    var mediaList : [DownloadedMedia] = []
}

