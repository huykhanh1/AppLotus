package com.example.lotus.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lotus.fragment.HomeLotusFragment
import com.example.lotus.fragment.MessageFragment
import com.example.lotus.fragment.NotificationFragment
import com.example.lotus.fragment.ProfileFragment

class AdapterViewPage(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeLotusFragment()      // Fragment cho tab "LOTUS"
            1 -> MessageFragment()        // Fragment cho tab "XU HƯỚNG"
            2 -> NotificationFragment()      // Fragment cho tab "MỚI NHẤT"
            3 -> ProfileFragment()        // Fragment cho tab "ĐỀ XUẤT"
            else -> HomeLotusFragment()   // Trường hợp mặc định
        }
    }
}
