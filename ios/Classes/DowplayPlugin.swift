import Flutter
import UIKit

public class DowplayPlugin: NSObject, FlutterPlugin {
    let flutterViewController: UIViewController = (UIApplication.shared.delegate?.window??.rootViewController)!;
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "dowplay", binaryMessenger: registrar.messenger())
        let instance = DowplayPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch(call.method){
        case "play_episode":
            self.playEpisode(call: call, result: result)
            result(true)
            break;
        case "play_movie":
            self.playMovie(call: call, result: result)
            result(true)
            break;
        case "config_downloader":
            DownloadManager.shared.config()
            result(true)
        case "get_downloads_list":
            getDownloadsList(call: call, result: result)
        default:
            print("method wasn't found : ",call.method);
            result(false)
        }
    }
    
    func playEpisode(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_type : String = myArgs["media_type"] as? String,
           let user_id : String = myArgs["user_id"] as? String,
           let profile_id : String = myArgs["profile_id"] as? String,
           let token : String = myArgs["token"] as? String,
           let api_base_url : String = myArgs["api_base_url"] as? String,
           let media_group : [String:Any] = myArgs["media_group"] as? [String:Any],
           let info : [String:Any] = myArgs["info"] as? [String:Any],
           let lang : String = myArgs["lang"] as? String {
            
            let keeUser = KeeUser(userID: user_id, profileID: profile_id,token: token)
            let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang,baseURL: api_base_url,apiVersion: 4,baseType: "mobile",baseVersion: "v4",acceptType: "ios")
            let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
            let itemsIds: [String:Any] = media_group["items_ids"] as! [String : Any]
            let episodes: [[String:Any]] = media_group["episodes"] as! [[String:Any]]
            let playIndex: Int = Int(info["order"] as! String)! - 1;
            var media : [Media] = []
            let _ : [String:Any] = media_group["tv_show"] as! [String:Any]
            let season : [String:Any] = media_group["season"] as! [String:Any]
            for episode in episodes {
                let mediaId = String(episode["id"] as! Int);
                let mediaGroup : MediaGroup = MediaGroup(showId: itemsIds["tv_show_id"] as! String, seasonId: itemsIds["season_id"] as! String, episodeId: mediaId ,data: media_group)
                let watching : [String:Any]? = episode["watching"] as? [String : Any]
                var startAt:Float = 0.0
                if(watching != nil) {
                    startAt = Float(watching?["current_time"] as! String)!
                }
                let mediaItem = Media(title: episode["title"] as! String,subTitle: season["title"] as? String, urlToPlay: episode["media_url"] as! String,keeId: mediaId,type: mediaType, startAt: startAt,mediaGroup: mediaGroup,info: episode)
                media.append(mediaItem)
                
            }
            MediaManager.default.openMediaPlayer(usingMediaList: media,playMediaIndex: playIndex,usingSettings: hostAppSettings, forViewController: flutterViewController)
            
        } else {
            print("iOS could not extract flutter arguments in method: (play)")
            result(false)
        }
    }
    
    func playMovie(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let title : String = myArgs["title"] as? String,
           let sub_title : String = myArgs["sub_title"] as? String,
           let url : String = myArgs["url"] as? String,
           let media_id : String = myArgs["media_id"] as? String,
           let media_type : String = myArgs["media_type"] as? String,
           let user_id : String = myArgs["user_id"] as? String,
           let profile_id : String = myArgs["profile_id"] as? String,
           let token : String = myArgs["token"] as? String,
           let api_base_url : String = myArgs["api_base_url"] as? String,
           let start_at : Float = myArgs["start_at"] as? Float,
           let info : [String:Any] = myArgs["info"] as? [String:Any],
           let lang : String = myArgs["lang"] as? String {
            
            let keeUser = KeeUser(userID: user_id, profileID: profile_id,token: token)
            let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang,baseURL: api_base_url,apiVersion: 4,baseType: "mobile",baseVersion: "v4",acceptType: "ios")
            let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
            
            var media : [Media] = []
            
            let mediaItem = Media(title: title,subTitle: sub_title, urlToPlay: url,keeId: media_id,type: mediaType, startAt: start_at,info: info)
            media.append(mediaItem)
            
            MediaManager.default.openMediaPlayer(usingMediaList: media,usingSettings: hostAppSettings, forViewController: flutterViewController)
            result(true)
            
        } else {
            print("iOS could not extract flutter arguments in method: (play)")
            result(false)
        }
    }
    
    func getDownloadsList(call: FlutterMethodCall,result: @escaping FlutterResult){
        let downloadsList : [[String : Any]] = try! DownloadManager.shared.getAllMediaDecoded()
        result(downloadsList)
        
    }
}
