//
//  DownloadedMedia.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/03/2023.
//

import Foundation

enum MediaRetrivalType {
    case SeriseInfo
    case SeasonInfo
    case EpisodeInfo
    case MovieInfo
}

public struct DownloadedMedia : Codable{
    var mediaRetrivalType : MediaRetrivalType = .MovieInfo
    
    var mediaId : String
    var mediaURL : URL?
    var mediaType : MediaManager.MediaType = .movie
    var path : URL{
        if let g = group {
            return FilesManager.shared.cache.appendingPathComponent(mediaType.version_3_value, isDirectory: true)
                .appendingPathComponent(g.showId, isDirectory: true)
                .appendingPathComponent(g.seasonId, isDirectory: true)
                .appendingPathComponent("\(mediaId).mp4")
        }
        
        return FilesManager.shared.cache.appendingPathComponent(mediaType.rawValue, isDirectory: true).appendingPathComponent("\(mediaId).mp4")
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
   
    
    var mediaDownloadName : String {
        return mediaType.rawValue + "/" + "\(mediaId)"
    }
    
    func store() throws{
        try FilesManager.shared.registerDownloadedMedia(self)
    }
    
    func remove() throws{
        if mediaType == .movie{
            try FilesManager.shared.deleteMovieBy(id: mediaId)
        }else{
            FilesManager.shared.deleteEpisode(self)
        }
        
    }
    
    static func getMovieByID(id : String)throws->DownloadedMedia?{
            return try FilesManager.shared.getDownloadeMovieById(id)
    }
    
    static func getEpisodeByID(id : String, season: String, series: String)->DownloadedMedia?{
        return FilesManager.shared.getDownloadedEpisode(id: id, season: season, series: series)
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

//public struct SeasonInfo : Codable{
//    var seasonID : String
//    var seriseName : String
//    var seriseName : String
//    var data : Data!
//    
//    var info : [String:Any]? {
//        set{
//            if newValue != nil{
//                if let d = try? JSONSerialization.data(withJSONObject: newValue!, options: .fragmentsAllowed) {
//                    self.data = d
//                }
//            }else{
//                self.data = nil
//            }
//        }
//        
//        get{
//            let f = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]
//            return f
//        }
//    }
//}
