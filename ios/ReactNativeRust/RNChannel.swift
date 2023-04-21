//
//  RNChannel.swift
//  ReactNativeRust
//
//  Created by Phạm Tiến on 19/04/2023.
//

import Foundation
import Connect
import OSLog

@objc(RNChannel)
class RNChannel: RCTEventEmitter {
    override func supportedEvents() -> [String]! {
      return []
    }
    
    @objc
    func response(_ uuid: String, response: String, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        
    }
    
    @objc
    func call(_ uuid: String, method: String, request: String, resolver: @escaping RCTPromiseResolveBlock, rejecter: @escaping RCTPromiseRejectBlock) {
        os_log("call success");
    }
}

