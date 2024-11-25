package com.example.lotus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotus.api.MainRetrofit
import com.example.lotus.model.FriendModel
import com.example.lotus.model.UserModel
import kotlinx.coroutines.launch

class HandleFriendViewModel : ViewModel() {
    private val _informationUser = MutableLiveData<UserModel>()
    val informationUser: LiveData<UserModel> = _informationUser

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _friendRequests = MutableLiveData<List<UserModel>>()
    val friendRequests: LiveData<List<UserModel>> = _friendRequests

private val _friend = MutableLiveData<List<UserModel>>()
    val friend: LiveData<List<UserModel>> = _friend

    //hien thong tin cua user
    fun loadUserInformation(userId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.findUserProfile(userId, token)
                if (response.isSuccessful) {
                    _informationUser.value = response.body()
                    _error.value = null
                } else {
                    _error.value = response.errorBody()?.string() ?: "Error loading user profile"
                }
            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    //gửi lởi mời kết bạn
    fun sendFriendRequest(receiverId: String, token: String) {
        viewModelScope.launch {
            try {
                val friendModel = FriendModel(receiverId = receiverId)
                val response = MainRetrofit.instance.sendRequest(
                    token = token,
                    receiverId = friendModel
                )
                if (response.isSuccessful) {
                    _successMessage.value = "Friend request sent successfully!"
                    _error.value = null
                } else {
                    _successMessage.value = null
                    _error.value = response.errorBody()?.string() ?: "Error sending friend request"
                }
            } catch (e: Exception) {
                _successMessage.value = null
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    fun acceptFriendRequest(friendId: String, token: String) {
        viewModelScope.launch {
            try {
                val friendModel = FriendModel(friendId = friendId)
                val response = MainRetrofit.instance.acceptRequest(
                    token = token,
                    friendId = friendModel
                )
                if (response.isSuccessful) {
                    _successMessage.value = "Friend request accepted!"
                    _error.value = null
                } else {
                    _successMessage.value = null
                    _error.value = response.errorBody()?.string() ?: "Error sending friend request"
                }
            } catch (e: Exception) {
                _successMessage.value = null
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    fun rejectFriendRequest(friendId: String, token: String) {
        viewModelScope.launch {
            try {
                val friendModel = FriendModel(friendId = friendId)
                val response = MainRetrofit.instance.rejectRequest(
                    token = token,
                    friendId = friendModel
                )
                if (response.isSuccessful) {
                    _successMessage.value = "Do not accept friend requests!"
                    _error.value = null
                } else {
                    _successMessage.value = null
                    _error.value = response.errorBody()?.string() ?: "Error sending friend request"
                }
            } catch (e: Exception) {
                _successMessage.value = null
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    fun unFriend(friendId: String, token: String) {
        viewModelScope.launch {
            try {
                val friendModel = FriendModel(friendId = friendId)
                val response = MainRetrofit.instance.unfriend(
                    token = token,
                    friendId = friendModel
                )
                if (response.isSuccessful) {
                    _successMessage.value = "Do not accept friend requests!"
                    _error.value = null
                } else {
                    _successMessage.value = null
                    _error.value = response.errorBody()?.string() ?: "Error sending friend request"
                }
            } catch (e: Exception) {
                _successMessage.value = null
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    // hiện danh sách lời mời
    fun listFriendRequest(token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.getFriendRequests(token)
                if (response.isSuccessful) {
                    _friendRequests.value = response.body()
                } else {
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = null
            }
        }
    }

    fun listFriend(token: String) {
        viewModelScope.launch {
            try {
                val response = MainRetrofit.instance.getFriend(token)
                if (response.isSuccessful) {
                    _friend.value = response.body()
                } else {
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = null
            }
        }
    }


    fun clearMessages() {
        _successMessage.value = null
        _error.value = null
    }

}
