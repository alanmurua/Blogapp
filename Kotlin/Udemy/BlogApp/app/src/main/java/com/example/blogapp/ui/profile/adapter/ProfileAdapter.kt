package com.example.blogapp.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogapp.R
import com.example.blogapp.core.BaseViewHolder
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.model.Post
import com.example.blogapp.databinding.ProfileItemPostBinding


class ProfileAdapter(private val postList: List<Post>) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val profileBinding =
            ProfileItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(profileBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ProfileViewHolder -> {
                holder.bind(postList[position])
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    private inner class ProfileViewHolder(val binding: ProfileItemPostBinding, val context: Context) : BaseViewHolder<Post>(binding.root) {

        override fun bind(item: Post) {
            postImage(item)
            postDescription(item)
            likesCount(item)
            tintFavoritePost(item)
        }


        private fun postImage(post: Post) {
            Glide.with(context).load(post.post_image).into(binding.ivPostImage)

        }

        private fun postDescription(post: Post) {

            if (post.picture_description.isEmpty()) {
                binding.tvDescription.visibility = View.GONE
            } else {


                binding.tvDescription.text = post.picture_description
            }
        }

        private fun tintFavoritePost(post : Post){
            if (!post.liked){
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24))
                binding.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24))
                binding.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
        }

        private fun likesCount(post: Post) {
            if (post.likes > 0){
                binding.tvLikes.show()

                binding.tvLikes.text = "${post.likes} Likes"
            } else {
                binding.tvLikes.hide()
            }
        }
    }
}