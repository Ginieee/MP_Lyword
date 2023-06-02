package com.example.lyword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.dao.MypageDao
import com.example.lyword.data.entity.MypageEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.databinding.ActivityMainBinding
import com.example.lyword.databinding.ActivityOnboardBinding
import com.example.lyword.databinding.BottomSheetClauseBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardActivity : AppCompatActivity() {

    lateinit var binding : ActivityOnboardBinding

    lateinit var db : LywordDatabase
    private var mypage : MypageEntity = MypageEntity()
    private lateinit var myPageDao: MypageDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LywordDatabase.getInstance(this)
        myPageDao = db.myPageDao

        binding.onboardStart.setOnClickListener{
            openClauseBottomSheet()
        }

        // 이름이랑 받아오기
        UserApiClient.instance.me { user, error ->
            val nickname = user?.kakaoAccount?.profile?.nickname

            val mypage = MypageEntity(name = nickname ?: "", introduction = "", profileImg = "https://blog.kakaocdn.net/dn/c3vWTf/btqUuNfnDsf/VQMbJlQW4ywjeI8cUE91OK/img.jpg", level = 1, completion = 0, ongoing = 0)

            CoroutineScope(Dispatchers.IO).launch {
                val mypageId = myPageDao.insertMypage(mypage)
                // 저장된 데이터의 ID 확인
                Log.d("OnboardActivity", "Inserted Mypage ID: $mypageId")
            }

        }

    }

    private fun openClauseBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val dialogBinding =
            BottomSheetClauseBinding.inflate(LayoutInflater.from(this))

        dialog.setContentView(dialogBinding.root)

        with(dialogBinding) {
            clauseNextButton.isEnabled = false

            allAgreeCheckBox.setOnClickListener {
                serviceAgreeCheckBox.isChecked = allAgreeCheckBox.isChecked
                personalInfoAgreeCheckBox.isChecked = allAgreeCheckBox.isChecked
                clauseNextButton.isEnabled = allAgreeCheckBox.isChecked
            }

            serviceAgreeCheckBox.setOnClickListener {
                allAgreeCheckBox.isChecked = serviceAgreeCheckBox.isChecked && personalInfoAgreeCheckBox.isChecked
                clauseNextButton.isEnabled = allAgreeCheckBox.isChecked
            }

            personalInfoAgreeCheckBox.setOnClickListener {
                allAgreeCheckBox.isChecked = serviceAgreeCheckBox.isChecked && personalInfoAgreeCheckBox.isChecked
                clauseNextButton.isEnabled = allAgreeCheckBox.isChecked
            }

            clauseNextButton.setOnClickListener {

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

                dialog.dismiss()
                finish()
            }
        }
        dialog.show()
    }

}