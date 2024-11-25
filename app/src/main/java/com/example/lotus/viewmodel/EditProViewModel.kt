package com.example.lotus.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotus.api.MainRetrofit
import com.example.lotus.model.UserModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditProViewModel : ViewModel() {
    private val _userProfile = MutableLiveData<UserModel>()
    val userProfile: LiveData<UserModel> = _userProfile
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    private val _requestStatus = MutableLiveData<String>()
    val requestStatus: LiveData<String> = _requestStatus

    // Hàm cập nhật profile với tên người dùng và ảnh
    fun initUpdateProfile(
        token: String,
        userName: String,
        imageUri: Uri?,
        contentResolver: ContentResolver
    ) {
        val userNameBody = userName.toRequestBody("text/plain".toMediaTypeOrNull())

        // Tạo ảnh dưới dạng MultipartBody.Part nếu imageUri tồn tại
        val imagePart = imageUri?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val imageBytes = inputStream?.readBytes()
            val requestBody = imageBytes?.toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", "profile.jpg", requestBody!!)
        }

        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.updateProfile(token, userNameBody, imagePart)
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Update failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                Log.e("Profile Update", "Error: ${e.message}")
                _error.value = e.message
            }
        }
    }

    // Hàm tải thông tin người dùng
    fun loadUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.getUserProfile(token)
                _userProfile.value = response.body()
                _error.value = null
            } catch (e: Exception) {
                Log.e("Profile Load", "Error: ${e.message}")
                _error.value = e.message
            }
        }
    }


}

