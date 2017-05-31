package com.hyeonmin.kface.tenonine.activities.kakao

import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.hyeonmin.kface.tenonine.R
import com.kakao.auth.AuthType

import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger

/**
 * Sample Activity that dose not use fragment for login.
 */
class SampleLoginActivity : BaseActivity() {
    private var callback: SessionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_sample_login)

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            checkSession()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }
    }
}
