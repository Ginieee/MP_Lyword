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
import com.example.lyword.studying.StudyingSongRVAdapter
import java.util.Random


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
            parentFragmentManager.beginTransaction()
                .replace(R.id.today_frm, studyingFragment)
                .addToBackStack(null)
                .commit()
            (requireActivity() as MainActivity).binding.mainBottomNavi.selectedItemId = R.id.studyingFragment
        }


        val todayWords = ArrayList<TodayWord>()


        todayWords.add(TodayWord("Flower", "꽃"))
        todayWords.add(TodayWord("Scent", "향기"))
        todayWords.add(TodayWord("Only", "오직"))
        todayWords.add(TodayWord("Kind", "착한"))
        todayWords.add(TodayWord("Change", "변하다"))
        // 5개
        todayWords.add(TodayWord("Perhaps", "어쩌면"))
        todayWords.add(TodayWord("Also", "또한"))
        todayWords.add(TodayWord("Time", "시간"))
        todayWords.add(TodayWord("Red", "붉다"))
        todayWords.add(TodayWord("Burn", "타다"))
        // 10개
        todayWords.add(TodayWord("Blue", "파란"))
        todayWords.add(TodayWord("Butterfly", "나비"))
        todayWords.add(TodayWord("Fly", "날다"))
        todayWords.add(TodayWord("Catch", "잡다"))
        todayWords.add(TodayWord("Portion", "몫"))
        // 15개
        todayWords.add(TodayWord("Now", "이제"))
        todayWords.add(TodayWord("Cloud", "구름"))
        todayWords.add(TodayWord("You", "너"))
        todayWords.add(TodayWord("Me", "나"))
        todayWords.add(TodayWord("Nice", "괜찮은"))
        // 20개
        todayWords.add(TodayWord("Pretty", "예쁜"))
        todayWords.add(TodayWord("Day", "날"))
        todayWords.add(TodayWord("Leave", "남기다"))
        todayWords.add(TodayWord("Crazy", "미친"))
        todayWords.add(TodayWord("Horrible", "처참하다"))
        // 25개
        todayWords.add(TodayWord("Trample", "짓밟다"))
        todayWords.add(TodayWord("Lilac", "라일락"))
        todayWords.add(TodayWord("White", "하얀"))
        todayWords.add(TodayWord("Petal", "꽃잎"))
        todayWords.add(TodayWord("Wind", "바람"))
        // 30개
        todayWords.add(TodayWord("Lead", "이끌다"))
        todayWords.add(TodayWord("Like", "처럼"))
        todayWords.add(TodayWord("Spring", "봄"))
        todayWords.add(TodayWord("Come", "오다"))
        todayWords.add(TodayWord("Wind", "바람"))
        // 35개
        todayWords.add(TodayWord("Hello", "안녕"))
        todayWords.add(TodayWord("Back", "뒤"))
        todayWords.add(TodayWord("Never", "절대"))
        todayWords.add(TodayWord("See", "보다"))
        todayWords.add(TodayWord("Name", "이름"))
        // 45개
        todayWords.add(TodayWord("Rain", "비"))
        todayWords.add(TodayWord("Apart", "떨어져"))

        val random = Random()
        val tmpWords = ArrayList<TodayWord>()

        var count: Int = 0

        do{
            var num=random.nextInt(todayWords.size)
            if(count==0) {
                count++
                tmpWords.add(todayWords[num])
            }
            else if(!tmpWords.contains(todayWords[num])){
                count++
                tmpWords.add(todayWords[num])

            }

        }while(count<=2)

        binding.todayWordRv.adapter = TodayWordRVAdapter(tmpWords)
        binding.todayWordRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)



        return binding.root
    }
}