package com.example.lyword.home.notify

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.NotifyEntity
import com.example.lyword.databinding.ActivityNoticeBinding
import com.example.lyword.databinding.ActivitySearchBinding
import kotlin.concurrent.thread

class NoticeActivity : AppCompatActivity() {

    lateinit var binding : ActivityNoticeBinding
    lateinit var db : LywordDatabase

    private val noticeAdapter = NotifyRVAdapter()
    private var notices : ArrayList<NotifyEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LywordDatabase.getInstance(this)


        setList()
        setAdapter()
        clickListener()
    }

    private fun clickListener() {
        binding.noticeBackBtn.setOnClickListener {
            finish()
        }

        noticeAdapter.setMyItemClickListener(object : NotifyRVAdapter.NotifyItemClickListener {
            override fun onNotifyClicked(item: NotifyEntity) {
                Log.d("NOTICE_CLICK", item.toString())
                if (item.isRead == 0) item.isRead = 1
                val thread  = Thread {
                    Log.d("NOTICE_CLICK", item.toString())
                    db.notifyDao.updateNotify(item)
                }
                thread.start()
                try {
                    thread.join()
                } catch (e : InterruptedException) {
                    e.printStackTrace()
                }
                setList()
                return
            }

        })
    }

    private fun setAdapter() {
        binding.noticeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.noticeRv.adapter = noticeAdapter
        noticeAdapter.addNotice(notices)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setList() {
        notices.clear()
        val thread = Thread {
            notices = db.notifyDao.getNotifyList() as ArrayList<NotifyEntity>
            Log.d("NOTICE_LIST", notices.toString())
        }
        thread.start()
        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }

        noticeAdapter.addNotice(notices)
        noticeAdapter.notifyDataSetChanged()
        if (notices.isEmpty()) {
            binding.noticeNoneTv.visibility = View.VISIBLE
        } else {
            binding.noticeNoneTv.visibility = View.GONE
        }
    }
}