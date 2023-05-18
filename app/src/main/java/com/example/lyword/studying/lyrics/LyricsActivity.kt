package com.example.lyword.studying.lyrics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lyword.databinding.ActivityLyricsBinding
import com.google.android.material.tabs.TabLayoutMediator

class LyricsActivity : AppCompatActivity() {

    lateinit var binding: ActivityLyricsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Viewpager 세팅
        val lyricsAdapter = LyricsViewpagerAdapter(this)
        val menu = arrayListOf("Study", "Quiz")
        binding.lyricsContentVp.adapter = lyricsAdapter
        TabLayoutMediator(binding.lyricsMenuTb, binding.lyricsContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        // StudyingFragment에서 보낸 데이터 받기 -> 앨범 정보 세팅
        if(intent.hasExtra("title")){
            binding.lyricsSongTv.text = intent.getStringExtra("title")
        }
        if(intent.hasExtra("singer")){
            binding.lyricsSingerTv.text = intent.getStringExtra("singer")
        }

        binding.lyricsBackBtn.setOnClickListener {
            finish()
        }
    }
}