package com.example.lyword.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.MainActivity
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.home.search.SearchActivity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.FragmentHomeBinding
import com.example.lyword.studying.lyrics.LyricsActivity
import org.jsoup.Jsoup
import java.io.IOException

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

        binding.homeRecentStudyHeaderLayout.setOnClickListener {
            val bottomNavigationView = (requireActivity() as MainActivity).getBottomNavigation()
            bottomNavigationView.selectedItemId = R.id.studyingFragment
        }

        binding.homePopularNowHeaderLayout.setOnClickListener {
            val intent = Intent(requireContext(), PopularActivity::class.java)
            startActivity(intent)
        }

        recentStudyingAdapter.setMyItemClickListener(object : HomeStudyingRVAdapter.RecentItemClickListener {
            override fun onRecendClicked(idx: Long) {
                val intent = Intent(context, LyricsActivity::class.java)
                intent.putExtra("studyId", idx)
                startActivity(intent)
            }
        })

        popularMusicAdapter.setMyItemClickListener(object : HomePopularRVAdapter.PopularItemClickListener {
            override fun onPopularClicked(item: PopularMusic) {
                val intent = Intent(context, PopularMusicDialog::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("artist", item.artist)
                intent.putExtra("albumCover", item.album_art)
                startActivity(intent)
            }

        })
    }

    private fun getList() {
        getStudyingList()
        getPopularChart()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getStudyingList() {
        studyingMusic.clear()
        var studyingListThread : Thread = Thread {
            studyingMusic = db.studyDao.getStudyList() as ArrayList<StudyEntity>
            studyingMusic.reverse()
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
        Log.d("HOME_FRAGMENT", studyingMusic.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPopularChart() {
        var url = "https://www.melon.com/chart/"
        popularMusic.clear()

        val thread = object : Thread() {
            override fun run() {
                try {
                    val doc : org.jsoup.nodes.Document? = Jsoup.connect(url).get()
                    val elements = doc?.select("tr.lst50")?.subList(0,3)

                    if (elements?.isNotEmpty() == true) {
                        for (element in elements) {
                            val rank = element.select("span.rank").text()
                            val title = element.select("div.ellipsis.rank01 span a").text()
                            val artist = element.select("div.ellipsis.rank02 span.checkEllipsis a").text()
                            val albumCover = element.select("a.image_typeAll img").attr("src")
                            Log.d("GET_POPULAR", "순위 : $rank, 제목 : $title, 가수 : $artist, 커버 : $albumCover")

                            popularMusic.add(
                                PopularMusic(
                                    rank.toInt(),
                                    title,
                                    artist,
                                    albumCover
                                )
                            )
                        }
                    }
                } catch (e : IOException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }

        popularMusicAdapter.addPopular(popularMusic)
        popularMusicAdapter.notifyDataSetChanged()
    }
}