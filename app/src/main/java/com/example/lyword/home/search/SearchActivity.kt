package com.example.lyword.home.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ActivitySearchBinding
import com.example.lyword.studying.lyrics.separate.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URLEncoder

class SearchActivity : AppCompatActivity(), ITunesView, SeparateView {

    lateinit var binding : ActivitySearchBinding

    private val searchResultAdapter = SearchRVAdapter()
    private val results : ArrayList<ITunesResult> = arrayListOf()

    private var search : String = ""
    private lateinit var imm : InputMethodManager

    lateinit var db : LywordDatabase
    private var selectedStudy : StudyEntity = StudyEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchSelectTv.visibility = View.GONE

        db = LywordDatabase.getInstance(this)

        readyToSearch()

        Log.d("HOME_FRG", "검색 액티비티 열림")

        setAdapter()
        clickListener()
    }

    private fun readyToSearch() {
        binding.searchHeaderEt.requestFocus()

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchHeaderEt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setAdapter() {
        binding.searchResultRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchResultRv.adapter = searchResultAdapter
        searchResultAdapter.addResults(results)
    }

    private fun clickListener() {
        binding.searchHeaderEt.setOnEditorActionListener { _, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE ) {
                val text = binding.searchHeaderEt.text.split(" ")
                search = text.joinToString("+")
                Log.d("SEARCH_ACT", "search : $search")
                getSearchResult(search)

                imm.hideSoftInputFromWindow(binding.searchHeaderEt.windowToken, 0)

                true
            } else {
                false
            }
        }

        searchResultAdapter.setMyItemClickListener(object : SearchRVAdapter.SearchItemClickListener {
            override fun onSearchClicked(item: ITunesResult) {
                Log.d("GET_LYRICS", "Search Item Clicked")
                selectedStudy.title = item.trackName
                selectedStudy.artist = item.artistName
                selectedStudy.album_art = item.artworkUrl100

                getLyrics(item.trackName, item.artistName)
            }

        })

        binding.searchSelectTv.setOnClickListener {
            var addStudyThread : Thread = Thread {
                val studyIdx = db.studyDao.insertStudy(selectedStudy)
                Log.d("ADD_STUDY", "study idx : $studyIdx")
            }
            addStudyThread.start()

            try {
                addStudyThread.join()
            } catch (e : InterruptedException) {
                e.printStackTrace()
            }

            binding.searchSelectTv.visibility = View.GONE

            finish()
        }
    }

    private fun getSearchResult(search : String) {
        val iTunesService = ITunesService()
        iTunesService.setITunesView(this)

        iTunesService.getSearchResult(search)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchITunesSuccess(count: Int, result: List<ITunesResult>?) {
        if (count == 100) {
            Toast.makeText(this, "검색에 오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
        else if (count > 0) {
            Log.d("SEARCH_ACT", result.toString())
            results.clear()
            results.addAll(result!!)
            results.sortBy { it.releaseDate }
            results.reverse()
            searchResultAdapter.addResults(results)
            searchResultAdapter.notifyDataSetChanged()
        } else {
            results.clear()
            searchResultAdapter.notifyDataSetChanged()
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLyrics(title : String, artist : String) {

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
        selectedStudy.sentenceList = selectedSentenceList

        binding.searchSelectTv.visibility = View.VISIBLE

    }

    override fun onGetLyricsSuccess(result: ArrayList<MorpResult>, index: Int) {
        Log.d("SEARCH_ACT", "onGetLyricsSuccess")
    }


}