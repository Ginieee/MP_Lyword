package com.example.lyword.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lyword.SplashActivity
import com.example.lyword.databinding.ActivitySettingBinding
import com.example.lyword.kakao.LoginActivity
import com.kakao.sdk.common.util.Utility

class SettingActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)

        binding.verIv.setOnClickListener {
            val intent = Intent(applicationContext, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.icBack.setOnClickListener {
            finish()
        }
    }
}