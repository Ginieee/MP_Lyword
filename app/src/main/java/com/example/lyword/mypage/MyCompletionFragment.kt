package com.example.lyword.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.FragmentMyCompletionBinding
import com.example.lyword.studying.StudyingSongRVAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyCompletionFragment : Fragment() {
    lateinit var binding: FragmentMyCompletionBinding
    lateinit var db: LywordDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCompletionBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

        GlobalScope.launch(Dispatchers.Main) {
            initRV()
        }

        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun initRV() {

        withContext(Dispatchers.IO) {
//            Log.d("Studying", db.studyDao.getStudyList().toString())
            val songs = db.studyDao.getCompletedStudies() as ArrayList<StudyEntity>
            withContext(Dispatchers.Main) {
                val rvAdapter = StudyingSongRVAdapter(songs, requireContext())
                binding.studyRecordRv.adapter = rvAdapter
                binding.studyRecordRv.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }
}