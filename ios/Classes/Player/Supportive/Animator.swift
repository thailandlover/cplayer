//
//  Animator.swift
//  UnitOneFramework
//
//  Created by Ahmed Qazzaz on 6/29/20.
//  Copyright © 2020 unitone. All rights reserved.
//

import UIKit


struct AssociatedKeys {
    static var leadingKey: UInt8 = 1
    static var trailingKey: UInt8 = 2
    static var topKey: UInt8 = 3
    static var bottomKey: UInt8 = 4
    static var centerXKey: UInt8 = 5
    static var centerYKey: UInt8 = 6
    static var heightKey: UInt8 = 7
    static var widthKey: UInt8 = 8
    
    static var uSessionTask_mediaID: UInt8 = 10
}

///Create basic and simple animations
///
///The UOAnimator class used for simple animation, it can not serve with complex and large animations
///The supported animations are, moving, scaling, rotation, and appearing
/// - Functions list:
///     - **init** : Constructor
///     - **update**(duration, delay, animationOption, damping, velocity)
///     - **updateDuration**(double)
///     - **updateDelay**(double)
///     - **updateAnimationOption**(AnimationOptions)
///     - **updateDamping**(CGFloat)
///     - **updateVelocity**(CGFloat)
///     - **rotateAnimation**(view,angle, animationHandler, CompletionHandler)
///     - **alphaAnimation**(view,angle, animationHandler, CompletionHandler)
///     - **scaleAnimation**(view,angle, animationHandler, CompletionHandler)
///     - **initPost**(view, constant vlaue for constraints...)
///     - **animateTo**(view, constant vlaue for constraints...,animationHandler, CompletionHandler)
///     - **animateFrom**(view, constant vlaue for constraints...,animationHandler, CompletionHandler)
///     - **typingAnimation**(label, text, duration, isAppending)
public class UOAnimator {
    var animationOptions : UIView.AnimationOptions?
    var delay : Double = 0
    var duration : Double = 0.245
    var damping : CGFloat = 0.5
    var velocity : CGFloat = 5
    
    /// overloaded constructor
    ///
    /// you can pass one or more of the following parameters to set your animator properties
    /// - parameter duration : double value the define the time needed to complete the animation defualt is 0.245
    /// - parameter delay : double value to define the delay time before stating the animation default is 0
    /// - parameter animationOptions : optional UIView.AnimationOptions default is nil
    /// - parameter damping : CGFloat value used to define the bouncing around the end point of animation used only in movment animations, lower values means heigher bouncing around point, 1 means no bouncing, default is 0.5
    /// - parameter velocity: CGFloat value define the speed of bouncing default is 5
    public init(duration : Double = 0.245, delay : Double = 0, animationOptions : UIView.AnimationOptions? = nil, damping : CGFloat = 0.5, velocity : CGFloat = 5) {
        self.delay = delay
        self.duration = duration
        self.animationOptions = animationOptions
        self.damping = damping
        self.velocity = velocity
    }
        
