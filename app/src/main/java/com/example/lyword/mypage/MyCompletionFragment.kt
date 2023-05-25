package com.example.lyword.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.entity.StudyEntity
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

        val studys = ArrayList<StudyEntity>()

        val rvAdapter = CompletionSongRVAdapter(studys, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}