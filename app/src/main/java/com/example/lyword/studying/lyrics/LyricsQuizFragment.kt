package com.example.lyword.studying.lyrics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lyword.databinding.FragmentStudyQuizBinding

class LyricsQuizFragment  : Fragment() {
    lateinit var binding: FragmentStudyQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyQuizBinding.inflate(inflater, container, false)

        return binding.root
    }
}