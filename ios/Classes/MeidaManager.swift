import UIKit


class MediaManager {
    
    static var `default` = MediaManager()
    private var currentPlayer : KeeVideoPlayerController?
    
    
    private init(){}
    
    
    //MARK: -Media Player Functions.
    @discardableResult
    func openMediaPlayer(usingMediaList list : [Media], forViewController mvc: UIViewController)->KeeVideoPlayerController {
        let vc = KeeVideoPlayerController(nibName: "KeeVideoPlayerController", bundle: nil)
        vc.setMediaList(mediaList: list)
        if let nvc = mvc.navigationController {
            nvc.pushViewController(vc, animated: true)
        }else{
            mvc.present(vc, animated: true)
        }
        return vc
    }
    
    func mediaPlayerApplyAction(_ action : MediaAction){
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
        
    func getDownloaded(ForMediaIdentifier identifier: String, type: MediaType)->DownloadedMedia?{
        return try? FilesManager.shared.getDownloadeMediaById(identifier,type: type)
    }
    
    func getMetaData(ForMediaIdentifier identifier: String, type: MediaType)->[String:Any]?{
        if let media = getDownloaded(ForMediaIdentifier: identifier, type: type) {
            return media.object
        }
        
        if let data = UserDefaults.standard.object(forKey: "\(identifier)_\(type.rawValue)") as? Data {
            return try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]
        }
        
        return nil
    }
    
    
    func getDownloadedList(type: MediaType)->[DownloadedMedia]{
        return (try? FilesManager.shared.getDownloadList(type: type)) ?? []
    }
    
    func getDownloadingList(type: MediaType)->[[String:Any]]{
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
    
    
    
    enum MediaAction {
        case play
        case pause
        case jumpForeward
        case jumpBackward
        case nextMediaFile
        case previousMediaFile
    }
    
    enum MediaType : String, Codable{
        case movie = "movies"
        case series = "series"
    }
}



