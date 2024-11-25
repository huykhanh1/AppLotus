package com.example.lotus.api

import com.example.lotus.model.FriendModel
import com.example.lotus.model.LoginResponse
import com.example.lotus.model.PostModel
import com.example.lotus.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
//User Mission

    @GET("/users")
    suspend fun getAllUser(@Header("Authorization") token: String? = null): Response<List<UserModel>>  // Đảm bảo trả về List<UserModel>

    @GET("/users/profile")
    suspend fun getUserProfile(@Header("Authorization") token: String? = null): Response<UserModel>

    @POST("/users/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<String> // Adjusted to return LoginResponse for consistency

    @POST("/users")
    suspend fun register(@Body credentials: Map<String, String>): LoginResponse


    @Multipart
    @PATCH("/users")
    suspend fun updateProfile(@Header("Authorization") token: String,@Part("userName") userName: RequestBody,
                              @Part image: MultipartBody.Part? = null
    ): Response<UserModel>


//Post Mission

    @GET("/post")
    suspend fun postAll(@Header("Authorization") token: String? = null): Response<List<PostModel>>

    @GET("users/{id}")
    suspend fun findUserProfile(@Path("id") userId: String,@Header("Authorization") token: String
    ): Response<UserModel>

    @Multipart
    @POST("/post")
    suspend fun createPost(@Header("Authorization") token: String? = null,@Part("content") content: RequestBody,
                           @Part image: MultipartBody.Part? = null
    ): Response<PostModel>


//Friend Mission
    @POST("/friends/send-request")
    suspend fun sendRequest(@Header("Authorization") token: String? = null, @Body receiverId: FriendModel
    ): Response<String>

    @POST("/friends/reject-request")
    suspend fun rejectRequest(@Header("Authorization") token: String? = null, @Body friendId: FriendModel
    ): Response<String>

    @POST("/friends/accept-request")
    suspend fun acceptRequest(@Header("Authorization") token: String? = null, @Body friendId: FriendModel
    ): Response<String>

    @POST("/friends/unfriend")
    suspend fun unfriend(@Header("Authorization") token: String? = null, @Body friendId: FriendModel
    ): Response<String>

    @GET("/friends/friend-requests")
    suspend fun getFriendRequests(@Header("Authorization") token: String? = null): Response<List<UserModel>>


    @GET("/friends/friends-list")
    suspend fun getFriend(@Header("Authorization") token: String? = null): Response<List<UserModel>>


}


//    @GET("/user")
//    suspend fun getUsers(): List<UserModel>
