package com.hyeonmin.kface.tenonine.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.activities.kakao.BaseActivity
import com.kakao.util.helper.Utility.getPackageInfo
import java.security.MessageDigest

class VersionCheckActivity : BaseActivity() {

    var versionCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_version_check)

        versionCode = packageManager.getPackageInfo("com.akadev.waitingmom", 0).versionCode

        var packageInfo = getPackageInfo(this, PackageManager.GET_SIGNATURES)
        for (signature in packageInfo.signatures)
            try {
                var md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
//                디버그 키 해시를 이걸로 알아내자
//                println(android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP))
            } catch (e: Exception) {
                println(e)
            }
        checkSession()
    }
}
