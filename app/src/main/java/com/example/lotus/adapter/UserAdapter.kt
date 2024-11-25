package com.example.lotus.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lotus.R
import com.example.lotus.databinding.ViewholderUserBinding
import com.example.lotus.model.UserModel

class UserAdapter(
    val items: MutableList<UserModel>,
    private val onUserClicked: (String) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("UserAdapter", "onCreateViewHolder called")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.binding.txtViewName.text = user.userName

        if (user.image != null) {
            Glide.with(holder.itemView.context)
                .load(user.image)
                .into(holder.binding.imgViewAva)
        } else {
            holder.binding.imgViewAva.setImageResource(R.drawable.profile_icon)
        }
        Log.d("UserAdapter", "onBindViewHolder called for user: ${user.userName}")

        // Set click listener
        holder.itemView.setOnClickListener {
            onUserClicked(user._id)
        }
    }

    override fun getItemCount(): Int = items.size


    fun updateUsers(newUsers: List<UserModel>) {
        items.clear()
        items.addAll(newUsers)
        notifyDataSetChanged()
        Log.d("UserAdapter", "Updated users: $newUsers")
    }

}
