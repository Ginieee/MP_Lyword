package com.example.lyword

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LyricsViewpagerAdapter(fragment: LyricsActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LyricsStudyFragment()
            else -> LyricsQuizFragment()
        }
    }
}