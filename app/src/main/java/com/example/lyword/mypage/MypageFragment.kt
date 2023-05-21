package com.example.lyword.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lyword.databinding.FragmentMypageBinding
import com.example.lyword.studying.lyrics.LyricsViewpagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding

    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val name = result.data?.getStringExtra("name")!!
            binding.mypageNickname.text = name
            val intro = result.data?.getStringExtra("intro")!!
            binding.profileIntroTv.text = intro
            val imageUriString = result.data?.getStringExtra("imageUri")
            val imageUri = if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

            if (imageUri != null) {
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.profileImg)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)


        // Viewpager 세팅
        val mypageAdapter = MypageViewpagerAdapter(this)
        val menu = arrayListOf("Ongoing", "Completion")
        binding.mypageContentVp.adapter = mypageAdapter
        TabLayoutMediator(binding.mypageMenuTb, binding.mypageContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        binding.mypageEditBt.setOnClickListener {

            val intent = Intent(activity, MyProfileActivity::class.java)

            // 이름, 자기소개 ProfileEditActivity로 보내주기
            var name: String = binding.mypageNickname.text.toString()
            var intro: String = binding.profileIntroTv.text.toString()
            intent.putExtra("name", name)
            intent.putExtra("intro", intro)

            startForResult.launch(intent)
        }
        return binding.root
    }

}