package com.example.lyword.studying.lyrics

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.lyword.databinding.DialogLyricsWordBinding
import com.example.lyword.studying.lyrics.word.WordDao
import com.example.lyword.data.WordDatabase
import com.example.lyword.studying.lyrics.word.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LyricsWordDialog : AppCompatActivity() {

    lateinit var binding: DialogLyricsWordBinding
    lateinit var wordDao: WordDao
    lateinit var wordAdapter: LyricsWordViewPagerAdapter

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
        val wordDatabase = Room.databaseBuilder(
            applicationContext,
            WordDatabase::class.java,
            "word-database"
        ).build()

        wordDao = wordDatabase.wordDao()

        lifecycleScope.launch {
            val wordList = getWordListFromDatabase()
            wordAdapter = LyricsWordViewPagerAdapter(wordList)
            binding.dialogLyricsVp.adapter = wordAdapter
            binding.dialogLyricsVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

    }

    private suspend fun getWordListFromDatabase(): List<WordEntity> = withContext(Dispatchers.IO) {
        wordDao.getWord()
    }
}