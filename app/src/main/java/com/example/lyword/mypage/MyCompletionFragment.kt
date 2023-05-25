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
import com.example.lyword.studying.Study

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

        val studys = ArrayList<Study>()
        studys.add(Study("one", "singer1", 100))
        studys.add(Study("two", "singer2", 100))
        studys.add(Study("three", "singer3", 100))
        studys.add(Study("four", "singer4", 100))
        studys.add(Study("five", "singer5", 100))
        studys.add(Study("six", "singer6", 100))

        val rvAdapter = CompletionSongRVAdapter(studys, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}