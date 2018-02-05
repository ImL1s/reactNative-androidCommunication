package com.johnsontech.reactnativeandroidhybrid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.johnsontech.reactnativeandroidhybrid.other.CommunicationInterface
import com.johnsontech.reactnativeandroidhybrid.other.MReactPacakge
import kotlinx.android.synthetic.main.activity_react_comunucatuin.*


class ReactComunicationActivity : AppCompatActivity() {


    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    private var reactPackage: MReactPacakge? = null
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_react_comunucatuin)

        mReactRootView = ReactRootView(this)
        reactPackage = MReactPacakge()
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(MainReactPackage())
                .addPackage(reactPackage)   //添加  本地 moudel
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()

        // 注意这里的MyReactNativeApp必须对应“index.android.js”中的
        // “AppRegistry.registerComponent()”的第一个参数
//        mReactRootView!!.startReactApplication(mReactInstanceManager, "Communication3", null)
        react_root_view1.startReactApplication(mReactInstanceManager, "Communication2", null)
        react_root_view2.startReactApplication(mReactInstanceManager, "Communication3", null)

        //将  ReactView 模块添加进 布局
//        val linearLayout = findViewById<LinearLayout>(R.id.root_view)
//        linearLayout.addView(mReactRootView)

        //添加  本地按钮的  点击事件：
        native_btn.setOnClickListener {
            if (reactPackage!!.getInterf(0) != null) {
                (reactPackage!!.getInterf(0) as CommunicationInterface).sendMessage("这是本地发送的消息" + i++)
            }
        }
    }

    fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
