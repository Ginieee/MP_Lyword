package com.example.lyword.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.ActivityPopularBinding
import org.jsoup.Jsoup
import java.io.IOException

class PopularActivity : AppCompatActivity() {

    private val RESULT_CODE_SUCCESS = 100

    lateinit var binding : ActivityPopularBinding

    private val popularMusicAdapter = HomePopularRVAdapter()
    private var popularMusic : ArrayList<PopularMusic> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickListener()
    }

    override fun onResume() {
        super.onResume()
        getPopularChart()

        binding.popularRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.popularRv.adapter = popularMusicAdapter
        popularMusicAdapter.addPopular(popularMusic)
    }

    private fun clickListener() {
        binding.popularBackBtn.setOnClickListener {
            finish()
        }

        popularMusicAdapter.setMyItemClickListener(object : HomePopularRVAdapter.PopularItemClickListener {
            override fun onPopularClicked(item: PopularMusic) {

                val intent = Intent(this@PopularActivity, PopularMusicDialog::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("artist", item.artist)
                intent.putExtra("albumCover", item.album_art)
                popularDialogResultLauncher.launch(intent)
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
}