package com.example.lotus.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lotus.R
import com.example.lotus.adapter.AdapterViewPage
import com.example.lotus.databinding.ActivityHomeLotusBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeLotus : AppCompatActivity() {
    private lateinit var binding: ActivityHomeLotusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeLotusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gán Adapter cho ViewPager2
        binding.fragmentContainer1.adapter = AdapterViewPage(this)

        setupTabLayout()
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabNextLayout

        TabLayoutMediator(tabLayout, binding.fragmentContainer1) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.home_icon2)
                1 -> tab.setIcon(R.drawable.message_icon2)
                2 -> tab.setIcon(R.drawable.bell_icon)
                3 -> tab.setIcon(R.drawable.profile_icon)
            }
        }.attach()

        // Set màu sắc cho tab
        tabLayout.setTabTextColors(
            android.graphics.Color.parseColor("#808080"), // Màu cho tab không được chọn
            android.graphics.Color.parseColor("#FF000000")  // Màu cho tab được chọn
        )
    }
}
