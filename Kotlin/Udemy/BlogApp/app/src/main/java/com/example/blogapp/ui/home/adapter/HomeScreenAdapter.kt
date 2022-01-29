package com.example.blogapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogapp.core.BaseViewHolder
import com.example.blogapp.data.model.Post
import com.example.blogapp.databinding.PostItemViewBinding

class HomeScreenAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val postBinding =
            PostItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeScreenViewholder(postBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {

            is HomeScreenViewholder -> holder.bind(postList[position])
        }

    }

    override fun getItemCount(): Int = postList.size


    private inner class HomeScreenViewholder(
        val binding: PostItemViewBinding,
        val context: Context
    ) : BaseViewHolder<Post>(binding.root) {

        override fun bind(item: Post) {
            Glide.with(context).load(item.post_image).into(binding.ivPostImage)
            Glide.with(context).load(item.profile_picture).into(binding.ivProfilePicture)
            binding.tvProfileName.text = item.profile_name
            binding.tvPostTime.text = "Hace 2 horas"
            if (item.picture_description.isEmpty()) {
                binding.tvDescription.visibility = View.GONE
            } else {
                binding.tvDescription.text = item.picture_description
            }
        }

    }
}