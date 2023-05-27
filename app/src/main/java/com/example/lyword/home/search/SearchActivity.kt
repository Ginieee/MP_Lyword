package com.example.lyword.home.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ActivitySearchBinding
import com.example.lyword.home.PopularMusicDialog

class SearchActivity : AppCompatActivity(), ITunesView {

    lateinit var binding : ActivitySearchBinding

    private val searchResultAdapter = SearchRVAdapter()
    private val results : ArrayList<ITunesResult> = arrayListOf()

    private var search : String = ""
    private lateinit var imm : InputMethodManager

    lateinit var db : LywordDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                val intent = Intent(this@SearchActivity, PopularMusicDialog::class.java)
                intent.putExtra("title", item.trackName)
                intent.putExtra("artist", item.artistName)
                intent.putExtra("albumCover", item.artworkUrl100)
                popularDialogResultLauncher.launch(intent)
            }
        })
    }

    private fun getSearchResult(search : String) {
        binding.searchLoadingLv.visibility = View.VISIBLE
        binding.searchResultRv.visibility = View.GONE

        val iTunesService = ITunesService()
        iTunesService.setITunesView(this)

        iTunesService.getSearchResult(search)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchITunesSuccess(count: Int, result: List<ITunesResult>?) {
        binding.searchLoadingLv.visibility = View.GONE
        binding.searchResultRv.visibility = View.VISIBLE

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

    private val popularDialogResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}