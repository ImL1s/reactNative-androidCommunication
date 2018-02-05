package com.johnsontech.reactnativeandroidhybrid.other

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.*


/**
 * Created by ImL1s on 31/01/2018.
 * Description:
 */
class AndroidWidgetPackage : ReactPackage {

    private var nativeModules: MutableList<NativeModule>? = null

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        // 在這裡將需要溝通原生模組放入list並回傳,這樣就等於將原生註冊到react native
        nativeModules = ArrayList()
        nativeModules!!.add(0, ToastAndroidModule(reactContext))
        return nativeModules as ArrayList<NativeModule>
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return Collections.emptyList()
    }

    /**
     * 方便取得AndroidModule的方法
     */
    fun getModule(index: Int): NativeModule? {
        return if (nativeModules == null) null else nativeModules!![index]
    }

    fun getToastModule(): ToastAndroidModule {
        return getModule(0) as ToastAndroidModule
    }
}