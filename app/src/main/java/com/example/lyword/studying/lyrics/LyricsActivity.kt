package com.example.lyword.studying.lyrics

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.BuildConfig
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ActivityLyricsBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class LyricsActivity : AppCompatActivity() {

    lateinit var binding: ActivityLyricsBinding
    lateinit var db : LywordDatabase
    private lateinit var youTubePlayerView : YouTubePlayerView

    private var studyId : Long = 0
    private var studyMusic : StudyEntity = StudyEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LywordDatabase.getInstance(this)
        intent?.let {
            studyId = it.getLongExtra("studyId", 0)
        }
        setMusic()

        youTubePlayerView = binding.lyricsYoutubePlayerView
        setYoutubeListener()

        // Viewpager 세팅
        val lyricsAdapter = LyricsViewpagerAdapter(this)
        val menu = arrayListOf("Study", "Quiz")
        binding.lyricsContentVp.adapter = lyricsAdapter
        TabLayoutMediator(binding.lyricsMenuTb, binding.lyricsContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        binding.lyricsBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun setYoutubeListener() {
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                val videoId = studyMusic.videoId
                youTubePlayer.loadVideo(videoId, 0f)
            }

//            override fun onStateChange(
//                youTubePlayer: YouTubePlayer,
//                state: PlayerConstants.PlayerState
//            ) {
//                super.onStateChange(youTubePlayer, state)
//                //플레이어의 상태 변화 이벤트 처리
//            }
        })
    }

    private fun setMusic() {
        getMusicFromDB()

        binding.lyricsTitleTv.text = studyMusic.title
        binding.lyricsSingerTv.text = studyMusic.artist

        Glide.with(this)
            .load(studyMusic.album_art)
            .placeholder(R.drawable.ic_logo_splash)
            .error(R.drawable.ic_logo_splash)
            .fallback(R.drawable.ic_logo_splash)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.lyricsAlbumIv)
    }

    private fun getMusicFromDB() {
        val thread : Thread = Thread {
            studyMusic = db.studyDao.getStudyById(studyId)
        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }


}