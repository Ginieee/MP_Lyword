package com.example.lyword.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MypageViewpagerAdapter (fragment: MypageFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyOngoingFragment()
            else -> MyCompletionFragment()
        }
    }
}