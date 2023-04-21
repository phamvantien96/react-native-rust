//
//  RNChannel.m
//  ReactNativeRust
//
//  Created by Phạm Tiến on 19/04/2023.
//

#import <Foundation/Foundation.h>
#import "RNChannel.h"

@interface RCT_EXTERN_MODULE(RNChannel, RCTEventEmitter)
RCT_EXTERN_METHOD(response:(NSString *)uuid
                  response:(NSString *)response
                  resolver:(RCTPromiseResolveBlock)resolver
                  rejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(call:(NSString *)uuid
                  method:(NSString *)method
                  request:(NSString *)request
                  resolver:(RCTPromiseResolveBlock)resolver
                  rejecter:(RCTPromiseRejectBlock)reject)
@end