    /// Used to update any value of the animator
    ///
    /// Function is overloaded to multiple function
    /// ```
    /// update() // This will prints warning : update function called in animtor with no updates
    /// update(duration : 1)
    /// update(delay : 1)
    /// update(animationOption : .curveEaseOut)
    /// update(damping: 1)
    /// update(velocity: 1)
    /// ```
    /// You can make any combination of these parameter , for example
    /// ```
    /// update(duration: 1, deley: 3)
    /// update(animationOption : .curveEaseInOut, damping: 1, velocity: 10)
    /// ```
    /// Also you can chain these function, for example
    /// ```
    /// update(delay : 1).update(damping: 1, velocity: 1)
    /// ```
    /// - parameter duration : optional double value the define the time needed to complete the animation defualt is nil
    /// - parameter delay : optional double value to define the delay time before stating the animation default is nil
    /// - parameter animationOptions : optional UIView.AnimationOptions default is nil
    /// - parameter damping : optional CGFloat value used to define the bouncing around the end point of animation used only in movment animations, lower values means heigher bouncing around point, 1 means no bouncing, default is nil
    /// - parameter velocity: optional CGFloat value define the speed of bouncing default is nil
    /// - Returns: `self`
    @discardableResult
    public func update(duration : Double? = nil, delay : Double? = nil, animationOptions : UIView.AnimationOptions? = nil, damping : CGFloat? = nil, velocity : CGFloat? = nil)->UOAnimator{
        
        var numberOfUpdates = 0
        if let value = delay { numberOfUpdates+=1; updateDelay(value)}
        if let value = duration { numberOfUpdates+=1; updateDuration(value)}
        if let value = animationOptions { numberOfUpdates+=1; updateOption(value)}
        if let value = damping { numberOfUpdates+=1; updateDamping(value)}
        if let value = velocity { numberOfUpdates+=1; updateVelocity(value)}
        
        if(numberOfUpdates == 0){
            print("update function called in animtor with no updates")
        }
        return self
    }
    
    
    /// Update delay value only
    ///
    /// Use this function when you are willing to change the delay value of animator before starting new animation
    /// - Parameter newValue : Double value  used for delay value
    /// - Returns: `self`
    @discardableResult
    public func updateDelay(_ newValue : Double)->UOAnimator {
        self.delay = newValue
        return self
    }
    /// Update duration value only
    ///
    /// Use this function when you are willing to change the duration value of animator before starting new animation
    /// - Parameter newValue : Double value  used for duration value
    /// - Returns: `self`
    @discardableResult
    public func updateDuration(_ newValue : Double)->UOAnimator {
        self.duration = newValue
        return self
    }
    
    /// Update damping value only
    ///
    /// Use this function when you are willing to change the damping value of animator before starting new animation
    /// - Parameter newValue : CGFloat value  used for damping value
    /// - Returns: `self`
    @discardableResult
    public func updateDamping(_ newValue : CGFloat)->UOAnimator {
        self.damping = newValue
        return self
    }
    
    /// Update velocity value only
    ///
    /// Use this function when you are willing to change the velocity value of animator before starting new animation
    /// - Parameter newValue : CGFloat value  used for velocity value
    /// - Returns: `self`
    @discardableResult
    public func updateVelocity(_ newValue : CGFloat)->UOAnimator {
        self.velocity = newValue
        return self
    }
    
    /// Update animation options value only
    ///
    /// Use this function when you are willing to change the animation options value of animator before starting new animation
    /// - Parameter newValue : UIView.AnimationOptions value  used for animation options value
    /// - Returns: `self`
    @discardableResult
    public func updateOption(_ newValue : UIView.AnimationOptions)->UOAnimator {
        self.animationOptions = newValue
        return self
    }
    
    
    /// Apply scale animation to view
    ///
    /// Used to enlarge or shrink view element, the origin point for all views are the center point, it might not work will with corner raduis  (circle views)
    ///
    /// - 0: disappear
    /// - 1: original size
    /// - -1: mirror original size
    /// - less than 1: shrink
    /// - larger than 1: enlarge
    ///
    /// - parameter view: the view that will animate
    /// - parameter scaleX: The percentage of X axis scalling, based on the equation of scaleX * width
    /// - parameter scaleY: The percentage of Y axis scalling, based on the equation of scaleY * height
    /// - parameter animation: is a handler you can use if you want to achieve code in the animation closure of the `UIView.animate` function
    /// - parameter completion: is a handler you can use if you want to achieve code in the completion closure of the `UIView.animate` function (Whene the animation complete)
    public func scaleAnimation(forView view : UIView,
                               scaleX : CGFloat,
                               scaleY : CGFloat,
                               animation:  (()->Void)? = nil,
                               completion: (()->Void)? = nil){
        
        UIView.animate(withDuration:duration,
                       delay: delay,
                       usingSpringWithDamping: damping,
                       initialSpringVelocity: velocity,
                       options: .curveEaseInOut,
                       animations: {
                        
                        view.transform = CGAffineTransform(scaleX: scaleX, y: scaleY)
                        animation?()
        }) { (done) in
            completion?()
        }
        
    }
    /// Apply rotation animation to view
    ///
    /// Used to rotate view element, the origin point for all views are the center point
    ///
    ///
    /// - parameter view: the view that will animate
    /// - parameter angle: the angle value in degress scale
    /// - parameter animation: is a handler you can use if you want to achieve code in the animation closure of the `UIView.animate` function
    /// - parameter completion: is a handler you can use if you want to achieve code in the completion closure of the `UIView.animate` function (Whene the animation complete)
    public func rotateAnimation(forView view: UIView,
                                angle: CGFloat,
                                animation:  (()->Void)? = nil,
                                completion: (()->Void)? = nil){
        
        UIView.animate(withDuration:duration,
                       delay: delay,
                       usingSpringWithDamping: damping,
                       initialSpringVelocity: velocity,
                       options: .curveEaseInOut,
                       animations: {
                        
                        view.transform = CGAffineTransform(rotationAngle: angle);
                        animation?()
        }) { (done) in
            completion?()
        }
    }
    
    
    public func makeAnimation( animation:  @escaping (()->Void),
                               completion: (()->Void)? = nil) {
        UIView.animate(withDuration:duration,
                       delay: delay,
                       usingSpringWithDamping: damping,
                       initialSpringVelocity: velocity,
                       options: .curveEaseInOut,
                       animations: {
                        animation()
        }) { (done) in
            completion?()
        }
    }
    
    
    /// Apply alpha animation to view
    ///
    /// Used to show or hide view element
    ///
    ///
    /// - parameter view: the view that will animate
    /// - parameter value: the value of view alpha you want to reach
    /// - parameter animation: is a handler you can use if you want to achieve code in the animation closure of the `UIView.animate` function
    /// - parameter completion: is a handler you can use if you want to achieve code in the completion closure of the `UIView.animate` function (Whene the animation complete)
    
