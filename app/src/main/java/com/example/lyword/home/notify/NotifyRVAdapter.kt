package com.example.lyword.home.notify

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
import com.example.lyword.data.entity.NotifyEntity
import com.example.lyword.databinding.ItemNoticeBinding

class NotifyRVAdapter : RecyclerView.Adapter<NotifyRVAdapter.ViewHolder>() {
    private lateinit var context : Context
    private val notifyList = ArrayList<NotifyEntity>()

    interface NotifyItemClickListener {
        fun onNotifyClicked(item : NotifyEntity)
    }

    private lateinit var mItemClickListener : NotifyItemClickListener

    fun setMyItemClickListener(itemClickListener : NotifyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding : ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (notify : NotifyEntity, position : Int) {
            binding.noticeTitleTv.text = notify.title
            binding.noticeContentTv.text = notify.content
            binding.noticeTimeTv.text = notify.time
            if (notify.img.isNullOrBlank()) {
                binding.noticeIv.setImageResource(R.drawable.ic_notice_bell)
            } else {
                val imageUrl = notify.img
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_logo_splash)
                    .error(R.drawable.ic_logo_splash)
                    .fallback(R.drawable.ic_logo_splash)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(binding.noticeIv)
            }

            if (notify.isRead == 1) {
                binding.noticeUnreadIv.visibility = View.INVISIBLE
            } else {
                binding.noticeUnreadIv.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemNoticeBinding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = notifyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifyList[position], position)

        holder.itemView.setOnClickListener {
            mItemClickListener.onNotifyClicked(notifyList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNotice(notices : ArrayList<NotifyEntity>) {
        notifyList.clear()
        notifyList.addAll(notices)
    }
}