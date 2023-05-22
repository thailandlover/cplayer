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
            break;		
        case "play_movie":
            self.playMovie(call: call, result: result)
            break;
        case "config_downloader":
            configDownloader(call: call, result: result);
        case "get_download_movie":
            getDownloadMovie(call: call, result: result)
        case "get_download_episode":
            getDownloadEpisode(call: call, result: result)
        case "get_downloads_list":
            getDownloadsList(call: call, result: result)
        case "start_download_movie":
            startDownloadMovie(call: call, result: result)
        case "start_download_episode":
            startDownloadEpisode(call: call, result: result)
        case "pause_download":
            pauseDownload(call: call, result: result)
        case "resume_download":
            resumeDownload(call: call, result: result)
        case "cancel_download":
            cancelDownload(call: call, result: result)
        case "tvshow_seasons_downloads_list":
            tvShowSeasonsDownloadList(call:call,result:result)
        case "season_episodes_downloads_list":
            seasonEpisodesDownloadList(call:call,result:result)
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
            Task {
                let keeUser = KeeUser(userID: user_id, profileID: profile_id,token: token)
                let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang,baseURL: api_base_url,apiVersion: 4,baseType: "mobile",baseVersion: "v4",acceptType: "ios")
                let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
                let itemsIds: [String:Any] = media_group["items_ids"] as! [String : Any]
                let episodes: [[String:Any]] = media_group["episodes"] as! [[String:Any]]
                let playIndex: Int = Int(info["order"] as! String)! - 1;
                var media : [Media] = []
                let _ : [String:Any] = media_group["tv_show"] as! [String:Any]
                let season : [String:Any] = media_group["season"] as! [String:Any]
                let seasonNumber:Any = season["season_number"]!
                
                let tvShow :[String:Any]  = media_group["tv_show"]  as! [String : Any]
                let tvShowTitle:Any = tvShow["title"]!
                
                for episode in episodes {
                    let mediaId = String(episode["id"] as! Int);
                    let mediaGroup : MediaGroup = MediaGroup(showId: itemsIds["tv_show_id"] as! String, seasonId: itemsIds["season_id"] as! String, episodeId: mediaId,seasonName: "Season \(seasonNumber)",showName: tvShowTitle as! String ,data: media_group)
                    let watching : [String:Any]? = episode["watching"] as? [String : Any]
                    var startAt:Float = 0.0
                    if(watching != nil) {
                        startAt = Float(watching?["current_time"] as! String)!
                    }
                    let mediaItem = Media(title: episode["title"] as! String,subTitle: season["title"] as? String, urlToPlay: episode["media_url"] as! String,downloadURL: episode["download_url"] as! String,keeId: mediaId,type: mediaType, startAt: startAt,mediaGroup: mediaGroup,info: episode)
                    media.append(mediaItem)
                    
                }
                let playerResult : [[String:Any]] = await MediaManager.default.openMediaPlayer(usingMediaList: media,playMediaIndex: playIndex,usingSettings: hostAppSettings, forViewController: flutterViewController)
                if(playerResult.isEmpty){
                    result(false)
                } else {
                    result(playerResult)
                }
            }
        } else {
            print("iOS could not extract flutter arguments in method: (playEpisode)")
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
            Task {
                let keeUser = KeeUser(userID: user_id, profileID: profile_id,token: token)
                let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang,baseURL: api_base_url,apiVersion: 4,baseType: "mobile",baseVersion: "v4",acceptType: "ios")
                let mediaType : MediaManager.MediaType = media_type == "movie" ? .movie : .series
                
                var media : [Media] = []
                
                let mediaItem = Media(title: title,subTitle: sub_title, urlToPlay: url,downloadURL: episode["download_url"] as! String,keeId: media_id,type: mediaType, startAt: start_at,info: info)
                media.append(mediaItem)
                
                let playerResult : [[String:Any]] = await MediaManager.default.openMediaPlayer(usingMediaList: media,usingSettings: hostAppSettings, forViewController: flutterViewController)
                if(playerResult.isEmpty){
                    result(false)
                } else {
                    result(playerResult)
                }
            }
           
            
        } else {
            print("iOS could not extract flutter arguments in method: (playMovie)")
            result(false)
        }
    }
    
    func getDownloadsList(call: FlutterMethodCall,result: @escaping FlutterResult){
        let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
        result(downloadsList)
    }
    
    func getDownloadMovie(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_id : String = myArgs["media_id"] as? String {
            let results: DownloadedMedia? = DownloadManager.shared.getDownloadedMovie(media_id)
            
            if(results != nil){
                result([results?.getObjectAsJSONDictionary()])
            } else {
                result([])
            }
        } else {
            print("iOS could not extract flutter arguments in method: (getDownloadMovie)")
            result(false)
        }
    }
    
    func getDownloadEpisode(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_id : String = myArgs["media_id"] as? String,
           let tvshow_id : String = myArgs["tvshow_id"] as? String,
           let season_id : String = myArgs["season_id"] as? String{
            let results: DownloadedMedia? = DownloadManager.shared.getDownloadedEpisode(media_id, seasonId: season_id, tvShowId: tvshow_id)
            if(results != nil){
                result([results?.getObjectAsJSONDictionary()])
            } else {
                result([])
            }
        } else {
            print("iOS could not extract flutter arguments in method: (getDownloadEpisode)")
            result(false)
        }
    }
    
    
    
    func configDownloader(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let user_id : String = myArgs["user_id"] as? String,
           let profile_id : String = myArgs["profile_id"] as? String,
           let lang : String = myArgs["lang"] as? String {
            let keeUser = KeeUser(userID: user_id, profileID: profile_id)
            let hostAppSettings = HostAppSettings(KeeUser:keeUser, lang: lang)
            
            DownloadManager.shared.config(useSettings: hostAppSettings)
            result(true)
        } else {
            print("iOS could not extract flutter arguments in method: (configDownloader)")
            result(false)
        }
    }
    
    func tvShowSeasonsDownloadList(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let tvShowId : String = myArgs["tvshow_id"] as? String {
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllSeasonsDecoded(forSeries: tvShowId)
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (tvShowSeasonsDownloadList)")
            result(false)
        }
    }

    func seasonEpisodesDownloadList(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let tvShowId : String = myArgs["tvshow_id"] as? String,
           let seasonId : String = myArgs["season_id"] as? String{
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllEpisodesDecoded(forSeason: seasonId, atSeriesID: tvShowId)
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (seasonEpisodesDownloadList)")
            result(false)
        }
    }
    
    func startDownloadMovie(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any]
        {
            let url : URL = URL(string: myArgs["url"] as! String)!
            let mediaId: Int = Int(myArgs["media_id"] as! String)!
            
            DownloadManager.shared.startDownload(url: url, forMediaId: mediaId, type: .movie,mediaGroup: nil, object:myArgs)
            
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
            
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (startDownload)")
            result(false)
        }
        
    }
    
    func startDownloadEpisode(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any]{
            
            let info : [String:Any] = (myArgs["info"] as? [String:Any])!
            let media_group : [String:Any] = (myArgs["media_group"] as? [String:Any])!
            
            let mediaId = String(info["id"] as! Int);
            let url : URL = URL(string: info["download_url"] as! String)!
            
            
            let itemsIds: [String:Any] = media_group["items_ids"] as! [String : Any]
            let season :[String:Any]  = media_group["season"]  as! [String : Any]
            let seasonNumber:Any = season["season_number"]!
            
            let tvShow :[String:Any]  = media_group["tv_show"]  as! [String : Any]
            let tvShowTitle:Any = tvShow["title"]!
            
            let mediaGroup : MediaGroup = MediaGroup(showId: itemsIds["tv_show_id"] as! String, seasonId: itemsIds["season_id"] as! String, episodeId: mediaId,seasonName: "Season \(seasonNumber)",showName: tvShowTitle as! String ,data: media_group)
            
            DownloadManager.shared.startDownload(url: url, forMediaId: Int(mediaId )!, type: .series,mediaGroup: mediaGroup, object:myArgs)
            
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
            
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (startDownload)")
            result(false)
        }
        
    }
    
    func pauseDownload(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_type : String = myArgs["mediaType"] as? String,
           let media_id : String = myArgs["mediaId"] as? String {
            DownloadManager.shared.pauseDownload(forMediaId: media_id, ofType: media_type == "movie" ? .movie : .series)
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (pauseDownload)")
            result(false)
        }
    }
    
    func resumeDownload(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_type : String = myArgs["mediaType"] as? String,
           let media_id : String = myArgs["mediaId"] as? String {
            DownloadManager.shared.resumeDownload(forMediaId: media_id, ofType: media_type == "movie" ? .movie : .series)
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (resumeDownload)")
            result(false)
        }
    }
    
    func cancelDownload(call: FlutterMethodCall,result: @escaping FlutterResult){
        guard let args = call.arguments else {
            return
        }
        if let myArgs = args as? [String: Any],
           let media_type : String = myArgs["mediaType"] as? String,
           let media_id : String = myArgs["mediaId"] as? String {
            DownloadManager.shared.cancelMedia(withMediaId: media_id, forType: media_type == "movie" ? .movie : .series)
            let downloadsList : [[String : Any]] = DownloadManager.shared.getAllMediaDecoded()
            result(downloadsList)
        } else {
            print("iOS could not extract flutter arguments in method: (cancelDownload)")
            result(false)
        }
    }
    
    
}
