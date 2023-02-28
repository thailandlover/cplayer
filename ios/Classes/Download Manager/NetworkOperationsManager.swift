



import Foundation

class NetworkOperatinosManager {
    
    static var `default` = NetworkOperatinosManager()
    var session : URLSession
    private init(){
        session = URLSession(configuration: .default)
    }
    
    func updateWatchTime(forMedia media: Media, updateTime time: Double, mediaDuration duration: Double){
        let link = media.settings.baseURL ?? "https://thekeeapp.com/api/v3/"
        if  let mediaId = media.keeId{
            var queryItems : [URLQueryItem] = []
            queryItems.append(URLQueryItem(name: "duration", value: "\(duration)"))
            queryItems.append(URLQueryItem(name: "lang", value: media.settings.lang))
            queryItems.append(URLQueryItem(name: "mediaId", value: mediaId))
            queryItems.append(URLQueryItem(name: "profileId", value: media.KeeUser.profileID))
            queryItems.append(URLQueryItem(name: "time", value: "\(time)"))
            queryItems.append(URLQueryItem(name: "userId", value: media.KeeUser.userID))
            queryItems.append(URLQueryItem(name: "type", value: media.type.rawValue))
            
            var urlComponents = URLComponents(string: link.appending("media/updateCurrentTime"))
            urlComponents?.queryItems = queryItems
            if let url = urlComponents?.url {
                let request = URLRequest(url: url)
                session.dataTask(with: request).resume()
            }
        }
    }
            
}
