package com.example.lyword.studying

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.studying.lyrics.LyricsActivity
import com.example.lyword.databinding.ItemStudyingSongBinding

class StudyingSongRVAdapter(var songList: ArrayList<StudyEntity>, var context: Context): RecyclerView.Adapter<StudyingSongRVAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemStudyingSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: StudyEntity) {
            binding.studyingItemSongIv.text = songs.title
            binding.studyingItemSingerIv.text = songs.artist
            binding.studyingItemLoaderIv.setImageResource(matchPercent(songs.percent))

            val imageUrl = songs.album_art
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo_splash)
                .error(R.drawable.ic_logo_splash)
                .fallback(R.drawable.ic_logo_splash)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.studyingItemAlbumIv)
            //binding.studyingItemLoaderIv.setImageResource(songs.progress)
        }

        fun setLyrics(songs: StudyEntity) {
            // 해당 Recyclerview의 데이터 넣어서 LyricsActivity 띄우기
            val intent = Intent(context, LyricsActivity::class.java)
            intent.putExtra("studyId", songs.studyId)
            (context as Activity).startActivityForResult(intent,101)
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding: ItemStudyingSongBinding = ItemStudyingSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position])

        // 해당 노래 학습 시작하기
        holder.binding.studyingItemStartBtn.setOnClickListener {
            holder.setLyrics(songList[position])
        }
    }

    override fun getItemCount(): Int {
        return songList.size
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