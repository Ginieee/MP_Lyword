package com.example.lyword

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lyword.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("HOME_FRG", "검색 액티비티 열림")
    }
}