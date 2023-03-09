



import Foundation





class NetworkOperatinosManager {
    
    static var `default` = NetworkOperatinosManager()
    var session : URLSession
    private init(){
        session = URLSession(configuration: .default)
    }
    
    func updateWatchTime(forMedia media: Media,settings: HostAppSettings, updateTime time: Double, mediaDuration duration: Double){
        if settings.apiVersion == 4 {
            self.version2API(media: media,settings: settings, updateTime: time, mediaDuration: duration)
        }else{
            let link = settings.baseURL ?? "https://thekeeapp.com/api/v3/"
            if  let mediaId = media.keeId{
                var queryItems : [URLQueryItem] = []
                queryItems.append(URLQueryItem(name: "duration", value: "\(duration)"))
                queryItems.append(URLQueryItem(name: "lang", value: settings.lang))
                queryItems.append(URLQueryItem(name: "mediaId", value: mediaId))
                queryItems.append(URLQueryItem(name: "profileId", value: settings.KeeUser.profileID))
                queryItems.append(URLQueryItem(name: "time", value: "\(time)"))
                queryItems.append(URLQueryItem(name: "userId", value: settings.KeeUser.userID))
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
    
    private func version2API(media: Media, settings: HostAppSettings, updateTime time: Double, mediaDuration duration: Double){
        let link = settings.baseURL ?? "https://v4.kaayapp.com/api/"
        if var url = URL(string: link) {
            url.appendPathComponent(settings.baseType)
            url.appendPathComponent(settings.baseVersion)
            url.appendPathComponent("watching/add")
            var requestDir : [String:Any] = [:]
            requestDir["media_type"] = media.type.version_4_value
            requestDir["profile_id"] = settings.KeeUser.profileID
            requestDir["media_id"] = media.keeId ?? ""
            requestDir["duration"] = duration
            requestDir["time"] = time
            
            var headerDir : [String:String] = [:]
            headerDir["Accept-Language"] = settings.lang
            headerDir["Authorization"] = "Bearer \(settings.KeeUser.token)"
            
            
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.allHTTPHeaderFields = headerDir
            request.httpBody = requestDir.queryFormattedString?.data(using: .utf8)
            //request.httpBody = try? JSONSerialization.data(withJSONObject: requestDir,options: .fragmentsAllowed)
            //session.dataTask(with: request).resume()
            session.dataTask(with: request) { d, response, error in
                print(error)
                if let data = d {
                    let s = String(data: data, encoding: .utf8)
                    print(s)
                }
            }.resume()
            
        }
        
    }
            
}


// This extension used to convert [String:Any] type to JSON string or Data
public extension Dictionary where Key == String{
    
    /// Returns Binary (``Data``) of Dictionary object
    var jsonData : Data? {
        do{
            return try JSONSerialization.data(withJSONObject: self, options: .prettyPrinted)
        }catch{
        }
        return nil
    }
    
    /// Returns ``String`` based on JSON formate of Dictionary
    var jsonFormattedString:String? {
        guard self.count > 0 else {return nil}
        
         do {
                   let stringData = try JSONSerialization.data(withJSONObject: self as NSDictionary, options: JSONSerialization.WritingOptions.fragmentsAllowed)
                   if let string = String(data: stringData, encoding: .utf8){
                       return string
                   }
               }catch  {
                   
               }
               return nil
    }
    
    var queryFormattedString:String?{
        guard self.count > 0 else {return nil}
        
        var queries : [URLQueryItem] = []
        for (key, value) in self {
            queries.append(URLQueryItem(name: key, value: "\(value)"))
        }
        var comp = URLComponents()
            comp.queryItems = queries
        
        return comp.percentEncodedQuery
    }
    
    //Other extensions to [String:Any] can be added here
}
