package com.example.lyword.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lyword.R
import com.example.lyword.databinding.FragmentTodayBinding
import com.example.lyword.studying.StudyingFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.MainActivity
import com.example.lyword.Song
import com.example.lyword.studying.StudyingSongRVAdapter

class TodayFragment : Fragment() {
    lateinit var binding : FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(inflater, container, false)


        binding.startToLearn.setOnClickListener {
            val studyingFragment = StudyingFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.today_frm, studyingFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            (requireActivity() as MainActivity).binding.mainBottomNavi.selectedItemId = R.id.studyingFragment
        }


        val todayWords = ArrayList<TodayWord>()

        todayWords.add(TodayWord("Flower", "꽃"))
        todayWords.add(TodayWord("Scent", "향기"))
        todayWords.add(TodayWord("Only", "오직"))



        binding.todayWordRv.adapter = TodayWordRVAdapter(todayWords)
        binding.todayWordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)



        return binding.root
    }
}