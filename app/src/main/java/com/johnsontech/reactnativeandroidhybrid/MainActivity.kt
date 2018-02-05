package com.johnsontech.reactnativeandroidhybrid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_react_content.setOnClickListener {
            val intent = Intent(this, ReactContentActivity::class.java)
            startActivity(intent)
        }

        btn_react_communication.setOnClickListener {
            val intent = Intent(this, ReactComunicationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }
}
