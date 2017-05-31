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
package com.hyeonmin.kface.tenonine.activities.kakao.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

//import com.android.volley.toolbox.NetworkImageView
import com.hyeonmin.kface.tenonine.R
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile

//TODO image upload, back button, cancel button
/**
 * 기본 UserProfile(사용자 ID, 닉네임, 프로필 이미지)을 그려주는 Layout.
 *
 * 1. 프로필을 노출할 layout에 [com.kakao.sdk.sample.common.widget.ProfileLayout]을 선언한다.
 *
 * 2. [com.kakao.sdk.sample.common.widget.ProfileLayout.setMeResponseCallback]를 이용하여 사용자정보 요청 결과에 따른 callback을 설정한다.
 *
 * @author MJ
 */
class ProfileLayout : FrameLayout {
    private var meResponseCallback: MeResponseCallback? = null

    private var email: String? = null
    private var nickname: String? = null
    private var userId: String? = null
    private var birthDay: String? = null
    private var countryIso: String? = null
//    private var profile: NetworkImageView? = null
//    private var background: NetworkImageView? = null
    private var profileDescription: TextView? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    /**
     * 사용자정보 요청 결과에 따른 callback을 설정한다.
     * @param callback 사용자정보 요청 결과에 따른 callback
     */
    fun setMeResponseCallback(callback: MeResponseCallback) {
        this.meResponseCallback = callback
    }

    /**
     * param으로 온 UserProfile에 대해 view를 update한다.
     * @param userProfile 화면에 반영할 사용자 정보
     */
    fun setUserProfile(userProfile: UserProfile) {
        setEmail(userProfile.email)
        setProfileURL(userProfile.profileImagePath)
        setNickname(userProfile.nickname)
        setUserId(userProfile.id.toString())
    }

    fun setEmail(email: String) {
        this.email = email
        updateLayout()
    }

    /**
     * 프로필 이미지에 대해 view를 update한다.
     * @param profileImageURL 화면에 반영할 프로필 이미지
     */
    fun setProfileURL(profileImageURL: String?) {
//        if (profile != null && profileImageURL != null) {
//            val app = GlobalApplication.globalApplicationContext ?: throw UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader")
//            profile!!.setImageUrl(profileImageURL, (app as GlobalApplication).getImageLoader())
//        }
    }

    fun setBgImageURL(bgImageURL: String?) {
//        if (bgImageURL != null) {
//            val app = GlobalApplication.globalApplicationContext ?: throw UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader")
//            background!!.setImageUrl(bgImageURL, (app as GlobalApplication).getImageLoader())
//        }
    }

    fun setDefaultBgImage(imageResId: Int) {
//        if (background != null) {
//            background!!.setBackgroundResource(imageResId)
//        }
    }

    fun setDefaultProfileImage(imageResId: Int) {
//        if (profile != null) {
//            profile!!.setBackgroundResource(imageResId)
//        }
    }

    fun setCountryIso(countryIso: String) {
        this.countryIso = countryIso
        updateLayout()
    }

    /**
     * 별명 view를 update한다.
     * @param nickname 화면에 반영할 별명
     */
    fun setNickname(nickname: String) {
        this.nickname = nickname
        updateLayout()
    }

    fun setBirthDay(birthDay: String) {
        this.birthDay = birthDay
        updateLayout()
    }

    /**
     * 사용자 아이디 view를 update한다.
     * @param userId 화면에 반영할 사용자 아이디
     */
    fun setUserId(userId: String) {
        this.userId = userId
        updateLayout()
    }

    private fun updateLayout() {
        val builder = StringBuilder()

        if (!TextUtils.isEmpty(email)) {
            builder.append(resources.getString(R.string.com_kakao_profile_email)).append('\n').append(email).append('\n')
        }
        if (nickname != null && nickname!!.length > 0) {
            builder.append(resources.getString(R.string.com_kakao_profile_nickname)).append("\n").append(nickname).append("\n")
        }

        if (userId != null && userId!!.length > 0) {
            builder.append(resources.getString(R.string.com_kakao_profile_userId)).append("\n").append(userId).append("\n")
        }

        if (birthDay != null && birthDay!!.length > 0) {
            builder.append(resources.getString(R.string.com_kakao_profile_userId)).append("\n").append(birthDay)
        }

        if (countryIso != null) {
            builder.append(resources.getString(R.string.kakaotalk_country_label)).append("\n").append(countryIso)
        }

        if (profileDescription != null) {
            profileDescription!!.text = builder.toString()
        }
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.layout_common_kakao_profile, this)

//        profile = view.findViewById(R.id.com_kakao_profile_image) as NetworkImageView
//        background = view.findViewById(R.id.background) as NetworkImageView
        profileDescription = view.findViewById(R.id.profile_description) as TextView
    }

    /**
     * 사용자 정보를 요청한다.
     */
    fun requestMe() {
        UserManagement.requestMe(meResponseCallback)
    }
}