    public func alphaAnimation(forView view: UIView,
                               value : CGFloat,
                               animation:  (()->Void)? = nil,
                               completion: (()->Void)? = nil) {
        UIView.animate(withDuration:duration,
                       delay: delay,
                       usingSpringWithDamping: damping,
                       initialSpringVelocity: velocity,
                       options: .curveEaseInOut,
                       animations: {
                        
                        view.alpha = value
                        animation?()
        }) { (done) in
            completion?()
        }
    }
    
    /// Move object out of the screen from the left side
    ///
    /// This function used to set an object out of the screen from the left side
    /// - parameter view: the view that will animate
    /// - parameter animated : Bool value used to allow animation, true means this object will move on duration, false means the object will be set directlly out of the screen
    public func exitLeft(forView view: UIView,
                         animated : Bool = false){
        if view.leadingConstraint != nil {
            if(animated){
                self.animateTo(forView: view, leadingConstant: -(view.frame.width))
            }else{
                self.initPos(forView: view, leadingConstant: -(view.frame.width))
            }
        }else if view.trailingConstraint != nil {
            if(animated){
                self.animateTo(forView: view, trailingConstant: UIScreen.main.bounds.width + view.frame.width)
            }else{
                self.initPos(forView: view, trailingConstant: UIScreen.main.bounds.width + view.frame.width)
            }
        }else{
            print("Unable to <exitLeft> since there is no leading or trailing Constraint assinged to outlets")
        }
    }
    
    /// Move object out of the screen from the right side
    ///
    /// This function used to set an object out of the screen from the right side
    /// - parameter view: the view that will animate
    /// - parameter animated : Bool value used to allow animation, true means this object will move on duration, false means the object will be set directlly out of the screen
    public func exitRight(forView view: UIView,
                          animated : Bool = false){
        if view.trailingConstraint != nil {
            if(animated){
                self.animateTo(forView: view,trailingConstant: (view.frame.width))
            }else{
                self.initPos(forView: view,trailingConstant: (view.frame.width))
            }
        }else if view.leadingConstraint != nil {
            if(animated){
                self.animateTo(forView: view,leadingConstant: -(UIScreen.main.bounds.width + view.frame.width))
            }else{
                self.initPos(forView: view,leadingConstant: -(UIScreen.main.bounds.width + view.frame.width))
            }
        }else{
            print("Unable to <exitRight> since there is no trailing or leading Constraint assinged to outlets")
        }
    }
    
