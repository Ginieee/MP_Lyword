package com.example.lyword.studying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
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
        val songs = ArrayList<StudyEntity>()
        songs.add(StudyEntity(0, "나나", "울랄라", "", 0, listOf(), listOf() ))
        val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
        binding.studyRecordRv.adapter = rvAdapter
        binding.studyRecordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}