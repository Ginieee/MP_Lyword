package com.example.lyword

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), ITunesView {

    lateinit var binding : ActivitySearchBinding

    private val searchResultAdapter = SearchRVAdapter()
    private val results : ArrayList<ITunesResult> = arrayListOf()

    private var search : String = ""
    private lateinit var imm : InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        binding.searchHeaderBtn.setOnClickListener {
            val text = binding.searchHeaderEt.text.split(" ")
            search = text.joinToString("+")
            Log.d("SEARCH_ACT", "search : $search")
            getSearchResult(search)

            imm.hideSoftInputFromWindow(binding.searchHeaderEt.windowToken, 0)
        }

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
            searchResultAdapter.addResults(results)
            searchResultAdapter.notifyDataSetChanged()
        } else {
            results.clear()
            searchResultAdapter.notifyDataSetChanged()
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}