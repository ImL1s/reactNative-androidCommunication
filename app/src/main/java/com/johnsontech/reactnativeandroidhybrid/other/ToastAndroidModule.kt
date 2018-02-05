package com.johnsontech.reactnativeandroidhybrid.other

import com.facebook.react.modules.core.DeviceEventManagerModule
import android.widget.Toast
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule


/**
 * Created by ImL1s on 31/01/2018.
 * Description:
 */
class ToastAndroidModule(private val reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    /**
     * react native call android時的模組名稱
     */
    override fun getName(): String {
        return "ToastAndroidModule"
    }

    /**
     * react native call(->) android
     */
    @ReactMethod
    fun HandleMessage(message: String) {
        Toast.makeText(reactContext, message, Toast.LENGTH_LONG).show()
    }

    /**
     * android call(->) react native
     */
    fun sendMessage(params: String) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("mEventName", params)
    }
}