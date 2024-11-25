package com.example.lotus.model

data class UserModel(
    val _id: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String? = "",
    val conformPassword: String? = null,
    val friendRequests: ArrayList<String> = ArrayList(),
    val friends: ArrayList<String> = ArrayList(),
    val createdOn: String? = null,
    val __v: Int? = null,
    val image: String? = null,
    val backgroundImage: String? = null,
    val friendRequestsSent: ArrayList<String> = ArrayList()

)
