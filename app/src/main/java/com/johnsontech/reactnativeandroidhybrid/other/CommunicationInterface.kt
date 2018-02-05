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
class CommunicationInterface(private val reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "CommunicationInterface"
    }

    /**
     * Rn 需要调用的方法：
     * @param message
     */
    @ReactMethod
    fun HandleMessage(message: String) {
        Toast.makeText(reactContext, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 发送消息到  RN 界面
     */
    fun sendMessage(params: String) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("mEventName", params)
    }
}