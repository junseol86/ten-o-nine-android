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
import android.widget.Button
import com.hyeonmin.kface.tenonine.R

import com.kakao.auth.Session

/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.

 * @author MJ
 */
class RootLoginActivity : BaseActivity(), View.OnClickListener {

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.

     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            setContentView(R.layout.layout_common_kakao_login)

            val activityButton = findViewById(R.id.button_login_with_activity) as Button
            val fragmentButton = findViewById(R.id.button_login_with_fragment) as Button
            activityButton.setOnClickListener(this)
            fragmentButton.setOnClickListener(this)
        } else {
            redirectSignupActivity()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_login_with_activity -> startActivity(Intent(this@RootLoginActivity, SampleLoginActivity::class.java))
//            R.id.button_login_with_fragment -> startActivity(Intent(this@RootLoginActivity, LoginFragmentActivity::class.java))
            else -> {
            }
        }
    }
}
