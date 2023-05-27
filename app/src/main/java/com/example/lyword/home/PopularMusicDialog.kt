package com.example.lyword.home

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.BuildConfig
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.DialogPopularMusicBinding
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URLEncoder

class PopularMusicDialog : AppCompatActivity() {

    lateinit var binding : DialogPopularMusicBinding
    lateinit var db : LywordDatabase

    private lateinit var title : String
    private lateinit var artist : String
    private lateinit var albumCover : String
    private lateinit var previewUrl : String

    private val YOUTUBE_KEY = BuildConfig.YOUTUBE_KEY
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogPopularMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.popularStartBtn.text = "Start to Learn"
        binding.popularLoadingLv.visibility = View.GONE

        db = LywordDatabase.getInstance(this)

        intent?.let {
            title = it.getStringExtra("title") ?: ""
            artist = it.getStringExtra("artist") ?: ""
            albumCover = it.getStringExtra("albumCover") ?: ""
            previewUrl = it.getStringExtra("previewUrl") ?: ""
        }

        setMusic()
        clickListener()
    }

    private fun setMusic() {
        binding.popularContentTitleTv.text = title
        binding.popularContentArtistTv.text = artist

        Glide.with(this)
            .load(albumCover)
            .placeholder(R.drawable.ic_logo_splash)
            .error(R.drawable.ic_logo_splash)
            .fallback(R.drawable.ic_logo_splash)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.popularContentAlbumIv)

        if (previewUrl.isNotBlank()) {
            setMediaPlayer()
        } else {
            Toast.makeText(this, "재생할 음원 파일이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            setDataSource(previewUrl)
            prepare()
//            setVolume(0f,0f)

            isLooping = true
            start()

//            val fadeInDuration = 1000
//            val maxVolume = 1.0f
//
//            val fadeInAnimator = ValueAnimator.ofFloat(0f, maxVolume).apply {
//                duration = fadeInDuration.toLong()
//                addUpdateListener { animation ->
//                    val volume = animation.animatedValue as Float
//                    setVolume(volume, volume)
//                }
//            }
//
//            fadeInAnimator.start()
        }

        Log.d("MEDIA_PLAYER", "url : $previewUrl")
    }

    private fun clickListener() {
        binding.popularContentCloseBtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
        }

        binding.popularStartBtn.setOnClickListener {
            binding.popularStartBtn.text = ""
            binding.popularLoadingLv.visibility = View.VISIBLE

            var studying = StudyEntity()
            studying.title = title
            studying.artist = artist
            studying.album_art = albumCover

            CoroutineScope(Dispatchers.Main).launch {
                studying.sentenceList = withContext(Dispatchers.IO) {
                    getLyrics(title, artist)
                }
                Log.d("SENTENCE_ADD", studying.sentenceList.toString())
                studying.videoId = withContext(Dispatchers.IO) {
                    searchVideos("$title $artist Official")
                }

                val thread = Thread {
                    val sentenceIds = db.sentenceDao.insertSentenceList(studying.sentenceList!!)
                    studying.sentenceList = db.sentenceDao.getSentencesById(sentenceIds)

                    Log.d("SENTENCE_ADD", sentenceIds.toString())
                    Log.d("SENTENCE_ADD", studying.sentenceList.toString())
                }
                thread.start()
                thread.join()

                var addStudyThread = Thread {
                    val studyIdx = db.studyDao.insertStudy(studying)

                    Log.d("ADD_STUDY", "study idx : $studyIdx")

                    runOnUiThread {
                        val resultIntent = Intent()
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }

                addStudyThread.start()
            }
        }
    }


    private suspend fun getLyrics(title: String, artist: String): List<SentenceEntity> = withContext(Dispatchers.IO) {
        val input = URLEncoder.encode(artist, "UTF-8") + "+" + URLEncoder.encode(title, "UTF-8")
        Log.d("SEARCH_ACT_GET_LYRICS", input)
        val url = "https://search.naver.com/search.naver?ie=UTF-8&sm=whl_hty&query=$input"
        Log.d("SEARCH_ACT_GET_LYRICS", url)

        var sentenceList = emptyList<String>()
        val selectedSentenceList = ArrayList<SentenceEntity>()

        try {
            val doc: org.jsoup.nodes.Document? = Jsoup.connect(url).get()
            val lyrics: Elements? = doc?.select(".text_expand")
            val isEmpty = lyrics?.isEmpty()

            if (isEmpty == false) {
                var content = lyrics?.get(0)?.outerHtml()!!.split("<", ">").toMutableList()
                content = content.subList(4, content.size - 8)
                content.removeAll { it == "br" }
                sentenceList = content

                Log.d("SEARCH_ACT_GET_LYRICS", content.toString())
            } else {
                Log.d("SEARCH_ACT_GET_LYRICS", "lyrics is empty")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        for (i in sentenceList) {
            val input = SentenceEntity()
            input.sentenceOrigin = i
            selectedSentenceList.add(input)
        }

        selectedSentenceList
    }


    private suspend fun searchVideos(query: String): String = withContext(Dispatchers.IO) {
        val transport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val youtube = YouTube.Builder(transport, JSON_FACTORY, null)
            .setApplicationName("YoutubeSearch")
            .build()

        val searchRequest = youtube.search().list("id")
        searchRequest.key = YOUTUBE_KEY
        searchRequest.q = query
        searchRequest.type = "video"
        searchRequest.maxResults = 1
        Log.d("YOUTUBE_SEARCH", searchRequest.toString())

        var result: List<SearchResult> = emptyList()

        try {
            val searchResponse: SearchListResponse = searchRequest.execute()
            val searchResult: List<SearchResult> = searchResponse.items ?: emptyList()
            result = searchResult
        } catch (e: Exception) {
            e.printStackTrace()
        }

        result[0].id.videoId
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("POPULAR_DIALOG", "onDestroy")
    }
}