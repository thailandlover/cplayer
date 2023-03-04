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
           let api_base_url : String = myArgs["api_base_url"] as? String,
           let start_at : Float = myArgs["start_at"] as? Float,
           let lang : String = myArgs["lang"] as? String {
            let keeUser = KeeUser(userID: user_id, profileID: profile_id)
            let hostAppSettings = HostAppSettings(lang: lang,baseURL: api_base_url)
            let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
            var media : [Media] = []
            media.append(Media(title: title,subTitle: sub_title, urlToPlay: url,keeId: media_id,type: mediaType, KeeUser: keeUser,settings:hostAppSettings, startAt: start_at))
            
            MediaManager.default.openMediaPlayer(usingMediaList: media, forViewController: flutterViewController)
            
        } else {
            print("iOS could not extract flutter arguments in method: (play)")
            result(false)
        }
    }
}
