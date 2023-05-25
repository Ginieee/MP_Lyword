package com.example.lyword.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.entity.StudyEntity
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

        val studys = ArrayList<StudyEntity>()

        val rvAdapter = StudyingSongRVAdapter(studys, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}