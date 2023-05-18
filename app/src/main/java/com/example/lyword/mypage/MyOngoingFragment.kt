package com.example.lyword.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.Song
import com.example.lyword.databinding.FragmentMyOngoingBinding
import com.example.lyword.studying.StudyingSongRVAdapter


class MyOngoingFragment : Fragment() {
    lateinit var binding: FragmentMyOngoingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOngoingBinding.inflate(inflater, container, false)

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
        songs.add(Song("six", "singer6", 33))

        val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}