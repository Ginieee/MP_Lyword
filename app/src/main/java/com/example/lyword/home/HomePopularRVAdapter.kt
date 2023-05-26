package com.example.lyword.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
import com.example.lyword.databinding.ItemPopularNowBinding

class HomePopularRVAdapter : RecyclerView.Adapter<HomePopularRVAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val popular = ArrayList<PopularMusic>()

    interface PopularItemClickListener {
        fun onPopularClicked(item : PopularMusic)
    }

    private lateinit var mItemClickListener : PopularItemClickListener

    fun setMyItemClickListener(itemClickListener : PopularItemClickListener) {
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(val binding : ItemPopularNowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(popular : PopularMusic) {
            binding.itemPopularNowRankTv.text = popular.rank.toString()
            binding.itemPopularNowTitleTv.text = popular.title
            binding.itemPopularNowArtistTv.text = popular.artist

            val imageUrl = popular.album_art
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
        holder.bind(popular[position])

        holder.itemView.setOnClickListener {
            mItemClickListener.onPopularClicked(popular[position])
        }
    }

    override fun getItemCount(): Int = popular.size

    @SuppressLint("NotifyDataSetChanged")
    fun addPopular(popular : ArrayList<PopularMusic>) {
        this.popular.clear()
        this.popular.addAll(popular)
    }
}