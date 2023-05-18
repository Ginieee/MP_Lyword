package com.example.lyword.studying.lyrics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.FragmentStudyLyricsBinding

class LyricsStudyFragment  : Fragment() {
    lateinit var binding: FragmentStudyLyricsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLyricsBinding.inflate(inflater, container, false)

        initRV()

        return binding.root
    }

    private fun initRV(){

        val lyrics = ArrayList<Lyrics>()
        lyrics.add(Lyrics("one", "singer1", "33"))
        lyrics.add(Lyrics("two", "singer2", "33"))
        lyrics.add(Lyrics("three", "singer3", "33"))
        lyrics.add(Lyrics("four", "singer4", "33"))
        lyrics.add(Lyrics("five", "singer5", "33"))
        lyrics.add(Lyrics("one", "singer1", "33"))
        lyrics.add(Lyrics("two", "singer2", "33"))
        lyrics.add(Lyrics("three", "singer3", "33"))
        lyrics.add(Lyrics("four", "singer4", "33"))
        lyrics.add(Lyrics("five", "singer5", "33"))
        lyrics.add(Lyrics("one", "singer1", "33"))
        lyrics.add(Lyrics("two", "singer2", "33"))

        val rvAdapter = LyricsRVAdapter(lyrics, requireContext())
        binding.studyLyricsRv.adapter = rvAdapter
        binding.studyLyricsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}