package com.example.lyword.studying

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.FragmentStudyingBinding
import org.json.JSONObject
import org.snu.ids.kkma.ma.MExpression
import org.snu.ids.kkma.ma.MorphemeAnalyzer
import org.snu.ids.kkma.ma.Sentence
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class StudyingFragment : Fragment() {
    lateinit var binding : FragmentStudyingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyingBinding.inflate(inflater, container, false)

        initRV()

        return binding.root
    }



    private fun initRV(){

        val songs = ArrayList<Study>()
        songs.add(Study("one", "singer1", 33, 0, 0))
        songs.add(Study("two", "singer2", 33, 0, 0))
        songs.add(Study("three", "singer1", 33, 0, 0))
        songs.add(Study("four", "singer2", 33, 0, 0))
        songs.add(Study("five", "singer1", 33, 0, 0))
        songs.add(Study("six", "singer2", 33, 0, 0))

        val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}