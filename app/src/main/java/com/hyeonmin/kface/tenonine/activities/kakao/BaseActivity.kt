package com.hyeonmin.kface.tenonine.activities.kakao

import android.app.Activity
import android.content.Intent
import com.hyeonmin.kface.tenonine.activities.IntroActivity
import com.hyeonmin.kface.tenonine.utils.Singleton
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.Session
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult


/**
 * @author leoshin, created at 15. 7. 20..
 */
abstract class BaseActivity : Activity() {
    protected fun showWaitingDialog() {}

    protected fun cancelWaitingDialog() {}

    protected fun redirectLoginActivity() {
        val intent = Intent(this, RootLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    protected fun redirectSignupActivity() {
        val intent = Intent(this, SampleSignupActivity::class.java)
        startActivity(intent)
    }

    fun goToLogin() {
        val intent = Intent(this, SampleLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    protected fun goToIntro() {
        var intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun checkSession() {
        AuthService.requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse>() {
            override fun onSessionClosed(errorResult: ErrorResult) {
                goToLogin()
            }

            override fun onNotSignedUp() {
                goToLogin()
            }

            override fun onFailure(errorResult: ErrorResult?) {
                goToLogin()
            }

            override fun onSuccess(accessTokenInfoResponse: AccessTokenInfoResponse) {
                Singleton.kakaoAccessToken = Session.getCurrentSession().accessToken
                goToIntro()
            }
        })
    }
}
