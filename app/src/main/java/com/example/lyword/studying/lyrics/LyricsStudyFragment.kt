package com.example.lyword.studying.lyrics

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Transaction
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.studying.lyrics.separate.*
import com.example.lyword.data.entity.WordEntity
import com.example.lyword.databinding.FragmentStudyLyricsBinding
import kotlinx.coroutines.*
import net.crizin.KoreanCharacter
import net.crizin.KoreanRomanizer
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LyricsStudyFragment  : Fragment() {
    lateinit var binding: FragmentStudyLyricsBinding
    lateinit var db: LywordDatabase

    // Get studyId
    companion object {
        fun newInstance(studyId: Long): LyricsStudyFragment {
            val fragment = LyricsStudyFragment()
            val args = Bundle()
            args.putLong("studyId", studyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLyricsBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

        // Store studyId from LyricsActivity(RecyclerView)
        val studyId = arguments?.getLong("studyId")

        if (studyId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                val study = db.studyDao.getStudyById(studyId)
                GlobalScope.launch(Dispatchers.Main) {
                    // Set RecyclerView
                    val rvAdapter = LyricsRVAdapter(study.sentenceList as ArrayList<SentenceEntity>, requireContext())
                    binding.studyLyricsRv.adapter = rvAdapter
                    binding.studyLyricsRv.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }

        return binding.root
    }
}