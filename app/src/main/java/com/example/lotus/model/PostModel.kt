package com.example.lotus.model

data class PostModel(
    val _id: String? = "",
    val content: String? = "",
    val user: UserModel = UserModel(), // Đảm bảo user là một object như JSON
    val likes: ArrayList<String> = ArrayList(),
    val comments: ArrayList<CommentModel> = ArrayList(),
    val createdOn: String? = "",
    val image: String? = null, // Optional vì JSON không có trường này
    val __v: Int = 0
)





