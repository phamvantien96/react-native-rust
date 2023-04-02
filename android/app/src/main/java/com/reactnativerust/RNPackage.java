package com.reactnativerust;

import android.util.Log;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RNPackage implements ReactPackage {
    static private RNChannel channel;
    static private RNController controller;

    static public RNChannel getChannel() {
        return channel;
    }

    static public RNController getController() {
        return controller;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        channel = new RNChannel(reactContext);
        Log.d("MainActivity", "create chanel");
        modules.add(channel);

        return modules;
    }

}
