package com.example.lotus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lotus.api.MainRetrofit

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> = _userId
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun initRegister(userName: String, email: String, password: String, confirmPassword: String) {
        val registrationData = hashMapOf(
            "userName" to userName,
            "email" to email,
            "password" to password,
            "conformPassword" to confirmPassword
        )

        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.register(registrationData)
                _registrationResult.value = true
                _token.value = response.token
            } catch (e: HttpException) {
                Log.e("Register", "Error: ${e.message()}")
                _registrationResult.value = false
            } catch (e: IOException) {
                Log.e("Register", "Network failure: ${e.message}")
                _registrationResult.value = false
            }
        }
    }

    fun initLogin(email: String, password: String) {
        val credentials = hashMapOf("email" to email, "password" to password)

        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.login(credentials)
                if (response.isSuccessful) {
                    _loginResult.value = true
                    _token.value = response.body()
                    _userId.value = response.body()// assuming response.body() contains the token
                } else {
                    // Xử lý trường hợp phản hồi không thành công từ API
                    _loginResult.value = false
                    _error.value = "Đăng nhập thất bại: ${response.message()}"
                }
            } catch (e: HttpException) {
                Log.e("Login", "Error: ${e.message()}")
                _loginResult.value = false
                _error.value = "Lỗi đăng nhập: ${e.message()}"
            } catch (e: IOException) {
                Log.e("Login", "Network failure: ${e.message}")
                _loginResult.value = false
                _error.value = "Lỗi mạng: không thể kết nối tới máy chủ."
            }
        }
    }
}




