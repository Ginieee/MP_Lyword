package com.example.lyword.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lyword.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            finish()
        }
    }
}