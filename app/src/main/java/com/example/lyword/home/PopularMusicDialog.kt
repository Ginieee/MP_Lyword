package com.example.lyword.home

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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

    private val YOUTUBE_KEY = BuildConfig.YOUTUBE_KEY
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

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
    }

    private fun clickListener() {
        binding.popularContentCloseBtn.setOnClickListener {
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
                studying.videoId = withContext(Dispatchers.IO) {
                    searchVideos("$title $artist MV")
                }

                var addStudyThread = Thread {
                    Log.d("ADD_STUDY", studying.toString())
                    val studyIdx = db.studyDao.insertStudy(studying)

                    Log.d("ADD_STUDY", "study idx : $studyIdx")

                    runOnUiThread {
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

}