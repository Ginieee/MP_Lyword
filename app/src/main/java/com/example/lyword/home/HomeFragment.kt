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
import com.example.lyword.data.LywordDatabase
import com.example.lyword.home.search.SearchActivity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    private val recentStudyingAdapter = HomeStudyingRVAdapter()
    private val popularMusicAdapter = HomePopularRVAdapter()

    lateinit var db : LywordDatabase

    private var studyingMusic : ArrayList<StudyEntity> = arrayListOf()
    private var popularMusic : ArrayList<PopularMusic> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

//        getList()
//        setAdapter()
        clickListener()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getList()
        setAdapter()
    }

    private fun setAdapter() {
        binding.homeRecentStudyContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeRecentStudyContentRv.adapter = recentStudyingAdapter
        recentStudyingAdapter.addStudying(studyingMusic)

        binding.homePopularNowContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.homePopularNowContentRv.adapter = popularMusicAdapter
        popularMusicAdapter.addPopular(popularMusic)
    }

    private fun clickListener() {
        binding.homeHeaderSearchTv.setOnClickListener {
            Log.d("HOME_FRG","검색창 클릭")
            val intent = Intent( context, SearchActivity::class.java)
            startActivity(intent) //intent 에 명시된 액티비티로 이동
        }
    }

    private fun getStudyingList() {
        studyingMusic.clear()
        var studyingListThread : Thread = Thread {
            studyingMusic = db.studyDao.getStudyList() as ArrayList<StudyEntity>
            recentStudyingAdapter.addStudying(studyingMusic)
            requireActivity().runOnUiThread {
                recentStudyingAdapter.notifyDataSetChanged()
            }
            if (studyingMusic.isEmpty()) {
                requireActivity().runOnUiThread {
                    binding.homeRecentStudyContentNone.visibility = View.VISIBLE
                }
            } else {
                requireActivity().runOnUiThread {
                    binding.homeRecentStudyContentNone.visibility = View.GONE
                }
            }
        }
        studyingListThread.start()

        try {
            studyingListThread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun getPopularList() {
        popularMusic.clear()
        popularMusic.apply {
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

    private fun getList() {
        getStudyingList()
        getPopularList()
    }
}