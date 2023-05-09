package com.example.lyword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lyword.databinding.FragmentStudyingBinding

class StudyingFragment : Fragment() {
    lateinit var binding : FragmentStudyingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyingBinding.inflate(inflater, container, false)

        return binding.root
    }
}