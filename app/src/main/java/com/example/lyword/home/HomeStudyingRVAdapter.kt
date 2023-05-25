package com.example.lyword.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ItemRecentStudyBinding

class HomeStudyingRVAdapter : RecyclerView.Adapter<HomeStudyingRVAdapter.ViewHolder>() {

    private lateinit var context : Context
    private val studying = ArrayList<StudyEntity>()

    inner class ViewHolder(val binding : ItemRecentStudyBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(studying : StudyEntity) {
            binding.itemTitleTv.text = studying.title
            binding.itemArtistTv.text = studying.artist
            binding.itemAlbumArtCv.foreground = ContextCompat.getDrawable(context, studying.album_art)
            binding.itemPercentIv.setImageResource(studying.percent_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemRecentStudyBinding = ItemRecentStudyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studying[position])
    }

    override fun getItemCount(): Int = studying.size

    @SuppressLint("NotifyDataSetChanged")
    fun addStudying(studying : ArrayList<StudyEntity>) {
        this.studying.clear()
        this.studying.addAll(studying)
    }
}