package com.example.lyword

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.databinding.ItemPopularNowBinding

class SearchRVAdapter : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    private lateinit var context : Context
    private val results = ArrayList<ITunesResult>()

    inner class ViewHolder(val binding : ItemPopularNowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result : ITunesResult, position: Int) {
            binding.itemPopularNowRankTv.text = position.toString()
            binding.itemPopularNowTitleTv.text = result.trackName
            binding.itemPopularNowArtistTv.text = result.artistName

            val imageUrl = result.artworkUrl100
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo_splash)
                .error(R.drawable.ic_logo_splash)
                .fallback(R.drawable.ic_logo_splash)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.itemPopularNowAlbumArtIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemPopularNowBinding = ItemPopularNowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], position + 1)
    }

    override fun getItemCount(): Int = results.size

    @SuppressLint("NotifyDataSetChanged")
    fun addResults(results : ArrayList<ITunesResult>) {
        this.results.clear()
        this.results.addAll(results)
    }
}