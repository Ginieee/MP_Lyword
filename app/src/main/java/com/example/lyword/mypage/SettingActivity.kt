package com.example.lyword.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lyword.SplashActivity
import com.example.lyword.databinding.ActivitySettingBinding
import com.example.lyword.kakao.LoginActivity
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class SettingActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)

        binding.verIv.setOnClickListener {
            val intent = Intent(applicationContext, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logoutBt.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d("카카오","카카오 로그아웃 실패")
                }else {
                    Log.d("카카오","카카오 로그아웃 성공!")
                }
            }
        }

        binding.icBack.setOnClickListener {
            finish()
        }
    }
}