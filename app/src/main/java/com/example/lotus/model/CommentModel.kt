package com.example.lotus.model

// Model cho comment
data class CommentModel(
    val _id: String? = "",
    val content: String? = "",
    val user: Any? = null, // Sử dụng Any để cho phép linh hoạt kiểu dữ liệu
    val createdOn: String? = ""
)





