package com.reactnativerust;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

  private static final String TAG = "MainActivity";

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  // @Override
  // protected String getMainComponentName() {
  //   return "ReactNativeRust";
  // }

  // /**
  //  * Returns the instance of the {@link ReactActivityDelegate}. Here we use a util class {@link
  //  * DefaultReactActivityDelegate} which allows you to easily enable Fabric and Concurrent React
  //  * (aka React 18) with two boolean flags.
  //  */
  // @Override
  // protected ReactActivityDelegate createReactActivityDelegate() {
  //   return new DefaultReactActivityDelegate(
  //       this,
  //       getMainComponentName(),
  //       // If you opted-in for the New Architecture, we enable the Fabric Renderer.
  //       DefaultNewArchitectureEntryPoint.getFabricEnabled(), // fabricEnabled
  //       // If you opted-in for the New Architecture, we enable Concurrent React (i.e. React 18).
  //       DefaultNewArchitectureEntryPoint.getConcurrentReactEnabled() // concurrentRootEnabled
  //       );
  // }

  @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          Math.AddRequest request = Math.AddRequest.newBuilder().setFirstNumber(4).setSecondNumber(5).build();
          Math.MathService service = Math.MathService.Stub.newStub(RNPackage.getChannel());
          service.add(RNPackage.getController(), request, new RpcCallback<Math.AddResponse>() {
            @Override
            public void run(Math.AddResponse parameter) {
              Log.d(TAG, "callback " + parameter.getResult());
            }
          });

          service.sub(RNPackage.getController(), Empty.getDefaultInstance(), new RNChannel.EmptyRpcCallback());
        }
      };

      timer.schedule(task, 5000, 5000);
      // startService(new Intent(this, RNBridgeService.class));
    }
}