    /// Set object position
    ///
    /// This function used to initialize original position for objects based on object constraints.
    ///
    /// use this function if you are willing to use the `animateTo` function since this function used to define the stating position of an object, i.e. you make the desing in the storyboard as the final screen should be, and use this to move object away from final position then use `animateTo` to bring them back to the new positions.
    ///
    ///  **I do not recomment this function for storyboard elements**
    ///
    /// - parameter view: the view that will animate
    /// - parameter leadingConstant: the new constant for leading constraint
    /// - parameter trailingConstant: the new constant for trailing constraint
    /// - parameter topConstant: the new constant for top constraint
    /// - parameter bottomConstant: the new constant for bottom constraint
    /// - parameter centerXConstant: the new constant for centerX constraint
    /// - parameter centerXMultiplier: the new multiplier for centerX constraint
    /// - parameter centerYConstant: the new constant for centerY constraint
    /// - parameter centerYMultiplier: the new multiplier for centerT constraint
    /// - parameter heightConstant: the new constant for height constraint
    /// - parameter widthConstant: the new constant for width constraint
    public func initPos(forView view: UIView,
                        leadingConstant: CGFloat? = nil,
                        trailingConstant: CGFloat? = nil,
                        topConstant: CGFloat? = nil,
                        bottomConstant: CGFloat? = nil,
                        centerXConstant: CGFloat? = nil,
                        centerXMultiplier: CGFloat? = nil,
                        centerYConstant: CGFloat? = nil,
                        centerYMultiplier: CGFloat? = nil,
                        heightConstant: CGFloat? = nil,
                        widthConstant: CGFloat? = nil){
        
        
        
        
        let previousD = self.duration
        self.duration = 0
        self.animateTo(forView: view,
                       leadingConstant: leadingConstant,
                       trailingConstant: trailingConstant,
                       topConstant: topConstant,
                       bottomConstant: bottomConstant,
                       centerXConstant: centerXConstant,
                       centerXMultiplier: centerXMultiplier,
                       centerYConstant: centerYConstant,
                       centerYMultiplier: centerYMultiplier,
                       heightConstant: heightConstant,
                       widthConstant: widthConstant)
        self.duration = previousD
    }
    
    
    /// Move object to new positon based on constraints
    ///
    /// This function used to move object to new position based on object constraints.
    ///
    ///  **I do not recomment this function for storyboard elements**
    /// - parameter view: the view that will animate
    /// - parameter leadingConstant: the new constant for leading constraint
    /// - parameter trailingConstant: the new constant for trailing constraint
    /// - parameter topConstant: the new constant for top constraint
    /// - parameter bottomConstant: the new constant for bottom constraint
    /// - parameter centerXConstant: the new constant for centerX constraint
    /// - parameter centerXMultiplier: the new multiplier for centerX constraint
    /// - parameter centerYConstant: the new constant for centerY constraint
    /// - parameter centerYMultiplier: the new multiplier for centerT constraint
    /// - parameter heightConstant: the new constant for height constraint
    /// - parameter widthConstant: the new constant for width constraint
    /// - parameter animation: is a handler you can use if you want to achieve code in the animation closure of the `UIView.animate` function
    /// - parameter completion: is a handler you can use if you want to achieve code in the completion closure of the `UIView.animate` function (Whene the animation complete)
    public func animateTo(forView view: UIView,
                          leadingConstant: CGFloat? = nil,
                          trailingConstant: CGFloat? = nil,
                          topConstant: CGFloat? = nil,
                          bottomConstant: CGFloat? = nil,
                          centerXConstant: CGFloat? = nil,
                          centerXMultiplier: CGFloat? = nil,
                          centerYConstant: CGFloat? = nil,
                          centerYMultiplier: CGFloat? = nil,
                          heightConstant: CGFloat? = nil,
                          widthConstant: CGFloat? = nil,
                          animation:  (()->Void)? = nil,
                          completion: (()->Void)? = nil){
        
        let option = self.animationOptions ?? .curveEaseOut
        
//        UnitOneSignature.print("animator v1.2", level: .infomative)
        UIView.animate(withDuration:duration,
                       delay: delay,
                       usingSpringWithDamping: damping,
                       initialSpringVelocity: velocity,
                       options: option,
                       animations: {
                        
                        if let value = leadingConstant {view.leadingConstraint?.constant = value}
                        if let value = trailingConstant {view.trailingConstraint?.constant = value}
                        if let value = topConstant {view.topConstraint?.constant = value}
                        if let value = bottomConstant {view.bottomConstraint?.constant = value}
                        if let value = centerXConstant {view.centerXConstraint?.constant = value}
                        if let value = centerYConstant {view.centerYConstraint?.constant = value}
                        if let value = heightConstant {view.heightConstraint?.constant = value}
                        if let value = widthConstant {view.widthConstraint?.constant = value}
                        if let value = centerXMultiplier {
                            if let centerX = view.centerXConstraint {
                                if  let newConstraint = view.centerXConstraint?.constraintWithMultiplier(value) {
                                    view.superview?.removeConstraint(centerX)
                                    view.superview?.addConstraint(newConstraint)
                                    view.centerXConstraint = newConstraint
                                }
                            }
                        }
                        if let value = centerYMultiplier {
                            if let centerY = view.centerYConstraint {
                                if  let newConstraint = view.centerYConstraint?.constraintWithMultiplier(value) {
                                    view.superview?.removeConstraint(centerY)
                                    view.superview?.addConstraint(newConstraint)
                                    view.centerYConstraint = newConstraint
                                }
                            }
                        }
                        view.superview?.layoutIfNeeded()
                        animation?()
                        
        }) { (done) in
            completion?()
        }
    }
    
