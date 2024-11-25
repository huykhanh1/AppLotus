package com.example.lotus.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainRetrofit {
    private const val BASE_URL = "http://princehunganh.ddnsfree.com:7554"
    private var token: String? = null

    // Hàm để thiết lập token
    fun setToken(newToken: String) {
        token = newToken
    }

    // Hàm để khởi tạo token từ SharedPreferences (có thể gọi khi khởi tạo app)
    fun initialize(context: Context) {
        val sharedPref = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
        token = sharedPref.getString("TOKEN_KEY", null)
    }
    fun getToken(): String? = token

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val requestBuilder = original.newBuilder()

                // Thêm token vào header (nếu có)
                token?.let {
                    requestBuilder.addHeader("Authorization", it)
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
