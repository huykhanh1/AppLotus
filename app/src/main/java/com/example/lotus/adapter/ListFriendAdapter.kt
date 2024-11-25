package com.example.lotus.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lotus.R
import com.example.lotus.databinding.ViewholderListfriendBinding
import com.example.lotus.model.UserModel
import com.example.lotus.viewmodel.HandleFriendViewModel

sealed class FriendItem {
    data class FriendRequest(val user: UserModel) : FriendItem()
    data class Friend(val user: UserModel) : FriendItem()
}

class CombinedFriendAdapter(
    private val items: MutableList<FriendItem>,
    private val viewModel: HandleFriendViewModel,
    private val token: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_FRIEND_REQUEST = 0
        const val TYPE_FRIEND = 1
    }

    // ViewHolder cho Friend Request
    class FriendRequestViewHolder(private val binding: ViewholderListfriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(friendRequest: FriendItem.FriendRequest, viewModel: HandleFriendViewModel, token: String) {
            val user = friendRequest.user
            binding.txtViewName.text = user.userName
            Glide.with(binding.root.context)
                .load(user.image ?: R.drawable.profile_icon)
                .into(binding.imgViewAva)


            binding.btnClick.setOnClickListener { view ->
                val context = view.context
                val popMenu = PopupMenu(context, view)
                popMenu.menuInflater.inflate(R.menu.menu_example, popMenu.menu)

                popMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.option1 -> {
                            viewModel.acceptFriendRequest(user._id, token)
                            Toast.makeText(context, "Invitation accepted\n", Toast.LENGTH_SHORT).show()
                            binding.btnClick.setBackgroundResource(R.drawable.button_rounded_dark_grey)
                            binding.btnClick.text = "accepted"
                            true
                        }
                        R.id.option2 -> {
                            viewModel.rejectFriendRequest(user._id, token)
                            Toast.makeText(context, "Invitation declined\n", Toast.LENGTH_SHORT).show()
                            binding.btnClick.setBackgroundResource(R.drawable.button_rounded_dark_grey)
                            binding.btnClick.text = "don't accepted"
                            true
                        }
                        else -> false
                    }
                }
                popMenu.show()
            }
        }
    }

    // ViewHolder cho Friend
    class FriendViewHolder(private val binding: ViewholderListfriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(friend: FriendItem.Friend, viewModel: HandleFriendViewModel, token: String) {
            val user = friend.user
            binding.txtViewName.text = user.userName
            Glide.with(binding.root.context)
                .load(user.image ?: R.drawable.profile_icon)
                .into(binding.imgViewAva)
            binding.btnClick.setOnClickListener { view ->
                val context = view.context
                val popMenu = PopupMenu(context, view)
                popMenu.menuInflater.inflate(R.menu.menu, popMenu.menu)

                popMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.option1 -> {
                            viewModel.unFriend(user._id, token)
                            Toast.makeText(context, "Accept successfully", Toast.LENGTH_SHORT).show()
                            binding.btnClick.setBackgroundResource(R.drawable.button_rounded_dark_grey)
                            binding.btnClick.text = "Delete friend"
                            true
                        }
                        R.id.option2 -> {
                            Toast.makeText(context, "Invitation declined", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> false
                    }
                }
                popMenu.show()
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is FriendItem.FriendRequest -> TYPE_FRIEND_REQUEST
            is FriendItem.Friend -> TYPE_FRIEND
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ViewholderListfriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            TYPE_FRIEND_REQUEST -> FriendRequestViewHolder(binding)
            TYPE_FRIEND -> FriendViewHolder(binding)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FriendRequestViewHolder -> holder.bind(items[position] as FriendItem.FriendRequest, viewModel, token)
            is FriendViewHolder -> holder.bind(items[position] as FriendItem.Friend, viewModel, token)
        }
    }

    override fun getItemCount(): Int = items.size
    // Thêm hàm này vào CombinedFriendAdapter

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<FriendItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
