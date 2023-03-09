//
//  KeeUser.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/03/2023.
//

import Foundation


public struct KeeUser {
    var userID : String
    var profileID : String
    var token : String = ""
    
    static var debuging = KeeUser(userID: "0", profileID: "-1", token: "")
}
