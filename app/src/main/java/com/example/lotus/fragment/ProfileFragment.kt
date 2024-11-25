package com.example.lotus.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lotus.R
import com.example.lotus.activity.EditProfileActivity
import com.example.lotus.activity.LoginActivity
import com.example.lotus.api.MainRetrofit
import com.example.lotus.databinding.FragmentProfileBinding
import com.example.lotus.viewmodel.EditProViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: EditProViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Lấy token từ SharedPreferences và tải dữ liệu người dùng
        MainRetrofit.initialize(requireContext())
        val token = MainRetrofit.getToken()
        Log.d("ProfileFragment", "Token: $token")

        if (token != null) {
            profileViewModel.loadUserProfile(token)
        }

        viewProfile()

        binding.txtEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.imageSetting.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun viewProfile() {
        profileViewModel.userProfile.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.txtNameProfile.text = user.userName
                binding.txtEmail.text = user.email

                if (user.image == null) {
                    binding.imgAVTProfile.setImageResource(R.drawable.profile_icon)
                } else {
                    Glide.with(this)
                        .load(user.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.imgAVTProfile)
                }

            } else {
                Log.e("ProfileFragment", "User profile data is null.")
                binding.txtNameProfile.text = "Unknown User"
                binding.txtEmail.text = "No email available"
            }
        }

        profileViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("ProfileFragment", it)
            }
        }
    }


    private fun logout() {
        val sharedPref = requireContext().getSharedPreferences("APP_PREF", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("TOKEN_KEY")
        editor.apply()

        // Quay về LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


}