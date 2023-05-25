package com.example.lyword.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
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
            binding.itemPercentIv.setImageResource(matchPercent(studying.percent))

            val imageUrl = studying.album_art
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo_splash)
                .error(R.drawable.ic_logo_splash)
                .fallback(R.drawable.ic_logo_splash)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.itemAlbumArtCv)
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

    fun matchPercent(percent : Int) : Int {
        return when (percent) {
            0 -> R.drawable.loader_0
            5 -> R.drawable.loader_5
            10 -> R.drawable.loader_10
            15 -> R.drawable.loader_15
            20 -> R.drawable.loader_20
            25 -> R.drawable.loader_25
            30 -> R.drawable.loader_30
            35 -> R.drawable.loader_35
            40 -> R.drawable.loader_40
            45 -> R.drawable.loader_45
            50 -> R.drawable.loader_50
            55 -> R.drawable.loader_55
            60 -> R.drawable.loader_60
            65 -> R.drawable.loader_65
            70 -> R.drawable.loader_70
            75 -> R.drawable.loader_75
            80 -> R.drawable.loader_80
            85 -> R.drawable.loader_85
            90 -> R.drawable.loader_90
            95 -> R.drawable.loader_95
            100 -> R.drawable.loader_100

            else -> R.drawable.loader_0
        }
    }
}