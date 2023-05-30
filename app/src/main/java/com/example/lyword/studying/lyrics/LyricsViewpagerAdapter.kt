package com.example.lyword.studying.lyrics

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LyricsViewpagerAdapter(fragment: LyricsActivity, private val studyId: Long) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LyricsStudyFragment.newInstance(studyId)
            else -> LyricsQuizFragment.newInstance(studyId)
        }
    }
}