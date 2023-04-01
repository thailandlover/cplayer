//
//  DownloadManager.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 05/12/2022.
//

import UIKit



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
    
    public func config(){ configed = true }

    public func startDownload(url: URL,
                              forMediaId id :Int,
                              mediaName: String = "",
                              type: MediaManager.MediaType,
                              mediaGroup: MediaGroup?,
                              object: [String:Any]? = nil)  {
        if !configed {return}
        if tasks.contains(where: {$0.currentRequest?.url == url}) {
            return 
        }
        
        let task = urlSession.downloadTask(with: url)

        task.taskDescription = mediaName
        task.mediaId = "\(id)_\(type.rawValue)" //mediaID format (3255_movie) or (36970_series)
        task.resume()
        tasks.append(task)
        
        if let object = object, let data = try? JSONSerialization.data(withJSONObject: object, options: .prettyPrinted){
            UserDefaults.standard.set(data, forKey: "\(id)_\(type.rawValue)")
        }
        mediaGroup?.register()
        
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
    
    public func getDownloadProgress(ForMediaId id: String, ofMediaType type: MediaManager.MediaType)->Double?{
        return getDownloadTask(withMediaId: id, forType: type)?.progress.fractionCompleted
    }
    
    
    //MARK: - Cancel Download Functions
    public func cancelAll(){
        if !configed {return}
        tasks.forEach({$0.cancel()})
        tasks.removeAll()
    }
    
    public func cancelMedia(withMediaId id: String, forType type: MediaManager.MediaType){
        if !configed {return}
        let taskId = "\(id)_\(type.rawValue)"
        cancelTask(withID: taskId)
    }
    
    func cancelTask(withID id: String){
        if !configed {return}
        tasks.first(where: {$0.mediaId == id})?.cancel()
        tasks.removeAll(where: {$0.mediaId == id})
    }
    
    //MARK: - Pause Download Functions
    
    public func pauseDownloadForAllMedia(){
        if !configed {return}
        tasks.forEach({$0.suspend()})
    }
    
    public func pauseDownload(forMediaId id: String, ofType type: MediaManager.MediaType){
        if !configed {return}
        let taskId = "\(id)_\(type.rawValue)"
        pauseDownload(forTaskID: taskId)
    }
    
    func pauseDownload(forTaskID id: String){
        if !configed {return}
        tasks.first(where: {$0.mediaId == id})?.suspend()
    }
    
    //MARK: - Resume Download Functions
    public func resumeDownload(forMediaId id: String, ofType type: MediaManager.MediaType){
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
    public func isDownloadingMediaWithID(_ id : String, ofType type: MediaManager.MediaType)->Bool{
        let taskId = "\(id)_\(type.rawValue)"
        return tasks.contains(where: {taskId == "\($0.mediaId ?? "")"})
    }
    
    ///is downloading and is suspended
    public func isDownloadingMediaWithIDSuspended(_ id : String, ofType type: MediaManager.MediaType)->Bool{
        let taskId = "\(id)_\(type.rawValue)"
        return tasks.first(where: {taskId == "\($0.mediaId ?? "")"})?.state == .suspended
    }
    
 
    public func mediaIsDownloaded(_ id : String, ofType: MediaManager.MediaType)->Bool {
        return (try? FilesManager.shared.getDownloadeMediaById(id, type: ofType) != nil) ?? false
    }
    //MARK: - Hybrid Functions
    ///Get all media (downloading, suspended, and downloaded)
    public func getAllMedia() throws -> [DownloadedMedia]{
        guard configed else {throw DonwloadManagerError.managerIsNotConfiged}
        var allMedia : [DownloadedMedia] = []
        
        allMedia.append(contentsOf: tasks.map({ task in
            if let t = task as? URLSessionDownloadTask {
                return extractMedia(usingTask: t)
            }
            return nil
        }).compactMap({$0}) )
        allMedia = allMedia.filter({$0.object != nil})
        
        allMedia.append(contentsOf: FilesManager.shared.getAllDownloadedMedia())
        
        return allMedia
    }
    
    public func getAllMediaDecoded()-> [[String:Any]] {
        return (try? self.getAllMedia().getEncodedDictionary()) ?? []
    }
    
    
    func extractMedia(usingTask task : URLSessionDownloadTask)->DownloadedMedia{
        let  pureID = task.mediaId?.components(separatedBy: "_").first
        let pureType = task.mediaId?.components(separatedBy: "_").last
        
        var obj = DownloadedMedia(mediaId: pureID ?? "", name: task.taskDescription ?? "Untitled Media", status: task.state, progress: task.progress.fractionCompleted)
        
        if pureType != "movies"{
            obj.mediaType = .series
        }
        
        if let data = UserDefaults.standard.object(forKey: "\(task.mediaId ?? "")") as? Data,
           let object = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]{
            obj.object = object
        }
        if let  pureID = pureID {
            obj.group = MediaGroup.get(usingEpisodeID: pureID)
        }
        
        return obj
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
        
        let  pureID = d.mediaId?.components(separatedBy: "_").first
        let pureType = d.mediaId?.components(separatedBy: "_").last
        
        var obj = DownloadedMedia(mediaId: pureID ?? "", name: d.taskDescription ?? "Untitled Media", tempPath: location)
        
        if pureType != "movies"{
            obj.mediaType = .series
        }
        
        if let data = UserDefaults.standard.object(forKey: "\(d.mediaId ?? "")") as? Data,
           let object = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed) as? [String:Any]{
            obj.object = object
            UserDefaults.standard.removeObject(forKey: "\(d.mediaId ?? "")")            
        }
        if let  pureID = pureID {
            var group = MediaGroup.get(usingEpisodeID: pureID)
            obj.group = group
            UserDefaults.standard.removeObject(forKey: "\(d.mediaId ?? "")_group")
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
