/**
 * Copyright 2014-2015 Kakao Corp.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyeonmin.kface.tenonine.activities.kakao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast

import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.activities.kakao.usermgmt.ExtraUserPropertyLayout
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.ErrorCode
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.helper.log.Logger

/**
 * 유효한 세션이 있다는 검증 후
 * me를 호출하여 가입 여부에 따라 가입 페이지를 그리던지 Main 페이지로 이동 시킨다.
 */
open class SampleSignupActivity : BaseActivity() {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_intro)
        requestMe()
    }

    protected fun showSignup() {
        setContentView(R.layout.layout_usermgmt_signup)
        val extraUserPropertyLayout = findViewById(R.id.extra_user_property) as ExtraUserPropertyLayout
        val signupButton = findViewById(R.id.buttonSignup) as Button
        signupButton.setOnClickListener { requestSignUp(extraUserPropertyLayout.properties) }
    }

    private fun requestSignUp(properties: Map<String, String>) {
        UserManagement.requestSignup(object : ApiResponseCallback<Long>() {
            override fun onNotSignedUp() {}

            override fun onSuccess(result: Long?) {
                requestMe()
            }

            override fun onFailure(errorResult: ErrorResult?) {
                val message = "UsermgmtResponseCallback : failure : " + errorResult!!
                com.kakao.util.helper.log.Logger.w(message)
//                KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onSessionClosed(errorResult: ErrorResult) {}
        }, properties)
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected fun requestMe() {
        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get user info. msg=" + errorResult!!
                Logger.d(message)

                val result = ErrorCode.valueOf(errorResult.errorCode)
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
//                    KakaoToast.makeToast(getApplicationContext(), getString(R.string.error_message_for_service_unavailable), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    redirectLoginActivity()
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                redirectLoginActivity()
            }

            override fun onSuccess(userProfile: UserProfile) {
                Logger.d("UserProfile : " + userProfile)
                redirectMainActivity()
            }

            override fun onNotSignedUp() {
                showSignup()
            }
        })
    }

    private fun redirectMainActivity() {
//        val intent = Intent(this, KakaoServiceListActivity::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
    }
}
