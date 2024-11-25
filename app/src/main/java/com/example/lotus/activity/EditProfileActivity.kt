package com.example.lotus.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lotus.R
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivityEditProfileBinding
import com.example.lotus.viewmodel.EditProViewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProViewModel by viewModels()
    private var imageUri: Uri? = null // Biến lưu trữ Uri của ảnh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Khởi tạo token từ SharedPreferences
        MainRetrofit.initialize(this)
        val token = MainRetrofit.getToken() // Lấy token từ MainRetrofit

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, HomeLotus::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        if (token != null) {
            viewModel.loadUserProfile(token)
            viewEditName()
            binding.btnUpdateProfile.setOnClickListener {
                editProfile(token)
            }
        }

        binding.editPhoto.setOnClickListener { openGallery() }
    }

    private fun editProfile(token: String) {
        val username = binding.editName.text.toString()
        if (username.isNotBlank()) {
            if (imageUri != null) {
                viewModel.initUpdateProfile(token, username, imageUri, contentResolver)
            } else {
                // Nếu không có ảnh, chỉ gửi username
                viewModel.initUpdateProfile(token, username, null, contentResolver)
            }
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeLotus::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Không để trống tên", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data?.data
            if (imageUri != null) {
                Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageViewAvatar)
            }
        }
    }

    private fun viewEditName() {
        viewModel.userProfile.observe(this) { user ->
            if (user != null) {
                binding.editName.setText(user.userName)
                val imageRes = if (user.image == null) R.drawable.profile_icon else user.image
                Glide.with(this)
                    .load(imageRes)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imageViewAvatar)
            } else {
                Log.e("Load Name:", "null")
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
}


