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
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.dao.MypageDao
import com.example.lyword.data.entity.MypageEntity
import com.example.lyword.databinding.FragmentMypageBinding
import com.example.lyword.studying.lyrics.LyricsViewpagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding
    private lateinit var db: LywordDatabase
    private lateinit var myPageDao: MypageDao

    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                val name = result.data?.getStringExtra("name")!!
                val intro = result.data?.getStringExtra("intro")!!
                val imageUriString = result.data?.getStringExtra("imageUri")
                val imageUri = if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

                if (imageUri != null) {
                    withContext(Dispatchers.Main) {
                        Glide.with(requireContext())
                            .load(imageUri)
                            .into(binding.profileImg)
                    }
                }

//                Log.d("MypageActivity1", "Inserted Mypage: $mypage")
                myPageDao.updateMypageset(name = name ?: "", introduction = intro ?: "", profileImg = imageUriString ?: "")

                withContext(Dispatchers.Main) {
                    // 업데이트된 프로필 정보로 초기화
                    binding.mypageNickname.text = name
                    binding.profileIntroTv.text = intro
                }
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        db = LywordDatabase.getInstance(requireContext())
        myPageDao = db.myPageDao

        // Viewpager 세팅
        val mypageAdapter = MypageViewpagerAdapter(this)
        val menu = arrayListOf("Ongoing", "Completion")
        binding.mypageContentVp.adapter = mypageAdapter
        TabLayoutMediator(binding.mypageMenuTb, binding.mypageContentVp) { tab, position ->
            tab.text = menu[position]
        }.attach()

        CoroutineScope(Dispatchers.Main).launch {
            val mypage = withContext(Dispatchers.IO) {
                myPageDao.getLatestMypage()
            }
            Log.d("MypageActivity2", "Inserted Mypage: $mypage")
            binding.mypageNickname.text = mypage?.name
            binding.profileIntroTv.text = mypage?.introduction
            binding.mypageLevel.text = "Level" + mypage?.level.toString()
            binding.ongoingNum.text = mypage?.ongoing.toString()
            binding.compNum.text = mypage?.completion.toString()
            val imageUriString = mypage?.profileImg
            val imageUri = if (!imageUriString.isNullOrEmpty()) Uri.parse(imageUriString) else null

            if (imageUri != null) {
                Glide.with(requireContext())
                    .load(imageUri)
                    .into(binding.profileImg)
            }
        }



        binding.mypageEditBt.setOnClickListener {

            val intent = Intent(activity, MyProfileActivity::class.java)

            // 이름, 자기소개 ProfileEditActivity로 보내주기
            var name: String = binding.mypageNickname.text.toString()
            var intro: String = binding.profileIntroTv.text.toString()
            intent.putExtra("name", name)
            intent.putExtra("intro", intro)

            startForResult.launch(intent)
        }

        binding.mypageSetting.setOnClickListener {
            val intent = Intent(requireActivity(), SettingActivity::class.java)
            startActivity(intent)

        }
        return binding.root
    }

}