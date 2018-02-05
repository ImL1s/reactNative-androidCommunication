package com.johnsontech.reactnativeandroidhybrid.other

import com.facebook.react.bridge.NativeModule
import java.util.Collections.emptyList
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.ReactPackage
import com.facebook.react.uimanager.ViewManager
import java.util.*
import java.util.Collections.emptyList




/**
 * Created by ImL1s on 31/01/2018.
 * Description:
 */
class MReactPacakge : ReactPackage {

    private var nativeModules: MutableList<NativeModule>? = null

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        nativeModules = ArrayList()
        nativeModules!!.add(CommunicationInterface(reactContext))
        return nativeModules as ArrayList<NativeModule>
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return Collections.emptyList()
    }

    fun getInterf(index: Int): NativeModule? {
        return if (nativeModules == null) null else nativeModules!![index]
    }
}