package com.example.lotus.activity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lotus.R
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivityUpLoadPostBinding
import com.example.lotus.fragment.HomeLotusFragment
import com.example.lotus.viewmodel.UpLoadViewModel
import java.io.File
import java.io.FileOutputStream

class UpLoadPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpLoadPostBinding
    private val uploadViewModel: UpLoadViewModel by viewModels()
    private var selectedImageFile: File? = null
    private var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpLoadPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve the token from SharedPreferences
        MainRetrofit.initialize(this)
        val token = MainRetrofit.getToken()


        binding.huyTxt.setOnClickListener {
            finish()
        }
        binding.btnDelete.visibility = View.GONE
        binding.btnDelete.setOnClickListener {
            // Xoá ảnh khi nhấn btnDelete
            image = null
            selectedImageFile = null
            binding.imageUplaodPost.setImageDrawable(null)
            binding.btnDelete.visibility = View.GONE
        }
        // Load the user profile
        if (token != null) {
            uploadViewModel.loadUserProfile(token)
            loadProfile()
            binding.selectImageButton.setOnClickListener {
                openGallery()
                Glide.with(this).load(image)
                    .apply(RequestOptions.noTransformation()) // Dùng kích thước gốc của ảnh
                    .into(binding.imageUplaodPost)
            }
            binding.postTxt.setOnClickListener {
                postUpLoad(token)
            }
        }

    }
    private fun uriToFile(uri: Uri): File? {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val file = File(applicationContext.cacheDir, contentResolver.getFileName(uri))
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun ContentResolver.getFileName(uri: Uri): String {
        var name = "temp_image"
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow("_display_name"))
            }
        }
        return name
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            image = data?.data
            image?.let {
                selectedImageFile = uriToFile(it) // Sử dụng uriToFile để chuyển Uri thành File
                Glide.with(this).load(it).apply(RequestOptions.noTransformation())
                    .into(binding.imageUplaodPost)
            }
            binding.btnDelete.visibility = View.VISIBLE
        }
    }



    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }

    private fun loadProfile() {
        // Observe user profile to display username
        uploadViewModel.userProfile.observe(this) { user ->
            if (user != null) {
                binding.txtNamePost.text = user.userName
                if (user.image == null) {
                    binding.imageViewAvatar.setImageResource(R.drawable.profile_icon)
                } else {
                    Glide.with(this).load(user.image).apply(RequestOptions.circleCropTransform())
                        .into(binding.imageViewAvatar)
                }
            } else {
                Log.e("Load Name:", "null")
            }
        }

        // Handle post button click
    }


    private fun postUpLoad(token: String) {
        val content = binding.editTextPost.text.toString()

        // Log giá trị content và token để kiểm tra
        Log.d("UploadPost", "Content: $content")
        Log.d("UploadPost", "Token: $token")

        // Kiểm tra xem file ảnh đã được chọn chưa
        if (selectedImageFile != null) {
            Log.d("UploadPost", "Selected image file: ${selectedImageFile?.absolutePath}")
        } else {
            Log.d("UploadPost", "No image selected")
        }

        // Kiểm tra khi bắt đầu upload
        Log.d("UploadPost", "Starting post upload...")

        uploadViewModel.upLoadPost(content, token, selectedImageFile, onSuccess = {
            Toast.makeText(this, "Đăng bài thành công", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@UpLoadPostActivity, HomeLotus::class.java)
            startActivity(intent)
            finish()
        }, onError = { errorMessage ->
            // Log lỗi để kiểm tra nguyên nhân
            Log.e("UploadError", "Error during post upload: $errorMessage")
            Toast.makeText(this, "Error: ${errorMessage}", Toast.LENGTH_SHORT).show()
        })
    }

}

