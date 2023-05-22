package com.example.lyword.mypage

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.R
import com.example.lyword.databinding.ActivityMainBinding
import com.example.lyword.databinding.ActivityMyProfileBinding


class MyProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyProfileBinding

    var nickname = ""

    var imageUri: Uri? = null

    // 사진 받기
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    // 저장소 권한
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    private fun extractImagePath(imgUri: Uri?){
        imgUri?.let {
            imageUri = it
            Glide.with(this)
                .load(it)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.profileIv)
        }
    }

    private fun openStorage() {
        activityResultLauncher.launch(
            Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE)
                type = "image/*"
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)

        nickname = intent.getStringExtra("name").toString()
        var intro = intent.getStringExtra("intro")

        binding.nicknameEt.setText(nickname)
        binding.introEt.setText(intro)

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                extractImagePath(it.data?.data)
            }
        }
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            finish()
        }
        binding.profileEdit.setOnClickListener {
            showCustomDialog()
        }
        binding.editProfileOkBt.setOnClickListener {
                nickname = binding.nicknameEt.text.toString().trim()
                // 닉네임 변경 넣기
                val intent = Intent(this@MyProfileActivity, MypageFragment::class.java).apply {
                    putExtra("name", binding.nicknameEt.text.toString())
                    putExtra("intro", binding.introEt.text.toString())
                    if (imageUri != null) {
                        putExtra("imageUri", imageUri.toString())
                    }
                }

                setResult(RESULT_OK, intent)
                finish()
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_access_rights_photo)

        val dialogButton = dialog.findViewById<Button>(R.id.access_dialog_ok_bt)
        dialogButton.setOnClickListener {
            dialog.dismiss()
            openStorage()
        }

        dialog.show()
    }

}

