package com.example.lotus.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lotus.activity.SearchActivity
import com.example.lotus.activity.UpLoadPostActivity
import com.example.lotus.adapter.AdapterViewPageHome
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.FragmentHomeLotusBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeLotusFragment : Fragment() {
    private lateinit var binding: FragmentHomeLotusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeLotusBinding.inflate(inflater, container, false)
        MainRetrofit.initialize(requireContext())
        val token = MainRetrofit.getToken()


        // Thiết lập RecyclerView
        binding.txtSearch.setOnClickListener{
            startActivity(Intent(requireContext(), SearchActivity::class.java))
            activity?.finish()
        }

        binding.postBtn.setOnClickListener{
            val intent =  Intent(requireContext(), UpLoadPostActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewPager with Adapter
        val adapter = AdapterViewPageHome(requireActivity())
        binding.fragmentContainer.adapter = adapter

        // Set up TabLayoutMediator for custom tab names
        TabLayoutMediator(binding.tabLayout, binding.fragmentContainer) { tab, position ->
            tab.text = when (position) {
                0 -> "LOTUS"
                1 -> "XU HƯỚNG"
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}