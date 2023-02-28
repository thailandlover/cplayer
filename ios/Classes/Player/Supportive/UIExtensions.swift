//
//  UIExtensions.swift
//  KeeCustomPlayer
//
//  Created by Ahmed Qazzaz on 14/11/2022.
//

import UIKit


extension String {
    func subString(from: Int, to: Int) -> String {
        let startIndex = self.index(self.startIndex, offsetBy: from)
        let endIndex = self.index(self.startIndex, offsetBy: to)
        return String(self[startIndex...endIndex])
    }
}

extension NSLayoutConstraint {
    
    func constraintWithMultiplier(_ multiplier: CGFloat) -> NSLayoutConstraint {
        guard let fitem = self.firstItem else {return self}
        return NSLayoutConstraint(item: fitem, attribute: self.firstAttribute, relatedBy: self.relation, toItem: self.secondItem, attribute: self.secondAttribute, multiplier: multiplier, constant: self.constant)
    }
}




extension UIView {
    
    @IBOutlet weak var leadingConstraint : NSLayoutConstraint?{
        set(value){
            
//            if(value?.firstAttribute != .leading){
//                Swift.print("Trying to assign not leading constaint to leading constraint")
//            }
            
            objc_setAssociatedObject(self, &AssociatedKeys.leadingKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
            
        }
        get{
            return objc_getAssociatedObject(self, &AssociatedKeys.leadingKey) as? NSLayoutConstraint
        }
    }
    
       @IBOutlet weak var trailingConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.trailingKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.trailingKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var topConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.topKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.topKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var bottomConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.bottomKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.bottomKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var centerXConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.centerXKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.centerXKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var centerYConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.centerYKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.centerYKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var heightConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.heightKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.heightKey) as? NSLayoutConstraint
           }
       }
    
       @IBOutlet weak var widthConstraint : NSLayoutConstraint?{
           set(value){
               objc_setAssociatedObject(self, &AssociatedKeys.widthKey, value,  objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
               
           }
           get{
               return objc_getAssociatedObject(self, &AssociatedKeys.widthKey) as? NSLayoutConstraint
           }
       }
}

public protocol NibLoadable {
    // Name of the nib file
    static var nibName: String { get }
    static func createFromNib(in bundle: Bundle?) -> Self?
}


public extension NibLoadable where Self: UIView {

    // Default nib name must be same as class name
    static var nibName: String {
        return String(describing: Self.self)
    }

    static func createFromNib(in bundle: Bundle?) -> Self? {
        let useBundle = bundle ?? .main
        if let topLevelArray = useBundle.loadNibNamed(nibName, owner: self){
            let views = Array<Any>(topLevelArray).filter { $0 is Self }
            return views.last as? Self
        }
        return nil        
    }
}
