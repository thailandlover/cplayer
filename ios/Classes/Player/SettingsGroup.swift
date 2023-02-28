//
//  SettingsGroup.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 22/11/2022.
//

import UIKit

public class SettingsGroup : UIView, NibLoadable {
  
    
    @IBOutlet weak private var header : UILabel!
    @IBOutlet weak private var mainStack : UIStackView!
    var headerTitle : String = ""
    var optionsList : [SettingsOption] = []{
        didSet{
            for (i, o) in optionsList.enumerated() {
                if let v = SettingsOptionView.createFromNib(in: .packageBundle) {
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
    
    public override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = .clear
    }
    
    public override func draw(_ rect: CGRect) {
        super.draw(rect)
        self.backgroundColor = .clear
        self.header.text = headerTitle
    }
    
}
