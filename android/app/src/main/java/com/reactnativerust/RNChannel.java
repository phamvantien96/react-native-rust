package com.reactnativerust;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.util.JsonFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RNChannel extends ReactContextBaseJavaModule implements RpcChannel {
    private Map<UUID, RpcCallback> callbackMap = new HashMap<>();
    private Map<UUID, Message.Builder> responseBuilderMap = new HashMap<>();
    private static final String TAG = RNChannel.class.getSimpleName();

    public RNChannel(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return RNChannel.class.getSimpleName();
    }

    @Override
    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype, RpcCallback<Message> done) {
        Log.d(TAG, "callMethod: " + request.toString());
        UUID uuid = UUID.randomUUID();

        try {
            WritableMap   arg   = Arguments.createMap();

            arg.putString("uuid", uuid.toString());
            arg.putString("request", JsonFormat.printer().print(request));
            callbackMap.put(uuid, done);
            responseBuilderMap.put(uuid, responsePrototype.newBuilderForType());
            getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(method.getFullName().toLowerCase(), arg);
        } catch (IOException e) {
            Log.e(TAG, "error: ", e);
        };

    }

    @ReactMethod
    public void response(String uuid, String response) {
        Log.d(TAG, "response: " + response);
        try {
            Message.Builder builder = responseBuilderMap.remove(UUID.fromString(uuid));
            RpcCallback<Message> cb = callbackMap.remove(UUID.fromString(uuid));
            if (builder != null) {
                JsonFormat.parser().ignoringUnknownFields().merge(response, builder);
            }

            if (cb != null) {
                cb.run(builder.build());
            }
        } catch (IOException e) {
            Log.e(TAG, "error: ", e);
        };
    }

    public static class EmptyRpcCallback implements RpcCallback<Empty> {
        @Override
        public void run(Empty parameter) {
        }
    }
}