    /// type characters one by one
    ///
    /// - parameter label : UILable to apply animtion on it
    /// - parameter text : String to be writen or appended to the label
    /// - parameter duration : typing speed in milliseconds use large numbers 1000000 for one second
    /// - parameter appending : Bool it indecate if animation should write the text on the label, or add the text to the existing label text, default false meaning that text will be writen not appended.
    public func typeAnimation(forLabel label: UILabel, text: String, duration: useconds_t, appending : Bool = false,
                              completion: (()->Void)? = nil){
        if #available(iOS 10.0, *) {
            Thread.detachNewThread {
                if(self.delay > 0) {
                    let t = UInt32(self.delay * 1000000)
                    usleep(t)
                }
                var counter = 0
                while(counter < text.count){
                    DispatchQueue.main.async{
                        if(appending){
                            label.text! += text.subString(from: max(0, counter - 1), to: counter)
                        }
                        else{
                            label.text = text.subString(from: 0, to: counter)
                        }
                    }
                    usleep(duration)
                    counter+=1
                }
                completion?()
            }
        } else {
            // Fallback on earlier versions
            Thread.detachNewThreadSelector(#selector(detachThread_legacy(_:)), toTarget: self, with:ThreadObjectWrapper(objects: label, text, appending, completion))
        }
    }
    
