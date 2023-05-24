package com.example.lyword.kakao

import android.app.Application
import com.example.lyword.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        KakaoSdk.init(this, getString(R.string.kakao_native_key))
    }
}