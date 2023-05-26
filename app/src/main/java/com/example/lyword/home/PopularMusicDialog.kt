package com.example.lyword.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.DialogPopularMusicBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogPopularMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            var studying = StudyEntity()
            studying.title = title
            studying.artist = artist
            studying.album_art = albumCover
            studying.sentenceList = getLyrics(title, artist)

            var addStudyThread = Thread {
                Log.d("ADD_STUDY", studying.toString())
                val studyIdx = db.studyDao.insertStudy(studying)
                Log.d("ADD_STUDY", "study idx : $studyIdx")
            }
            addStudyThread.start()

            try {
                addStudyThread.join()
            } catch (e : InterruptedException) {
                e.printStackTrace()
            }

            finish()
        }
    }

    private fun getLyrics(title : String, artist : String) : List<SentenceEntity> {

        var input = URLEncoder.encode(artist, "UTF-8") + "+" + URLEncoder.encode(title, "UTF-8")
        Log.d("SEARCH_ACT_GET_LYRICS", input)
        var url = "https://search.naver.com/search.naver?ie=UTF-8&sm=whl_hty&query=$input"
        Log.d("SEARCH_ACT_GET_LYRICS", url)

        var sentenceList = ArrayList<String>().toMutableList()
        val selectedSentenceList = ArrayList<SentenceEntity>()
        val thread = object : Thread() {
            override fun run() {
                try {
                    var doc : org.jsoup.nodes.Document? = Jsoup.connect(url).get()
                    var lyrics : Elements? = doc?.select(".text_expand")
                    var isEmpty = lyrics?.isEmpty()

                    if (isEmpty == false) {
                        var content = lyrics?.get(0)?.outerHtml()!!.split("<",">").toMutableList()
                        content = content.subList(4, content.size - 8)
                        content.removeAll{ it == "br"}
                        sentenceList = content

                        Log.d("SEARCH_ACT_GET_LYRICS", content.toString())
                    } else {
                        Log.d("SEARCH_ACT_GET_LYRICS","lyrics is empty")
                    }
                } catch (e : IOException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }

        for (i in sentenceList) {
            var input = SentenceEntity()
            input.sentenceOrigin = i
            selectedSentenceList.add(input)
        }

        return selectedSentenceList
    }
}