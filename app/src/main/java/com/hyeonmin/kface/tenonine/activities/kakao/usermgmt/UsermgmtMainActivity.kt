/**
 * Copyright 2014-2016 Kakao Corp.

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
package com.hyeonmin.kface.tenonine.activities.kakao.usermgmt

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.activities.kakao.BaseActivity
import com.hyeonmin.kface.tenonine.activities.kakao.widget.ProfileLayout
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.helper.log.Logger

/**
 * 가입된 사용자가 보게되는 메인 페이지로 사용자 정보 불러오기/update, 로그아웃, 탈퇴 기능을 테스트 한다.
 */
class UsermgmtMainActivity : BaseActivity() {
    private var userProfile: UserProfile? = null
    private var profileLayout: ProfileLayout? = null
    private var extraUserPropertyLayout: ExtraUserPropertyLayout? = null

    /**
     * 로그인 또는 가입창에서 넘긴 유저 정보가 있다면 저장한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    override protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        initializeView()
    }

    override protected fun onResume() {
        super.onResume()
        userProfile = UserProfile.loadFromCache()
        if (userProfile != null)
            showProfile()
    }

    /**
     * 사용자의 정보를 변경 저장하는 API를 호출한다.
     */
    private fun onClickUpdateProfile() {
        val properties = extraUserPropertyLayout!!.properties
        UserManagement.requestUpdateProfile(object : UsermgmtResponseCallback<Long>() {
            override fun onSuccess(result: Long?) {
                userProfile!!.updateUserProfile(properties)
                if (userProfile != null) {
                    userProfile!!.saveUserToCache()
                }
//                KakaoToast.makeToast(getApplicationContext(), "succeeded to update user profile", Toast.LENGTH_SHORT).show()
                Logger.d("succeeded to update user profile" + userProfile!!)
                showProfile()
            }

        }, properties)
    }

    private fun onClickAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse>() {
            override fun onSessionClosed(errorResult: ErrorResult) {
                redirectLoginActivity()
            }

            override fun onNotSignedUp() {
                // not happened
            }

            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get access token info. msg=" + errorResult!!
                Logger.e(message)
//                KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show()
            }

            override fun onSuccess(accessTokenInfoResponse: AccessTokenInfoResponse) {
                val userId = accessTokenInfoResponse.userId
                Logger.d("this access token is for userId=" + userId)

                val expiresInMilis = accessTokenInfoResponse.expiresInMillis
                Logger.d("this access token expires after $expiresInMilis milliseconds.")

//                KakaoToast.makeToast(getApplicationContext(), "this access token for user(id=$userId) expires after $expiresInMilis milliseconds.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun onClickLogout() {
        UserManagement.requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                redirectLoginActivity()
            }
        })
    }

    private fun onClickUnlink() {
        val appendMessage = getString(R.string.com_kakao_confirm_unlink)
        AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        DialogInterface.OnClickListener { dialog, which ->
                            UserManagement.requestUnlink(object : UnLinkResponseCallback() {
                                override fun onFailure(errorResult: ErrorResult?) {
                                    Logger.e(errorResult!!.toString())
                                }

                                override fun onSessionClosed(errorResult: ErrorResult) {
                                    redirectLoginActivity()
                                }

                                override fun onNotSignedUp() {
                                    redirectSignupActivity()
                                }

                                override fun onSuccess(result: Long?) {
                                    redirectLoginActivity()
                                }
                            })
                            dialog.dismiss()
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).show()

    }


    private fun showProfile() {
//        if (profileLayout != null) {
//            profileLayout!!.setUserProfile(userProfile)
//        }
//
//        if (extraUserPropertyLayout != null) {
//            extraUserPropertyLayout!!.showProperties(userProfile!!.properties)
//        }
    }

    private fun initializeView() {
        setContentView(R.layout.layout_usermgmt_main)
        (findViewById(R.id.text_title) as TextView).setText(getString(R.string.text_usermgmt))
        findViewById(R.id.title_back).setOnClickListener(OnClickListener { finish() })

        initializeButtons()
        initializeProfileView()
    }

    private fun initializeButtons() {
        val buttonMe = findViewById(R.id.buttonMe) as Button
        buttonMe.setOnClickListener { profileLayout!!.requestMe() }

        val buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile) as Button
        buttonUpdateProfile.setOnClickListener { onClickUpdateProfile() }

        val logoutButton = findViewById(R.id.logout_button) as Button
        logoutButton.setOnClickListener { onClickLogout() }

        val unlinkButton = findViewById(R.id.unlink_button) as Button
        unlinkButton.setOnClickListener { onClickUnlink() }

        val tokenInfoButton = findViewById(R.id.token_info_button) as Button
        tokenInfoButton.setOnClickListener { onClickAccessTokenInfo() }
    }

    private fun initializeProfileView() {
        profileLayout = findViewById(R.id.com_kakao_user_profile) as ProfileLayout
        profileLayout!!.setMeResponseCallback(object : MeResponseCallback() {
            override fun onNotSignedUp() {
                redirectSignupActivity()
            }

            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get user info. msg=" + errorResult!!
                Logger.e(message)
//                KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show()
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                redirectLoginActivity()
            }

            override fun onSuccess(result: UserProfile?) {
//                KakaoToast.makeToast(getApplicationContext(), "succeeded to get user profile", Toast.LENGTH_SHORT).show()
                if (result != null) {
                    this@UsermgmtMainActivity.userProfile = result
                    userProfile!!.saveUserToCache()
                    showProfile()
                }
            }
        })

        extraUserPropertyLayout = findViewById(R.id.extra_user_property) as ExtraUserPropertyLayout
    }

    private abstract inner class UsermgmtResponseCallback<T> : ApiResponseCallback<T>() {
        override fun onNotSignedUp() {
            redirectSignupActivity()
        }

        override fun onFailure(errorResult: ErrorResult?) {
            val message = "failed to get user info. msg=" + errorResult!!
            Logger.e(message)
//            KakaoToast.makeToast(getApplicationContext(), message, Toast.LENGTH_LONG).show()
        }

        override fun onSessionClosed(errorResult: ErrorResult) {
            redirectLoginActivity()
        }
    }
}
