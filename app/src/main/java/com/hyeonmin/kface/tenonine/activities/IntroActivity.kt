package com.hyeonmin.kface.tenonine.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window

import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.asynctasks.TenONineLoginAsyncTask
import com.hyeonmin.kface.tenonine.utils.Secrets
import com.hyeonmin.kface.tenonine.utils.Singleton
import org.json.JSONObject

class IntroActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_intro)

        TenONineLoginAsyncTask(this).execute("${Secrets.apiUrl}/auth/login?access_token=${Singleton.kakaoAccessToken}")
    }

    fun getTenONineAccesstoken(str: String) {
        Singleton.tenONineAccessToken = JSONObject(str)["access_token"].toString()
        var intent = Intent(this, WebViewActivity::class.java)
        startActivity(intent)
        finish()
    }
}
