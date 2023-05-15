package com.example.lyword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.FragmentStudyingBinding

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

        val songs = ArrayList<Song>()
        songs.add(Song("one", "singer1", 33))
        songs.add(Song("two", "singer2", 33))
        songs.add(Song("three", "singer3", 33))
        songs.add(Song("four", "singer4", 33))
        songs.add(Song("five", "singer5", 33))

        val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}