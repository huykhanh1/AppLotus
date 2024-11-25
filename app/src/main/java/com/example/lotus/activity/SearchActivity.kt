package com.example.lotus.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lotus.adapter.UserAdapter
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.ActivitySearchBinding
import com.example.lotus.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        setupRecyclerView()
        observeUsers()
        // Set up back button to navigate to HomeLotus
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeLotus::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun setupRecyclerView() {
        userAdapter = UserAdapter(mutableListOf()) { userId ->
            val intent = Intent(this, InformationActivity::class.java)
            intent.putExtra("_id", userId) // Pass the user ID
            startActivity(intent)
        }
        binding.rcvUser.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = userAdapter
        }
    }

    private fun observeUsers() {

        searchViewModel.users.observe(this) { users ->
            userAdapter.updateUsers(users)
        }

        searchViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Lỗi: $it", Toast.LENGTH_SHORT).show()
            }
        }

        MainRetrofit.getToken()?.let { token ->
            searchViewModel.loadUsers("Bearer $token")
        } ?: run {
            Toast.makeText(this, "Token không tồn tại", Toast.LENGTH_SHORT).show()

        }
    }
}
