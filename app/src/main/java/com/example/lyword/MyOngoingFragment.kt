package com.example.lyword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lyword.databinding.FragmentMyOngoingBinding

class MyOngoingFragment : Fragment() {
    lateinit var binding: FragmentMyOngoingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOngoingBinding.inflate(inflater, container, false)

        return binding.root
    }


}

