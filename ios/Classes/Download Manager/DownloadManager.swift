//
//  DownloadManager.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/12/2022.
//

import UIKit

public struct DownloadedMedia : Codable{
    var mediaId : String
    var mediaURL : URL?
    var mediaType : MediaManager.MediaType = .movie
    var path : URL{
        return FilesManager.shared.cache.appendingPathComponent(mediaType.rawValue, isDirectory: true).appendingPathComponent("\(name).mp4")
    }
    var name : String
    var tempPath : URL
    
    
    init(mediaId: String, mediaURL: URL? = nil, mediaType: MediaManager.MediaType = .movie, name: String, tempPath: URL) {
        self.mediaId = mediaId
        self.mediaURL = mediaURL
        self.mediaType = mediaType
        self.name = name
        self.tempPath = tempPath
    }
    private var data : Data?
    
    var object : [String:Any]? {
        set{
            if newValue != nil{
                self.data = try? JSONSerialization.data(withJSONObject: newValue!, options: .prettyPrinted)
            }
        }
        
        get{
            if let data = self.data{
                return try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]
            }
            return nil
        }
    }
    
    
    var mediaDownloadName : String {
        return mediaType.rawValue + "/" + "\(name)"
    }
    
    func store() throws{
        try FilesManager.shared.registerDownloadedMedia(self)
    }
    
    func remove() throws{
        try FilesManager.shared.deleteMediaBy(id: mediaId, type: self.mediaType)
    }
    
    static func getByID(id : String, ofType: MediaManager.MediaType)throws->DownloadedMedia?{
        return try FilesManager.shared.getDownloadeMediaById(id, type: ofType)
    }
    
        
    static func getAll(ofType : MediaManager.MediaType)throws->[DownloadedMedia]{
        return try FilesManager.shared.getDownloadList(type: ofType)
    }
    
}

public class DownloadManager: NSObject/*, ObservableObject */{
    static var shared = DownloadManager()
    public static var backgroundCompletionHandler : (() -> Void)?
    
    private var urlSession: URLSession!
//    @Published var tasks: [URLSessionTask] = []
    var tasks: [URLSessionTask] = []
    
    private var configed : Bool = false
    
    var didLoadPreListedTasks : (()->Void)?
    
    override private init() {
        super.init()
        let config = URLSessionConfiguration.background(withIdentifier: "\(Bundle.main.bundleIdentifier!).background")
        config.sessionSendsLaunchEvents = true
        config.isDiscretionary = true
        urlSession = URLSession(configuration: config, delegate: self, delegateQueue: nil)
        updateTasks()
    }
    
    func config(){ configed = true }

    func startDownload(url: URL, forMediaId id :Int, mediaName: String = "", type: MediaManager.MediaType, object: [String:Any]? = nil)  {
        if !configed {return}
        if tasks.contains(where: {$0.currentRequest?.url == url}) {
            return 
        }
        
        let task = urlSession.downloadTask(with: url)

        task.taskDescription = mediaName
        task.mediaId = "\(id)_\(type.rawValue)" //mediaID format (3255_movie)
        task.resume()
        tasks.append(task)
        
        if let object = object, let data = try? JSONSerialization.data(withJSONObject: object, options: .prettyPrinted){
            UserDefaults.standard.set(data, forKey: "\(id)_\(type.rawValue)")
        }
    }

    func updateTasks() {
        urlSession.getAllTasks { tasks in
            DispatchQueue.main.async {
                self.tasks = tasks
                self.didLoadPreListedTasks?()
            }
        }
    }
    
    //MARK: - Getting Download Task Progress
    
    func getAllDownloadingTasks(forType type : MediaManager.MediaType)->[URLSessionTask]{
        if !configed {return []}
        return tasks
    }
    
    func getDownloadTask(withMediaId id: String, forType type: MediaManager.MediaType)->URLSessionTask?{
        if !configed {return nil}
        let taskId = "\(id)_\(type.rawValue)"
        return tasks.first(where: {$0.mediaId == taskId})
    }
    
    func getDownloadProgress(ForMediaId id: String, ofMediaType type: MediaManager.MediaType)->Double?{
        return getDownloadTask(withMediaId: id, forType: type)?.progress.fractionCompleted
    }
    
    
    //MARK: - Cancel Download Functions
    func cancelAll(){
        if !configed {return}
        tasks.forEach({$0.cancel()})
        tasks.removeAll()
    }
    
