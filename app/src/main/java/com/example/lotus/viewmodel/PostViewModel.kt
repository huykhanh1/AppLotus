package com.example.lotus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotus.api.MainRetrofit
import com.example.lotus.model.PostModel
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostModel>>()
    val posts: LiveData<List<PostModel>> = _posts

    private val _postResult = MutableLiveData<Boolean>()
    val postResult: LiveData<Boolean> = _postResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadPosts(token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.postAll(token)
                if (response.isSuccessful) {
                    response.body()?.let { postList ->
                        val sortedPosts = postList.sortedByDescending { it.createdOn }
                        _posts.value = sortedPosts
                        _postResult.value = true
                        _error.value = null
                    } ?: run {
                        _postResult.value = false
                        _error.value = "No data found"
                    }
                } else {
                    _postResult.value = false
                    _error.value = response.errorBody()?.string() ?: "Unknown error occurred"
                }
            } catch (e: Exception) {
                Log.e("Post Load", "Failure: ${e.message}")
                _postResult.value = false
                _error.value = e.message
            }
        }
    }
}