    @objc func detachThread_legacy(_ obj : Any?){
        
        guard let wrapper = obj as? ThreadObjectWrapper,
            let label = wrapper[0] as? UILabel,
            let text = wrapper[1] as? String,
            let appending = wrapper[2] as? Bool
            else{ return }
        
        let completion = wrapper[3] as? (()->Void)
        
        if(self.delay > 0) {
            let t = UInt32(self.delay * 1000000)
            usleep(t)
        }
        var counter = 0
        while(counter < text.count){
            DispatchQueue.main.async{
                if(appending){
                    label.text! += text.subString(from: max(0, counter - 1), to: counter)
                }
                else{
                    label.text = text.subString(from: 0, to: counter)
                }
            }
            usleep(useconds_t(self.duration))
            counter+=1
        }
        completion?()
    }
    
    
    /// Move object from a position to its current position
    ///
    /// When setting elements in storyboard as final design shows, and you need to move them into the screen from another positions, use this function to define the positions that objects should move from to the current position
    ///
    /// - parameter view: the view that will animate
    /// - parameter leadingConstant: the new constant for leading constraint
    /// - parameter trailingConstant: the new constant for trailing constraint
    /// - parameter topConstant: the new constant for top constraint
    /// - parameter bottomConstant: the new constant for bottom constraint
    /// - parameter centerXConstant: the new constant for centerX constraint
    /// - parameter centerXMultiplier: the new multiplier for centerX constraint
    /// - parameter centerYConstant: the new constant for centerY constraint
    /// - parameter centerYMultiplier: the new multiplier for centerT constraint
    /// - parameter heightConstant: the new constant for height constraint
    /// - parameter widthConstant: the new constant for width constraint
    /// - parameter animation: is a handler you can use if you want to achieve code in the animation closure of the `UIView.animate` function
    /// - parameter completion: is a handler you can use if you want to achieve code in the completion closure of the `UIView.animate` function (Whene the animation complete)
    public func animateFrom(forView view: UIView,
                            leadingConstant: CGFloat? = nil,
                            trailingConstant: CGFloat? = nil,
                            topConstant: CGFloat? = nil,
                            bottomConstant: CGFloat? = nil,
                            centerXConstant: CGFloat? = nil,
                            centerXMultiplier: CGFloat? = nil,
                            centerYConstant: CGFloat? = nil,
                            centerYMultiplier: CGFloat? = nil,
                            heightConstant: CGFloat? = nil,
                            widthConstant: CGFloat? = nil,
                            bouncingFactot : CGFloat = 0,
                            animation:  (()->Void)? = nil,
                            completion: (()->Void)? = nil){
        
        
        let wrapper = ConstraintsWrapper(leadingConstant: view.leadingConstraint?.constant,
                                         trailingConstant: view.trailingConstraint?.constant,
                                         topConstant: view.topConstraint?.constant,
                                         bottomConstant: view.bottomConstraint?.constant,
                                         heightConstant: view.heightConstraint?.constant,
                                         widthConstant: view.widthConstraint?.constant,
                                         centerXConstant: view.centerXConstraint?.constant,
                                         centerYConstant: view.centerYConstraint?.constant,
                                         centerXMultiplier: view.centerXConstraint?.multiplier,
                                         centerYMultiplier: view.centerYConstraint?.multiplier)
        initPos(forView: view,
                leadingConstant: leadingConstant,
                trailingConstant: trailingConstant,
                topConstant: topConstant,
                bottomConstant: bottomConstant,
                centerXConstant: centerXConstant,
                centerXMultiplier: centerXMultiplier,
                centerYConstant: centerYConstant,
                centerYMultiplier: centerYMultiplier,
                heightConstant: heightConstant,
                widthConstant: widthConstant)
        
        
        animateTo(forView: view,
                  leadingConstant: wrapper.leadingConstant,
                  trailingConstant: wrapper.trailingConstant,
                  topConstant: wrapper.topConstant,
                  bottomConstant: wrapper.bottomConstant,
                  centerXConstant: wrapper.centerXConstant,
                  centerXMultiplier: wrapper.centerXMultiplier,
                  centerYConstant: wrapper.centerYConstant,
                  centerYMultiplier: wrapper.centerYMultiplier,
                  heightConstant: wrapper.heightConstant,
                  widthConstant: wrapper.widthConstant)
    }
    
}

struct ConstraintsWrapper {
    var leadingConstant : CGFloat?
    var trailingConstant : CGFloat?
    var topConstant : CGFloat?
    var bottomConstant : CGFloat?
    var heightConstant : CGFloat?
    var widthConstant : CGFloat?
    var centerXConstant : CGFloat?
    var centerYConstant : CGFloat?
    var centerXMultiplier : CGFloat?
    var centerYMultiplier : CGFloat?
}


class ThreadObjectWrapper{
    var objects : [Any?]
    
    init(objects : Any?...) {
        self.objects = objects
    }
    
    subscript (_ value : Int)->Any? {
        get{
            if(value > 0  && value < objects.count){
                return objects[value]
            }else{
                return nil
            }
        }
    }
    
}
