package com.example.lyword.studying.lyrics

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.lyword.studying.Word
import com.example.lyword.databinding.DialogLyricsWordBinding

class LyricsWordDialog : AppCompatActivity() {

    lateinit var binding: DialogLyricsWordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLyricsWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액티비티 크기 세팅하기
        // 화면 가로 크기 구하기
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        // 액티비티 크기 설정
        val layoutParams = window.attributes
        val wid = (screenWidth * 0.95).toInt()
        layoutParams.width = wid
        layoutParams.height = wid
        window.attributes = layoutParams

        // 다이얼로그 안의 Viewpager(+Recyclerview) 세팅 함수
        initVP()
    }

    private fun initVP(){

        // 임시 더미데이터
        val words = ArrayList<Word>()
        words.add(Word("one", "singer1", "33", 1))
        words.add(Word("one", "singer1", "33", 1))
        words.add(Word("one", "singer1", "33", 1))
        words.add(Word("one", "singer1", "33", 1))
        words.add(Word("one", "singer1", "33", 1))
        words.add(Word("one", "singer1", "33", 1))

        // 리사이클러뷰 세팅
        binding.dialogLyricsVp.adapter = LyricsWordViewPagerAdapter(words)
        binding.dialogLyricsVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}