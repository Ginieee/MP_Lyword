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
import com.example.lyword.home.notify.NoticeActivity
import com.example.lyword.studying.lyrics.LyricsActivity
import okhttp3.internal.notify
import org.jsoup.Jsoup
import java.io.IOException

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    private val recentStudyingAdapter = HomeStudyingRVAdapter()
    private val popularMusicAdapter = HomePopularRVAdapter()

    lateinit var db : LywordDatabase

    private var studyingMusic : ArrayList<StudyEntity> = arrayListOf()
    private var popularMusic : ArrayList<PopularMusic> = arrayListOf()

    private var title : String = ""
    private var artist : String = ""
    private var albumCover : String = ""
    private var previewUrl : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

        clickListener()
        Log.d("HOME_FRAG", "OnCreate")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getList()
        setAdapter()
        setNotice()
        Log.d("HOME_FRAG", "OnResume")
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
        binding.homeHeaderNoticeBellIv.setOnClickListener {
            val intent = Intent(context, NoticeActivity::class.java)
            startActivity(intent)
        }

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

//                binding.homeLoadingLv.visibility = View.VISIBLE

                title = item.title
                artist = item.artist
                albumCover = item.album_art

                val intent = Intent(context, PopularMusicDialog::class.java)
                intent.putExtra("title", title)
                intent.putExtra("artist", artist)
                intent.putExtra("albumCover", albumCover)
                intent.putExtra("previewUrl", "")
                startActivity(intent)

//                var search = (item.title + " " + item.artist)
//                search = search.replace(" ", "+")
//                getSearchResult(search)
//                Log.d("POPULAR_URL", search)
            }

        })
    }

    private fun setNotice() {
        val thread = Thread {
            val notices = db.notifyDao.getUnreadNotice()
            Log.d("NOTICE_READ", notices.toString())

            if (notices.isEmpty()) {
                requireActivity().runOnUiThread {
                    binding.homeHeaderNoticeEclipseIv.visibility = View.GONE
                }
            } else {
                requireActivity().runOnUiThread {
                    binding.homeHeaderNoticeEclipseIv.visibility = View.VISIBLE
                }
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
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

//    private fun getSearchResult(search : String) {
//        val iTunesService = ITunesService()
//        iTunesService.setITunesView(this)
//
//        iTunesService.getSearchResult(search)
//    }
//
//    override fun onSearchITunesSuccess(count: Int, result: List<ITunesResult>?) {
//        if (count == 100) {
//            Toast.makeText(context, "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
//        }
//        else if (count > 0) {
//            Log.d("SEARCH_ACT", result.toString())
//            if (result?.isNotEmpty() == true) {
//                previewUrl = result[0].previewUrl
//
//                val intent = Intent(context, PopularMusicDialog::class.java)
//                intent.putExtra("title", title)
//                intent.putExtra("artist", artist)
//                intent.putExtra("albumCover", albumCover)
//                intent.putExtra("previewUrl", previewUrl)
//                startActivity(intent)
//            }
//        } else {
//            previewUrl = ""
//
//            val intent = Intent(context, PopularMusicDialog::class.java)
//            intent.putExtra("title", title)
//            intent.putExtra("artist", artist)
//            intent.putExtra("albumCover", albumCover)
//            intent.putExtra("previewUrl", previewUrl)
//            startActivity(intent)
//        }
//
//        binding.homeLoadingLv.visibility = View.GONE
//    }
}