    func cancelTask(withMediaId id: String, forType type: MediaManager.MediaType){
        if !configed {return}
        let taskId = "\(id)_\(type.rawValue)"
        cancelTask(withID: taskId)
    }
    
    func cancelTask(withID id: String){
        if !configed {return}
        tasks.first(where: {$0.mediaId == id})?.cancel()
    }
    
    //MARK: - Pause Download Functions
    
    func pauseDownloadForAllMedia(){
        if !configed {return}
        tasks.forEach({$0.suspend()})
    }
    
    func pauseDownload(forMediaId id: String, ofType type: MediaManager.MediaType){
        if !configed {return}
        let taskId = "\(id)_\(type.rawValue)"
        pauseDownload(forTaskID: taskId)
    }
    
    func pauseDownload(forTaskID id: String){
        if !configed {return}
        tasks.first(where: {$0.mediaId == id})?.suspend()
    }
    
    //MARK: - Resume Download Functions
    func resumeDownload(forMediaId id: String, ofType type: MediaManager.MediaType){
        if !configed {return}
        let taskId = "\(id)_\(type.rawValue)"
        resumeDownload(forTaskID: taskId)
    }
    
    func resumeDownload(forTaskID id: String){
        if !configed {return}
        tasks.first(where: {$0.mediaId == id})?.resume()
    }
    
    
    //MARK: - Check Download Status Functions
    ///is downloading regardless the status
    func isDownloadingMediaWithID(_ id : String, ofType type: MediaManager.MediaType)->Bool{
        let taskId = "\(id)_\(type.rawValue)"
        return tasks.contains(where: {taskId == "\($0.mediaId ?? "")"})
    }
    
    ///is downloading and is suspended
    func isDownloadingMediaWithIDSuspended(_ id : String, ofType type: MediaManager.MediaType)->Bool{
        let taskId = "\(id)_\(type.rawValue)"
        return tasks.first(where: {taskId == "\($0.mediaId ?? "")"})?.state == .suspended
    }
}

extension DownloadManager: URLSessionDelegate, URLSessionDownloadDelegate {
    public func urlSession(_: URLSession, downloadTask: URLSessionDownloadTask, didWriteData _: Int64, totalBytesWritten _: Int64, totalBytesExpectedToWrite _: Int64) {
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "downloadTask_media"), object: downloadTask)
        if let id = downloadTask.mediaId {
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "downloadTask_media_\(id)"), object: downloadTask)
        }
    }

    public func urlSession(_: URLSession, downloadTask d: URLSessionDownloadTask, didFinishDownloadingTo location: URL) {
        var obj = DownloadedMedia(mediaId: "\(d.mediaId ?? "")", name: d.taskDescription ?? "Untitled Media", tempPath: location)
        if let data = UserDefaults.standard.object(forKey: "\(d.mediaId ?? "")") as? Data,
           let object = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]{
            obj.object = object
            UserDefaults.standard.removeObject(forKey: "\(d.mediaId ?? "")")
        }
        do{
            try obj.store()
        }catch{
            print("Error:\(error)")
        }
            
        print("finished")
    }
    
    public func urlSessionDidFinishEvents(forBackgroundURLSession session: URLSession) {
        DispatchQueue.main.async {
               guard let backgroundCompletionHandler =
                   DownloadManager.backgroundCompletionHandler else {
                       return
               }
               backgroundCompletionHandler()
           }
    }

    public func urlSession(_: URLSession, task: URLSessionTask, didCompleteWithError error: Error?) {
        task.mediaId = nil
        if let error = error {
            print("error : \(error)")
        } else {
            print("Finish")
        }
    }
}



extension URLSessionTask {
    var mediaId : String?{
        set(value){
            if let value = value {
                UserDefaults.standard.set(value, forKey: "task_id_\(taskIdentifier)")
            }else{
                UserDefaults.standard.removeObject(forKey: "task_id_\(taskIdentifier)")
            }
        }
        get{
            return UserDefaults.standard.object(forKey: "task_id_\(taskIdentifier)") as? String
        }
    }
}
