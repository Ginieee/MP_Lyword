package com.example.lyword.studying.lyrics

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.R
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.databinding.ItemLyricsBinding

class LyricsRVAdapter(var lyricsList: ArrayList<SentenceEntity>, var context: Context): RecyclerView.Adapter<LyricsRVAdapter.ViewHolder>() {

    private var selectedItem = 0
    private var lyricsId = 0L
    inner class ViewHolder(val binding: ItemLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: SentenceEntity) {
            binding.itemLyricsLyTv.text = lyrics.sentenceOrigin
            binding.itemLyricsPronTv.text = lyrics.sentencePronunciation
            binding.itemLyricsTransTv.text = lyrics.sentenceEnglish
            lyricsId = lyrics.sentenceId

            if (adapterPosition == selectedItem) {
                setTextColorSelected()
            } else {
                setTextColorDefault()
            }
        }
        // 선택된 position에 대한 설정
        private fun setTextColorSelected() {
            binding.itemLyricsLyTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
            binding.itemLyricsPronTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
            binding.itemLyricsTransTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
            binding.itemLyricsStartIv.visibility = View.VISIBLE
        }
        // 그 외의 position 값들은 전부 기본 세팅
        private fun setTextColorDefault() {
            binding.itemLyricsLyTv.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.itemLyricsPronTv.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.itemLyricsTransTv.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.itemLyricsStartIv.visibility = View.INVISIBLE
        }

        fun startDialog(lyrics: SentenceEntity) {
            val intent = Intent(context, LyricsWordDialog::class.java)
            intent.putExtra("lyricsPosition", lyrics.sentenceId)
            (context as Activity).startActivityForResult(intent,101)
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding: ItemLyricsBinding = ItemLyricsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lyricsList[position])

        holder.binding.itemLyricsLl.setOnClickListener{
            selectedItem = holder.adapterPosition
            notifyDataSetChanged()
        }

        holder.binding.itemLyricsStartIv.setOnClickListener {
            holder.startDialog(lyricsList[position])
        }
    }

    override fun getItemCount(): Int {
        return lyricsList.size
    }
}