package com.example.lyword.studying

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.FragmentStudyingBinding
import kotlinx.coroutines.*

class StudyingFragment : Fragment() {
    lateinit var binding : FragmentStudyingBinding
    lateinit var db: LywordDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyingBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

        GlobalScope.launch(Dispatchers.Main) {
            initRV()
        }

        return binding.root
    }

    // Initialize Study RecyclerView
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun initRV(){
        withContext(Dispatchers.IO) {
            // Get Study information
            val songs = db.studyDao.getInProgressStudies() as ArrayList<StudyEntity>
            withContext(Dispatchers.Main) {
                // Set RecyclerView
                val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
                binding.studyRecordRv.adapter = rvAdapter
                binding.studyRecordRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}