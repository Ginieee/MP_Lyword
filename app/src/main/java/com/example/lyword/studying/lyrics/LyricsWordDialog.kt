package com.example.lyword.studying.lyrics

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.lyword.data.LywordDatabase
import com.example.lyword.databinding.DialogLyricsWordBinding
import com.example.lyword.data.dao.WordDao
import com.example.lyword.data.entity.WordEntity
import kotlinx.coroutines.*

class LyricsWordDialog : AppCompatActivity() {

    lateinit var binding: DialogLyricsWordBinding
    lateinit var wordDao: WordDao
    lateinit var wordAdapter: LyricsWordViewPagerAdapter
    lateinit var db: LywordDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLyricsWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var lyricsPosition = intent.getIntExtra("lyricsPosition", -1)
        Log.d("lyricsWordDialog", lyricsPosition.toString())

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
        initVP(lyricsPosition)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initVP(lyricsPosition: Int){
        db = LywordDatabase.getInstance(this)

        GlobalScope.launch {
            val wordList = db.wordDao.getWordByLyricsId(lyricsPosition)
            val allWord = db.wordDao.getWord()
            val songList = db.studyDao.getStudyList()
            Log.d("lyricsWordDialog - s", songList.toString())
            Log.d("lyricsWordDialog - all", allWord.toString())
            Log.d("lyricsWordDialog - Idx", wordList.toString())
            wordAdapter = LyricsWordViewPagerAdapter(wordList)
            binding.dialogLyricsVp.adapter = wordAdapter
            binding.dialogLyricsVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

    }

    private suspend fun getWordListFromDatabase(): List<WordEntity> = withContext(Dispatchers.IO) {
        wordDao.getWord()
    }
}