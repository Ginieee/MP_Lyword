package com.example.lyword.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ItemCompletionSongBinding

class CompletionSongRVAdapter (var songList: ArrayList<StudyEntity>, var context: Context): RecyclerView.Adapter<CompletionSongRVAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemCompletionSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(study: StudyEntity) {
            binding.studyingItemSongIv.text = study.title
            binding.studyingItemSingerIv.text = study.artist
            //binding.studyingItemAlbumIv.setImageResource(study.album_art)
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