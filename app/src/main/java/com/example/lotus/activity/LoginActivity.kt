package com.example.lotus.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivityLoginBinding
import com.example.lotus.fragment.ProfileFragment
import com.example.lotus.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val loginPass = binding.password.text.toString()

            if (email.isNotEmpty() && loginPass.isNotEmpty()) {
                viewModel.initLogin(email, loginPass)
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Observe login result
        viewModel.loginResult.observe(this) { success ->
            if (success) {
                viewModel.token.observe(this) { token ->
                        if (token != null ) {
                            // Lưu token và _id vào SharedPreferences
                            val sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE)
                            sharedPref.edit().putString("TOKEN_KEY", token).apply()

                            // Thiết lập token và _id cho MainRetrofit
                            MainRetrofit.setToken(token)

                            // Hiển thị thông báo
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()

                            // Chuyển đến HomeLotus
                            val intent = Intent(this@LoginActivity, HomeLotus::class.java)
                            startActivity(intent)
                            finish()
                        }
                }
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }



        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActitity::class.java))
            finish()
        }
    }


}
