package com.example.lyword.studying.lyrics

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.R
import com.example.lyword.databinding.ItemLyricsBinding

class LyricsRVAdapter(var lyricsList: ArrayList<Lyrics>, var context: Context): RecyclerView.Adapter<LyricsRVAdapter.ViewHolder>() {

    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var selectedItem = -1
    inner class ViewHolder(val binding: ItemLyricsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyrics: Lyrics) {
            binding.itemLyricsLyTv.text = lyrics.ly
            binding.itemLyricsPronTv.text = lyrics.pron
            binding.itemLyricsTransTv.text = lyrics.trans
        }

        fun clickLyrics(position: Int){
            binding.itemLyricsLyTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
            binding.itemLyricsPronTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
            binding.itemLyricsTransTv.setTextColor(ContextCompat.getColor(context, R.color.lyricsHighlight))
        }

        fun startDialog() {
            val intent = Intent(context, LyricsWordDialog::class.java)
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

        holder.binding.itemLyricsLyTv.setOnClickListener {
            holder.clickLyrics(position)
        }
        holder.binding.itemLyricsPronTv.setOnClickListener {
            holder.clickLyrics(position)
        }
        holder.binding.itemLyricsTransTv.setOnClickListener {
            holder.clickLyrics(position)
        }
        holder.binding.itemLyricsStartIv.setOnClickListener {
            holder.startDialog()
        }
    }

    override fun getItemCount(): Int {
        return lyricsList.size
    }
}