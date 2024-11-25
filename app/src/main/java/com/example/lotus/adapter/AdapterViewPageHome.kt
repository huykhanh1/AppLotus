package com.example.lotus.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lotus.fragment.NewsLotusFragment
import com.example.lotus.fragment.NewsTrendFragment

class AdapterViewPageHome(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> NewsLotusFragment()
            1 -> NewsTrendFragment()
            else -> NewsLotusFragment()
        }
    }

}