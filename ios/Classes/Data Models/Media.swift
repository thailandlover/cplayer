//
//  Media.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 11/11/2022.
//

import UIKit

public struct Media {
    var title : String
    var subTitle : String?
    var urlToPlay : String
    @available(*, deprecated, renamed: "startAt")
    var currentWatchTime : Double?
    var keeId : String?
    var type : MediaManager.MediaType = .movie
    var startAt : Float = 0
    var mediaGroup : MediaGroup?
    var info : [String:Any]?
}




