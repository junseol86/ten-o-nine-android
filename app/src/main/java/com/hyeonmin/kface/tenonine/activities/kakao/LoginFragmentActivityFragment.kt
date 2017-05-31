package com.hyeonmin.kface.tenonine.activities.kakao

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hyeonmin.kface.tenonine.R
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.usermgmt.LoginButton
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger

/**
 * Sample login fragmeent. Note that LoginButton's setFragment() should be called in order for
 * login button to work properly.
 */
class LoginFragmentActivityFragment : android.support.v4.app.Fragment() {

    private var callback: SessionCallback? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_login_fragment, container, false)

//        view.findViewById(R.id.login_button_fragment).setSuportFragment(this) // set fragment for LoginButton

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            val intent = Intent(activity, SampleSignupActivity::class.java)
            startActivity(intent)
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }
    }
}
