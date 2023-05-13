package com.example.lyword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lyword.databinding.ActivityLyricsBinding
import com.google.android.material.tabs.TabLayoutMediator

class LyricsActivity : AppCompatActivity() {

    lateinit var binding : ActivityLyricsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lyricsAdapter = LyricsViewpagerAdapter(this)
        val menu = arrayListOf("Study", "Quiz")
        binding.lyricsContentVp.adapter = lyricsAdapter
        TabLayoutMediator(binding.lyricsMenuTb, binding.lyricsContentVp){
                tab, position ->
            tab.text = menu[position]
        }.attach()

    }



}