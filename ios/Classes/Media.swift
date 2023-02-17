import UIKit

struct Media {
    var title : String
    var subTitle : String?
    var urlToPlay : String
    var currentWatchTime : Double?
    var keeId : String?
    
    var type : MediaManager.MediaType = .movie
            
    var KeeUser : KeeUser = .debuging
    var settings : HostAppSettings = .default
    
    var startAt : Float = 0
}

struct KeeUser {
    var userID : String
    var profileID : String
    
    static var debuging = KeeUser(userID: "0", profileID: "-1")
}

struct HostAppSettings {
    var lang : String
    var baseURL : String?
    
    
    static var `default` = HostAppSettings(lang: "en")
}
