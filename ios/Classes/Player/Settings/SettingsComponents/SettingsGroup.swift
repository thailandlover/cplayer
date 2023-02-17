import UIKit

class SettingsGroup : UIView, NibLoadable {
    @IBOutlet weak private var header : UILabel!
    @IBOutlet weak private var mainStack : UIStackView!
    var headerTitle : String = ""
    var optionsList : [SettingsOption] = []{
        didSet{
            for (i, o) in optionsList.enumerated() {
                if let v = SettingsOptionView.createFromNib() {
                    v.name = o.name
                    v.tag = i
                    v.didSelect = { btn in
                        self.deselectAll()
                        btn.isSelected = true
                        self.didSelectOption(btn.tag)
                    }
                    v.isSelected = o.selected
                    mainStack.addArrangedSubview(v)
                }
            }
        }
    }
    var didSelectOption : ((_:Int)->Void)!
    
    
    private func deselectAll(){
        mainStack.subviews.forEach { v in
            if let myView = v as? SettingsOptionView {
                myView.isSelected = false
            }
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = .clear
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        self.backgroundColor = .clear
        self.header.text = headerTitle
    }
    
}
