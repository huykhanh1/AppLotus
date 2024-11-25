package com.example.lotus.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lotus.adapter.CombinedFriendAdapter
import com.example.lotus.adapter.FriendItem
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.FragmentNotificationBinding
import com.example.lotus.viewmodel.HandleFriendViewModel


class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private val handleViewModel: HandleFriendViewModel by viewModels()
    private lateinit var friendRequestsAdapter: CombinedFriendAdapter
    private lateinit var friendsAdapter: CombinedFriendAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        // Khởi tạo Retrofit
        MainRetrofit.initialize(requireContext())

        // Kiểm tra Token
        val token = MainRetrofit.getToken()
        if (token == null) {
            Log.e("NotificationFragment", "Token không tồn tại")
            Toast.makeText(context, "Token không tồn tại", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        setupRecyclerView()
        observeViewModel()
        setupSwipeRefresh()
        // Gọi API lấy danh sách friend requests
        handleViewModel.listFriendRequest(token)

        handleViewModel.listFriend(token)

        return binding.root
    }

    private fun setupRecyclerView() {
        val token = MainRetrofit.getToken() ?: return

        // Adapter cho Friend Requests
        friendRequestsAdapter = CombinedFriendAdapter(mutableListOf(), handleViewModel, token)
        binding.listFriendRequest.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = friendRequestsAdapter
        }

        // Adapter cho Friends
        friendsAdapter = CombinedFriendAdapter(mutableListOf(), handleViewModel, token)
        binding.listFriend.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }
    }


    private fun observeViewModel() {
        // Quan sát danh sách Friend Requests
        handleViewModel.friendRequests.observe(viewLifecycleOwner) { friendRequests ->
            val friendRequestItems = friendRequests?.map { FriendItem.FriendRequest(it) } ?: emptyList()
            friendRequestsAdapter.updateItems(friendRequestItems)
        }

        // Quan sát danh sách Friends
        handleViewModel.friend.observe(viewLifecycleOwner) { friends ->
            val friendItems = friends?.map { FriendItem.Friend(it) } ?: emptyList()
            friendsAdapter.updateItems(friendItems)
        }

        // Quan sát lỗi
        handleViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }




    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout1.setOnRefreshListener {
            MainRetrofit.getToken()?.let { token ->
                // Làm sạch danh sách
                friendRequestsAdapter.updateItems(emptyList())
                friendsAdapter.updateItems(emptyList())

                // Gọi API để tải dữ liệu mới
                handleViewModel.listFriendRequest(token)
                handleViewModel.listFriend(token)

            } ?: run {
                Toast.makeText(context, "Token không tồn tại", Toast.LENGTH_SHORT).show()
            }

            binding.swipeRefreshLayout1.isRefreshing = false // Tắt hiệu ứng làm mới
        }
    }


}
