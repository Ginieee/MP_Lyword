package com.example.lyword.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.ActivityPopularBinding
import com.example.lyword.home.search.ITunesResult
import com.example.lyword.home.search.ITunesService
import com.example.lyword.home.search.ITunesView
import org.jsoup.Jsoup
import java.io.IOException

class PopularActivity : AppCompatActivity() {

    private val RESULT_CODE_SUCCESS = 100

    lateinit var binding : ActivityPopularBinding

    private val popularMusicAdapter = HomePopularRVAdapter()
    private var popularMusic : ArrayList<PopularMusic> = arrayListOf()

    private var title : String = ""
    private var artist : String = ""
    private var albumCover : String = ""
    private var previewUrl : String = ""

    val regex_br = "\\([^()]*\\)".toRegex()
    val regex_sp = "\\s{2,}".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPopularChart()

        binding.popularRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.popularRv.adapter = popularMusicAdapter
        popularMusicAdapter.addPopular(popularMusic)

        clickListener()
    }

    private fun clickListener() {
        binding.popularBackBtn.setOnClickListener {
            finish()
        }

        popularMusicAdapter.setMyItemClickListener(object : HomePopularRVAdapter.PopularItemClickListener {
            override fun onPopularClicked(item: PopularMusic) {
//                binding.popularLoadingLv.visibility = View.VISIBLE

                title = item.title
                artist = item.artist
                albumCover = item.album_art

                val intent = Intent(this@PopularActivity, PopularMusicDialog::class.java)
                intent.putExtra("title", title)
                intent.putExtra("artist", artist)
                intent.putExtra("albumCover", albumCover)
                intent.putExtra("previewUrl", "")
                popularDialogResultLauncher.launch(intent)
//
//                var search = (item.title + "+" + item.artist)
//                search = search.replace(regex_br, "").trim()
//                getSearchResult(search)
//                Log.e("ITUNES_SEARCH", search)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPopularChart() {
        var url = "https://www.melon.com/chart/"
        popularMusic.clear()

        val thread = object : Thread() {
            override fun run() {
                try {
                    val doc : org.jsoup.nodes.Document? = Jsoup.connect(url).get()
                    val elements = doc?.select("tr.lst50")

                    if (elements?.isNotEmpty() == true) {
                        for (element in elements) {
                            val rank = element.select("span.rank").text()
                            val title = element.select("div.ellipsis.rank01 span a").text()
                            val artist = element.select("div.ellipsis.rank02 span.checkEllipsis a").text()
                            val albumCover = element.select("a.image_typeAll img").attr("src")
                            Log.d("GET_POPULAR", "순위 : $rank, 제목 : $title, 가수 : $artist, 커버 : $albumCover")

                            popularMusic.add(
                                PopularMusic(
                                    rank.toInt(),
                                    title,
                                    artist,
                                    albumCover
                                )
                            )
                        }
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

        popularMusicAdapter.addPopular(popularMusic)
        popularMusicAdapter.notifyDataSetChanged()
    }

    private val popularDialogResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

//    private fun getSearchResult(search : String) {
//        val iTunesService = ITunesService()
//        iTunesService.setITunesView(this)
//
//        iTunesService.getSearchResult(search)
//    }
//
//    override fun onSearchITunesSuccess(count: Int, result: List<ITunesResult>?) {
//        if (count == 100) {
//            Toast.makeText(this, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
//        }
//        else if (count > 0) {
//            Log.d("SEARCH_ACT", result.toString())
//            if (result?.isNotEmpty() == true) {
//                Log.d("ITUNES_SEARCH", result.toString())
//                previewUrl = result[0].previewUrl
//            }
//        } else {
//            previewUrl = ""
//        }
//
//        val intent = Intent(this, PopularMusicDialog::class.java)
//        intent.putExtra("title", title)
//        intent.putExtra("artist", artist)
//        intent.putExtra("albumCover", albumCover)
//        intent.putExtra("previewUrl", previewUrl)
//        popularDialogResultLauncher.launch(intent)
//
//        binding.popularLoadingLv.visibility = View.GONE
//    }
}