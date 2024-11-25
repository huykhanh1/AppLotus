package com.example.lotus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotus.api.ApiService
import com.example.lotus.api.MainRetrofit
import com.example.lotus.model.UserModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpLoadViewModel : ViewModel() {
    private val apiService: ApiService = MainRetrofit.instance // Trực tiếp khởi tạo ApiService từ MainRetrofit
    private val _userProfile = MutableLiveData<UserModel>()
    val userProfile: LiveData<UserModel> = _userProfile

    fun loadUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUserProfile(token)
                if (response.isSuccessful) {
                    _userProfile.value = response.body() // Assuming body contains the UserModel
                } else {
                    Log.e("UserProfile", "Failed to load user profile")
                }
            } catch (e: Exception) {
                Log.e("UserProfile", "Error: ${e.message}")
            }
        }
    }

    fun upLoadPost(
        content: String,
        token: String,
        imageFile: File?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Convert content to RequestBody
                val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())

                // Prepare image part if imageFile is not null
                val imagePart = imageFile?.let {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", it.name, requestFile)
                }

                // Make API call
                val response = apiService.createPost(token = token, content = contentBody, image = imagePart)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to upload post")
                }
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}
