package com.example.lyword

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.databinding.ItemLyricsBinding
import com.example.lyword.databinding.ItemStudyingSongBinding

class LyricsRVAdapter(var lyricsList: ArrayList<Lyrics>, var context: Context): RecyclerView.Adapter<LyricsRVAdapter.ViewHolder>() {

    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    inner class ViewHolder(val binding: ItemLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: Lyrics) {
            binding.itemLyricsLyTv.text = lyrics.ly
            binding.itemLyricsPronTv.text = lyrics.pron
            binding.itemLyricsTransTv.text = lyrics.trans
        }

        fun startDialog() {
            // 다이얼로그로 구현하면 제약이 많아서 그냥 액티비티로 갈아탐
            val intent = Intent(context, LyricsWordDialog::class.java)
            (context as Activity).startActivityForResult(intent,101)

            //LyricsWordDialog().show((context as LyricsActivity).supportFragmentManager, "dialog")
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int
    ): LyricsRVAdapter.ViewHolder {
        val binding: ItemLyricsBinding = ItemLyricsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricsRVAdapter.ViewHolder, position: Int) {
        holder.bind(lyricsList[position])

        holder.binding.itemLyricsLyTv.setOnClickListener {

        }
        holder.binding.itemLyricsPronTv.setOnClickListener {

        }
        holder.binding.itemLyricsTransTv.setOnClickListener {

        }
        holder.binding.itemLyricsStartIv.setOnClickListener {
            holder.startDialog()
        }
    }

    override fun getItemCount(): Int {
        return lyricsList.size
    }
}