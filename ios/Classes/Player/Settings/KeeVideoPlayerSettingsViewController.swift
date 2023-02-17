import UIKit
import AVFoundation

class KeeVideoPlayerSettingsViewController: UIViewController {

    @IBOutlet weak private var mainStack : UIStackView!
    
    var audioGroupList : AVMediaSelectionGroup?
    var subTitlesGroupList : AVMediaSelectionGroup?
    
    var itemsHasBeenSet = false
    var speedValues = [0.5, 1, 1.25, 1.5, 2]
    
    var didChangeAudioOption : ((_:AVMediaSelectionOption)->Void)!
    var didChangeSubTitleOption : ((_:AVMediaSelectionOption)->Void)!
    var didChangeSpeedOption : ((_:Double)->Void)!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    func playerSetOptions(){
        guard !itemsHasBeenSet else {return}
        itemsHasBeenSet = true
        if let audioGroup = SettingsGroup.createFromNib(), (audioGroupList?.options.count ?? 0) > 0 {
            audioGroup.headerTitle = "Audio"
            audioGroup.optionsList = audioGroupList?.options.map({SettingsOption(name: $0.displayName, selected: $0 == audioGroupList?.defaultOption)}).compactMap({$0}) ?? []
            
//            [SettingsOption(name: "English"),SettingsOption(name: "Arabic")]
            audioGroup.didSelectOption = { index in
                if let option = self.audioGroupList?.options[index] {
                    self.didChangeAudioOption(option)
                }
            }
            
            mainStack.addArrangedSubview(audioGroup)
            mainStack.addArrangedSubview(getSeparator())
        }
        
        if let subTitleGroup = SettingsGroup.createFromNib(), (subTitlesGroupList?.options.count ?? 0) > 0 {
            subTitleGroup.headerTitle = "SubTitle"
            subTitleGroup.optionsList = subTitlesGroupList?.options.map({SettingsOption(name: $0.displayName, selected: $0 == audioGroupList?.defaultOption)}).compactMap({$0}) ?? []
            
            subTitleGroup.didSelectOption = { index in
                if let option = self.subTitlesGroupList?.options[index] {
                    self.didChangeSubTitleOption(option)
                }
            }
            
            mainStack.addArrangedSubview(subTitleGroup)
            mainStack.addArrangedSubview(getSeparator())
        }
        
        if let speedGroup = SettingsGroup.createFromNib() {
            speedGroup.headerTitle = "Playback Speed"
            speedGroup.optionsList = speedValues.map({SettingsOption(name:"x\($0)")})
            /*[SettingsOption(name: "x0.25"),
                                      SettingsOption(name: "x0.5"),
                                      SettingsOption(name: "x1"),
                                      SettingsOption(name: "x1.25"),
                                      SettingsOption(name: "x1.5"),
                                      SettingsOption(name: "x2")]*/
            
            speedGroup.didSelectOption = { index in
                self.didChangeSpeedOption(self.speedValues[index])
                
            }
            
            mainStack.addArrangedSubview(speedGroup)
        }
        
    }

    
    private func getSeparator()->UIView{
        let v = UIView()
        v.translatesAutoresizingMaskIntoConstraints = false
        v.addConstraint(NSLayoutConstraint(item: v, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1, constant: 1))
        v.backgroundColor = .white
        return v
    }
    
    
    @IBAction func dismissView(){
        UIView.animate(withDuration: 0.245) {
            self.view.alpha = 0
        } completion: {  done in
            self.view.removeFromSuperview()
            self.view.alpha = 1
        }
    }
    
    func show(inView v : UIView){
        self.view.alpha = 0
        v.addSubview(view)
        playerSetOptions()
        self.view.translatesAutoresizingMaskIntoConstraints = false
        v.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "H:|-0-[view]-0-|", metrics: nil, views: ["view" : view!]))
        v.addConstraints(NSLayoutConstraint.constraints(withVisualFormat: "V:|-0-[view]-0-|", metrics: nil, views: ["view" : view!]))
        UIView.animate(withDuration: 0.245) {
            self.view.alpha = 1
        } completion: {  done in
            //self.view.removeFromSuperview()
            //self.view.alpha = 1
        }
        
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
