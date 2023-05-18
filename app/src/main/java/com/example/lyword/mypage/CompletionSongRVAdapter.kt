package com.example.lyword.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.Song
import com.example.lyword.databinding.ItemCompletionSongBinding

class CompletionSongRVAdapter (var songList: ArrayList<Song>, var context: Context): RecyclerView.Adapter<CompletionSongRVAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemCompletionSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songs: Song) {
            binding.studyingItemSongIv.text = songs.title
            binding.studyingItemSingerIv.text = songs.singer
            //binding.studyingItemAlbumIv.setImageResource(songs.coverImg)
        }

//        fun setLyrics(songs: Song) {
//            // 해당 Recyclerview의 데이터 넣어서 LyricsActivity 띄우기
//            val intent = Intent(context, LyricsActivity::class.java)
//            intent.putExtra("title",songs.title)
//            intent.putExtra("singer",songs.singer)
//            (context as Activity).startActivityForResult(intent,101)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding: ItemCompletionSongBinding = ItemCompletionSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position])

        // 해당 노래 확인? 없어도 될지도
//        holder.binding.studyingItemStartBtn.setOnClickListener {
//            holder.setLyrics(songList[position])
//        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}