package com.example.lyword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lyword.databinding.ActivityMainBinding
import com.example.lyword.home.HomeFragment
import com.example.lyword.mypage.MypageFragment
import com.example.lyword.studying.StudyingFragment
import com.example.lyword.today.TodayFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> { //홈 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.studyingFragment -> { //친구목록 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, StudyingFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.todayFragment -> { //그룹목록 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, TodayFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> { //커스텀 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}