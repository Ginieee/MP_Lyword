package com.example.lyword.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.R
import com.example.lyword.Song
import com.example.lyword.databinding.FragmentMyCompletionBinding

class MyCompletionFragment : Fragment() {
    lateinit var binding: FragmentMyCompletionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCompletionBinding.inflate(inflater, container, false)
        initRV()
        return binding.root
    }

    private fun initRV(){

        val songs = ArrayList<Song>()
        songs.add(Song("one", "singer1", 100))
        songs.add(Song("two", "singer2", 100))
        songs.add(Song("three", "singer3", 100))
        songs.add(Song("four", "singer4", 100))
        songs.add(Song("five", "singer5", 100))
        songs.add(Song("six", "singer6", 100))

        val rvAdapter = CompletionSongRVAdapter(songs, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}