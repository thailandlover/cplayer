//
//  HostAppSettings.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/03/2023.
//

import Foundation


public struct HostAppSettings {
    var KeeUser : KeeUser = .debuging
    
    var lang : String
    var baseURL : String?
    var apiVersion : Int = 3
    
    //API2 attributes
    var baseType : String = ""
    var baseVersion : String = ""
    var acceptType : String = ""
        
    static var `default` = HostAppSettings(lang: "en")
    static var default_4 = HostAppSettings(lang: "en", apiVersion: 4)
    
    
    var userSignature : String {
        return "\(KeeUser.userID)_\(KeeUser.profileID)"
    }
}
