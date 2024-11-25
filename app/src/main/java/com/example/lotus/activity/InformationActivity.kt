package com.example.lotus.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lotus.R
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivityProfileBinding
import com.example.lotus.viewmodel.HandleFriendViewModel

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val handleViewModel: HandleFriendViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenInformation()
        // Handle back button
        binding.backBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSend.setOnClickListener{
            sendRequestFriend()

        }
    }

    private fun sendRequestFriend() {
        val userId = intent.getStringExtra("_id") // Lấy receiverId từ Intent
        val token = MainRetrofit.getToken()      // Lấy token từ MainRetrofit

        if (userId != null && token != null) {
            // Gửi yêu cầu kết bạn thông qua ViewModel
            handleViewModel.sendFriendRequest(userId, token)
            binding.btnSend.setBackgroundResource(R.drawable.button_rounded_dark_grey)
            binding.btnSend.text = "Sending!"
        } else {
            Toast.makeText(this, "Invalid user or token", Toast.LENGTH_SHORT).show()
        }
        handleViewModel.successMessage.observe(this) { successMessage ->
            successMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                handleViewModel.clearMessages() // Xóa thông báo sau khi hiển thị
            }
        }

        handleViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()

                handleViewModel.clearMessages() // Xóa lỗi sau khi hiển thị
            }
        }
    }

    private fun screenInformation() {
        val userId = intent.getStringExtra("_id")
        Log.d("hien id", "$userId")
        val token = MainRetrofit.getToken()
        if (userId != null && token != null) {
            handleViewModel.loadUserInformation(userId, token)
        }

        // Observe user data
        handleViewModel.informationUser.observe(this) { user ->
            binding.txtViewName.text = user.userName
            Glide.with(this)
                .load(user.image ?: R.drawable.profile_icon)
                .into(binding.imageViewAvatar)
        }

        // Observe errors
        handleViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
