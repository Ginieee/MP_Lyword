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
}