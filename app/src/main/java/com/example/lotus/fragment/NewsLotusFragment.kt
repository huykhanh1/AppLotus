package com.example.lotus.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lotus.adapter.PostAdapter
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.FragmentNewsLotusBinding
import com.example.lotus.viewmodel.PostViewModel

class NewsLotusFragment : Fragment() {
    private lateinit var binding: FragmentNewsLotusBinding
    private val postViewModel: PostViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsLotusBinding.inflate(inflater, container, false)

        // Initialize token
        MainRetrofit.initialize(requireContext())

        setupRecyclerView()
        setupSwipeRefresh()
        observePosts()

        return binding.root
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(mutableListOf())
        binding.rcvNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }


    private fun setupSwipeRefresh() {
        // Cấu hình SwipeRefreshLayout để làm mới dữ liệu
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Gọi loadPosts để tải lại dữ liệu
            MainRetrofit.getToken()?.let { token ->
                postViewModel.loadPosts(token)
            } ?: run {
                Toast.makeText(context, "Token không tồn tại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observePosts() {
        postViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
            // Tắt refresh sau khi cập nhật dữ liệu
            binding.swipeRefreshLayout.isRefreshing = false
        }

        postViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                // Tắt refresh nếu có lỗi xảy ra
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        // Load dữ liệu ban đầu với token từ MainRetrofit
        MainRetrofit.getToken()?.let { token ->
            postViewModel.loadPosts("Bearer $token")
        } ?: run {
            Toast.makeText(context, "Token không tồn tại", Toast.LENGTH_SHORT).show()
        }
    }
}


