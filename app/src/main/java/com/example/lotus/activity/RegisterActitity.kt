package com.example.lotus.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.lotus.databinding.ActivityRegisterActitityBinding
import com.example.lotus.viewmodel.LoginViewModel

class RegisterActitity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterActitityBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterActitityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinBtn.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.passwordConform.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    viewModel.initRegister(username, email, password, confirmPassword)
                } else {
                    Toast.makeText(this, "2 mật khẩu phải trùng nhau", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.registrationResult.observe(this) { success ->
            if (success) {
                viewModel.token.observe(this) { token ->
                    if (token != null) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, "Đăng ký thành công với token: $token", Toast.LENGTH_SHORT).show()
                        // Điều hướng tới màn hình chính hoặc lưu token
                    }
                }
            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }
        }
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this@RegisterActitity, MainActivity::class.java))
            finish()
        }
        binding.backLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActitity, LoginActivity::class.java))
            finish()
        }
    }
}