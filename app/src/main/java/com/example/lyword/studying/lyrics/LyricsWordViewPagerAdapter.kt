package com.example.lyword.studying.lyrics

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.databinding.ItemLyricsWordBinding
import com.example.lyword.studying.lyrics.word.WordEntity

class LyricsWordViewPagerAdapter(private var wordList: List<WordEntity>) : RecyclerView.Adapter<LyricsWordViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLyricsWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordEntity) {
            binding.dialogVocTv.text = word.voc
            binding.dialogPronTv.text = word.pron
            binding.dialogMeanTv.text = word.meaning
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemLyricsWordBinding = ItemLyricsWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(wordList[position])

        // 종료 버튼
        holder.binding.dialogLyricsCloseIv.setOnClickListener {
            (holder.binding.dialogLyricsCloseIv.context as Activity).finish()
        }
    }

    override fun getItemCount(): Int  = wordList.size

}