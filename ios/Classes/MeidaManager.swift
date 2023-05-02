//
//  MeidaManager.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 04/01/2023.
//

import UIKit



public class MediaManager {
    
    static var `default` = MediaManager()
    private var currentPlayer : KeeVideoPlayerController?
    
    
    private init(){}
    
    
    //MARK: -Media Player Functions.
    @discardableResult
    public func openMediaPlayer(usingMediaList list : [Media],playMediaIndex: Int = 0, usingSettings settings: HostAppSettings,  forViewController mvc: UIViewController)->KeeVideoPlayerController {
        
        if playMediaIndex < 0 || playMediaIndex >= list.count {
            let name = NSExceptionName("Playing index is not in the media list range")
            NSException(name: name, reason: "Media list range is from 0 ... to \(list.count - 1), and the playing index is \(playMediaIndex)").raise()
        }
        
        let vc = KeeVideoPlayerController(nibName: "KeeVideoPlayerController", bundle: .packageBundle)
        vc.settings = settings
        vc.setMediaList(mediaList: list)
        vc.setPlayingIndex(playMediaIndex)
        if let nvc = mvc.navigationController {
            nvc.pushViewController(vc, animated: true)
        }else{
            mvc.present(vc, animated: true)
        }
        return vc
    }
    
    public func mediaPlayerApplyAction(_ action : MediaAction){
        guard let player = currentPlayer else {return}
        switch(action){
        case .play:
            fallthrough
        case .pause:
            player.playPauseAction(nil)
        case .jumpForeward:
            player.jumpForwardAction(nil)
        case .jumpBackward:
            player.jumpBackwardAction(nil)
        case .nextMediaFile:
            player.nextAction(nil)
        case .previousMediaFile:
            player.previousAction(nil)
        }
    }
    
    //MARK: -Downloaded Media Functions. (Fetching)
        
//    public func getDownloaded(ForMediaIdentifier identifier: String, type: MediaType)->DownloadedMedia?{
//        return try? FilesManager.shared.getDownloadeMediaById(identifier,type: type)
//    }
//    
//    public func getMetaData(ForMediaIdentifier identifier: String, type: MediaType)->[String:Any]?{
//        if let media = getDownloaded(ForMediaIdentifier: identifier, type: type) {
//            return media.object
//        }
//        
//        if let data = UserDefaults.standard.object(forKey: "\(identifier)_\(type.rawValue)") as? Data {
//            return try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]
//        }
//        
//        return nil
//    }
    
    
   
    
    public func getDownloadingList(type: MediaType)->[[String:Any]]{
        let results = DownloadManager.shared.getAllDownloadingTasks(forType: type).map({ object in
            if !(object.state == .canceling || object.state == .completed){
                if let data = UserDefaults.standard.object(forKey: object.mediaId ?? "") as? Data {
                    if let dataObject = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any] {
                        return ["task" : object, "data" : dataObject]
                    }
                }
            }
            return nil
        }).compactMap({$0})
        return results
    }
    
    
    
    
    public enum MediaAction {
        case play
        case pause
        case jumpForeward
        case jumpBackward
        case nextMediaFile
        case previousMediaFile
    }
    
    public enum MediaType : String, Codable{
        case movie = "movies"
        case series = "series"
        
        var version_4_value : String {
            switch(self){
            case .movie :
                return "movie"
            case .series:
                return "episode"
            }
        }
        
        var version_3_value : String {
            switch(self){
            case .movie :
                return "movies"
            case .series:
                return "series"
            }
        }
        
    }
}



