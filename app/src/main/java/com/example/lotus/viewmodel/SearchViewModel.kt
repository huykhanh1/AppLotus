package com.example.lotus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotus.api.MainRetrofit
import com.example.lotus.model.UserModel
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> = _users

    private val _userResult = MutableLiveData<Boolean>()
    val userResult: LiveData<Boolean> = _userResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadUsers(token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.getAllUser(token)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _users.value = it
                        _userResult.value = true
                        _error.value = null
                        Log.d("SearchViewModel", "Users loaded: ${response.body()}")
                    } ?: run {
                        _userResult.value = false
                        _error.value = "No data found"
                    }
                } else {
                    _userResult.value = false
                    _error.value = response.errorBody()?.string() ?: "Error"
                }
            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
                Log.e("SearchViewModel", "Exception loading users: ${e.message}")
            }
        }
    }

}
