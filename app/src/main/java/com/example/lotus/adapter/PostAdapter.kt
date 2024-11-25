package com.example.lotus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lotus.model.PostModel
import com.example.lotus.databinding.ViewholderPostBinding
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(val items: MutableList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewholderPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = items[position]
        holder.binding.txtNameNews.text = post.user.userName

        // Xử lý ngày đăng
        val formattedDate = formatPostDate(post.createdOn.toString())
        holder.binding.txtDate.text = formattedDate

        holder.binding.txtStatus.text = post.content

        Glide.with(holder.itemView.context)
            .load(post.user.image)
            .into(holder.binding.imgAvatarNews)

        if (!post.image.isNullOrEmpty()) {
            holder.binding.imgNewsLotus.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(post.image)
                .into(holder.binding.imgNewsLotus)
        } else {
            holder.binding.imgNewsLotus.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    // Cập nhật danh sách mới và làm mới RecyclerView
    fun updatePosts(newPosts: List<PostModel>) {
        items.clear()
        items.addAll(newPosts)
        notifyDataSetChanged()
    }

    // Hàm xử lý ngày tháng
    private fun formatPostDate(createdDate: String): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC") // Set timezone to UTC

        val postDate = try {
            dateFormatter.parse(createdDate)
        } catch (e: Exception) {
            return "Ngày không xác định" // Nếu không thể phân tích được ngày
        }

        val currentDate = Calendar.getInstance()
        val postCalendar = Calendar.getInstance()
        postCalendar.time = postDate

        return when {
            // Nếu hôm nay
            currentDate.get(Calendar.DAY_OF_YEAR) == postCalendar.get(Calendar.DAY_OF_YEAR) &&
                    currentDate.get(Calendar.YEAR) == postCalendar.get(Calendar.YEAR) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(postDate)
            }
            // Hôm qua
            currentDate.get(Calendar.DAY_OF_YEAR) - 1 == postCalendar.get(Calendar.DAY_OF_YEAR) &&
                    currentDate.get(Calendar.YEAR) == postCalendar.get(Calendar.YEAR) -> {
                "${SimpleDateFormat("HH:mm", Locale.getDefault()).format(postDate)} Hôm qua"
            }
            // Các ngày khác
            else -> {
                val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = dateFormatter.format(postDate)
                val time = timeFormatter.format(postDate)
                "$time - $date"
            }
        }
    }
}
