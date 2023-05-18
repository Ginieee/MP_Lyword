package com.example.lyword.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.R
import com.example.lyword.home.search.SearchActivity
import com.example.lyword.studying.Study
import com.example.lyword.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    private val dummyStudying : ArrayList<Study> = arrayListOf()
    private val dummyPopular : ArrayList<PopularMusic> = arrayListOf()

    private val recentStudyingAdapter = HomeStudyingRVAdapter()
    private val popularMusicAdapter = HomePopularRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        getDummy()
        setAdapter()
        clickListener()


        return binding.root
    }

    private fun setAdapter() {
        binding.homeRecentStudyContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeRecentStudyContentRv.adapter = recentStudyingAdapter
        recentStudyingAdapter.addStudying(dummyStudying)

        binding.homePopularNowContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.homePopularNowContentRv.adapter = popularMusicAdapter
        popularMusicAdapter.addPopular(dummyPopular)
    }

    private fun clickListener() {
        binding.homeHeaderSearchTv.setOnClickListener {
            Log.d("HOME_FRG","검색창 클릭")
            val intent = Intent( context, SearchActivity::class.java)
            startActivity(intent) //intent 에 명시된 액티비티로 이동
        }
    }

    private fun getDummy() {
        dummyStudying.clear()
        dummyPopular.clear()

        dummyStudying.apply {
            add(
                Study(
                    "Kitsch",
                    "IVE",
                    R.drawable.example_album_art_1,
                    10,
                    R.drawable.loader_10
                )
            )
            add(
                Study(
                    "손오공",
                    "세븐틴",
                    R.drawable.example_album_art_2,
                    35,
                    R.drawable.loader_35
                )
            )
            add(
                Study(
                    "Spicy",
                    "에스파",
                    R.drawable.example_album_art_3,
                    50,
                    R.drawable.loader_50
                )
            )
            add(
                Study(
                    "Ditto",
                    "뉴진스",
                    R.drawable.example_album_art_4,
                    85,
                    R.drawable.loader_85
                )
            )
            add(
                Study(
                    "Hard to love",
                    "블랙핑크",
                    R.drawable.example_album_art_5,
                    100,
                    R.drawable.loader_100
                )
            )
        }

        dummyPopular.apply {
            add(
                PopularMusic(
                    1,
                    "Kitsch",
                    "IVE",
                    R.drawable.example_album_art_1
                )
            )
            add(
                PopularMusic(
                    2,
                    "손오공",
                    "세븐틴",
                    R.drawable.example_album_art_2
                )
            )
            add(
                PopularMusic(
                    3,
                    "Spicy",
                    "에스파",
                    R.drawable.example_album_art_3
                )
            )
        }
    }
}