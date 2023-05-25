package com.example.lyword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.lyword.databinding.ActivityMainBinding
import com.example.lyword.databinding.ActivityOnboardBinding
import com.example.lyword.databinding.BottomSheetClauseBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.user.UserApiClient

class OnboardActivity : AppCompatActivity() {

    lateinit var binding : ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.onboardStart.setOnClickListener{
            openClauseBottomSheet()
        }

        // 이름이랑 닉네임 받아오기
        UserApiClient.instance.me { user, error ->
//            binding.nickname.text = "${user?.kakaoAccount?.profile?.nickname}"
//            binding.email.text = "${user?.kakaoAccount?.email}"
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