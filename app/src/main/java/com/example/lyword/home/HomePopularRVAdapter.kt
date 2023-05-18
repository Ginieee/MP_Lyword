package com.example.lyword.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.databinding.ItemPopularNowBinding

class HomePopularRVAdapter : RecyclerView.Adapter<HomePopularRVAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val popular = ArrayList<PopularMusic>()

    inner class ViewHolder(val binding : ItemPopularNowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(popular : PopularMusic) {
            binding.itemPopularNowRankTv.text = popular.rank.toString()
            binding.itemPopularNowTitleTv.text = popular.title
            binding.itemPopularNowArtistTv.text = popular.artist
            binding.itemPopularNowAlbumArtIv.setImageResource(popular.album_art)
            binding.itemPopularNowAlbumArtIv.clipToOutline = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemPopularNowBinding = ItemPopularNowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popular[position])
    }

    override fun getItemCount(): Int = popular.size

    @SuppressLint("NotifyDataSetChanged")
    fun addPopular(popular : ArrayList<PopularMusic>) {
        this.popular.clear()
        this.popular.addAll(popular)
    }
}