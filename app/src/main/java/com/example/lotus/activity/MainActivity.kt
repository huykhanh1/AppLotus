package com.example.lotus.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainRetrofit.initialize(this)
        val token = MainRetrofit.getToken()
        if (token != null) {
            // Token đã tồn tại, chuyển đến HomeLotus
            startActivity(Intent(this, HomeLotus::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActitity::class.java))
            finish()
        }


    }



}