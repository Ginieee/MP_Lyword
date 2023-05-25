package com.example.lyword.studying

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.studying.lyrics.LyricsActivity
import com.example.lyword.databinding.ItemStudyingSongBinding

class StudyingSongRVAdapter(var songList: ArrayList<StudyEntity>, var context: Context): RecyclerView.Adapter<StudyingSongRVAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemStudyingSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: StudyEntity) {
            binding.studyingItemSongIv.text = songs.title
            binding.studyingItemSingerIv.text = songs.artist
            //binding.studyingItemAlbumIv.setImageResource(songs.coverImg)
            //binding.studyingItemLoaderIv.setImageResource(songs.progress)
        }

        fun setLyrics(songs: StudyEntity) {
            // 해당 Recyclerview의 데이터 넣어서 LyricsActivity 띄우기
            val intent = Intent(context, LyricsActivity::class.java)
            intent.putExtra("title",songs.title)
            intent.putExtra("singer",songs.artist)
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