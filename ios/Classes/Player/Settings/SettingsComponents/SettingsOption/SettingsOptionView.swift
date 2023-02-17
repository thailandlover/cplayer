import UIKit

class SettingsOptionView : UIView, NibLoadable {
    @IBOutlet weak private var markerView : UIView!
    @IBOutlet weak private var innerMarkerView : UIView!
    @IBOutlet weak private var titleLabel : UILabel!
    var name : String = ""
    var didSelect : ((_ : SettingsOptionView)->Void)?
    
    var isSelected : Bool = false {
        didSet{
            innerMarkerView.isHidden = !isSelected
        }
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        
        [markerView, innerMarkerView].forEach { v in
            v.layer.cornerRadius = v.bounds.width / 2
        }
        
        markerView.layer.borderWidth = 2.5
        markerView.layer.borderColor = UIColor.white.cgColor
        markerView.backgroundColor = .clear
        
        innerMarkerView.layer.borderColor = UIColor.clear.cgColor
        innerMarkerView.backgroundColor = UIColor(named: "mainColor" ) ?? .systemRed
                                
        titleLabel.text = name
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = .clear
    }
    
    @IBAction func selectButton(_ sender : Any?) {
        self.isSelected = true
        didSelect?(self)
    }
}





struct SettingsOption {
    var name : String
    var selected : Bool = false
}
