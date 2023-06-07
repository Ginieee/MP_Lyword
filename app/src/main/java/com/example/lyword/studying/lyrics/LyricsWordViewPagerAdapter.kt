package com.example.lyword.studying.lyrics

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.databinding.ItemLyricsWordBinding
import com.example.lyword.data.entity.WordEntity

class LyricsWordViewPagerAdapter(private var wordList: List<WordEntity>) : RecyclerView.Adapter<LyricsWordViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLyricsWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: WordEntity) {
            binding.dialogVocTv.text = word.wordOrigin
            binding.dialogPronTv.text = word.wordPronunciation
            binding.dialogMeanTv.text = word.wordEnglish
        }
        // Show default screen
        fun emptyInit() {
            binding.dialogLyricsCl.visibility = View.INVISIBLE
            binding.dialogLyricsDefaultIv.visibility = View.VISIBLE
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
        // If there is no word in selected sentence, show default screen
        if(wordList[0].wordOrigin == "0"){
            holder.emptyInit()
        } else {
            holder.bind(wordList[position])
        }
        // Exit button
        holder.binding.dialogLyricsCloseIv.setOnClickListener {
            (holder.binding.dialogLyricsCloseIv.context as Activity).finish()
        }
    }

    override fun getItemCount(): Int  = wordList.size

}