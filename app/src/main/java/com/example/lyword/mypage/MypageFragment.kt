package com.example.lyword.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lyword.databinding.FragmentMypageBinding
import com.example.lyword.studying.lyrics.LyricsViewpagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        // Viewpager 세팅
        val mypageAdapter = MypageViewpagerAdapter(this)
        val menu = arrayListOf("Ongoing", "Completion")
        binding.mypageContentVp.adapter = mypageAdapter
        TabLayoutMediator(binding.mypageMenuTb, binding.mypageContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        return binding.root
    }


}