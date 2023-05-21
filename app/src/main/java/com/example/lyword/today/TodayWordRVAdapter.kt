package com.example.lyword.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.R

class TodayWordRVAdapter(private var todayWords: ArrayList<TodayWord>): RecyclerView.Adapter<TodayWordViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayWordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today_word, parent, false)

        return TodayWordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todayWords.size
    }

    override fun onBindViewHolder(holder: TodayWordViewHolder, position: Int) {
        holder.todayLyrics.text=todayWords[position].lyrics
        holder.todayKoreanLyrics.text=todayWords[position].korean_lyrics
    }
}

class TodayWordViewHolder(view: View): RecyclerView.ViewHolder(view) {
    //var todayLyrics = view.item_today_lyrics
    //var todayKoreanLyrics = view.item_today_korean_lyrics
    var todayLyrics = view.findViewById<TextView>(R.id.item_today_lyrics)
    var todayKoreanLyrics = view.findViewById<TextView>(R.id.item_today_korean_lyrics)
}