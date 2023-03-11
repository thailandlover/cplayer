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
        case "play":
            self.play(call: call, result: result)
            result(true)
            break;
        case "config_downloader":
            DownloadManager.shared.config()
            result(true)
        default:
            print("method wasn't found : ",call.method);
            result(false)
        }
    }
    
    func play(call: FlutterMethodCall,result: @escaping FlutterResult){
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
           let media_group : [String:Any] = myArgs["media_group"] as? [String:Any],
           let lang : String = myArgs["lang"] as? String {
            
            let keeUser = KeeUser(userID: user_id, profileID: profile_id,token: token)
            let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang,baseURL: api_base_url,apiVersion: 4,baseType: "mobile",baseVersion: "v4",acceptType: "ios")
            let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
            let itemsIds: [String:Any] = media_group["items_ids"] as! [String : Any];
            let mediaGroup : MediaGroup = MediaGroup(showId: itemsIds["tv_show_id"] as! String, seasonId: itemsIds["season_id"] as! String, episodeId: itemsIds["episode_id"] as! String,data: media_group)
            var media : [Media] = []
            
            var mediaItem = Media(title: title,subTitle: sub_title, urlToPlay: url,keeId: media_id,type: mediaType, startAt: start_at,mediaGroup: mediaGroup,info: info)
            media.append(mediaItem)
            
            MediaManager.default.openMediaPlayer(usingMediaList: media,usingSettings: hostAppSettings, forViewController: flutterViewController)
            result(true)
            
        } else {
            print("iOS could not extract flutter arguments in method: (play)")
            result(false)
        }
    }
}
