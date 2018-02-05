package com.johnsontech.reactnativeandroidhybrid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.johnsontech.reactnativeandroidhybrid.other.AndroidWidgetPackage
import kotlinx.android.synthetic.main.activity_react_comunucatuin.*


class ReactCommunicationActivity : AppCompatActivity() {

    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    private var reactPackage: AndroidWidgetPackage? = null
    private var mClickTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_react_comunucatuin)

        mReactRootView = ReactRootView(this)
        reactPackage = AndroidWidgetPackage()
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(MainReactPackage())
                .addPackage(reactPackage)   //加入AndroidModule
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()

        // 注意這裡的MyReactNativeApp必須對應“index.android.js”中的
        // “AppRegistry.registerComponent()”的第一個參數
//        mReactRootView!!.startReactApplication(mReactInstanceManager, "Communication3", null)
        react_root_view1.startReactApplication(mReactInstanceManager, "Communication2", null)
        react_root_view2.startReactApplication(mReactInstanceManager, "Communication3", null)

        //將ReactView模塊添加進布局
//        val linearLayout = findViewById<LinearLayout>(R.id.root_view)
//        linearLayout.addView(mReactRootView)

        //添加本地按鈕的點擊事件
        native_btn.setOnClickListener {
            reactPackage!!.getToastModule().sendMessage("這是一條Android發送給React的消息${mClickTime++}")
        }
    }
}
