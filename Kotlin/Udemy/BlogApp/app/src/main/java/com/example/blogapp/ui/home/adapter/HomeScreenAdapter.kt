package com.example.blogapp.ui.home.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogapp.R
import com.example.blogapp.core.BaseViewHolder
import com.example.blogapp.core.TimeUtils
import com.example.blogapp.core.hide
import com.example.blogapp.core.show
import com.example.blogapp.data.model.Post
import com.example.blogapp.databinding.PostItemViewBinding

class HomeScreenAdapter(private val postList: List<Post>,private val onPostClickListener: onPostClickListener) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var postClickListener : onPostClickListener? = null

    init {
        postClickListener = onPostClickListener
    }
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
            profileInfo(item)
            addPostTimeStamp(item)
            postImage(item)
            postDescription(item)
            tintFavoritePost(item)
            likesCount(item)
            likeClickAction(item)
        }

        private fun profileInfo(post: Post){
            post.poster?.let {
                Glide.with(context).load(post.poster.profile_picture).into(binding.ivProfilePicture)
                binding.tvProfileName.text = post.poster.username
            }

        }

        private fun addPostTimeStamp(post : Post){

            val createdAt =(post.created_at?.time?.div(1000L))?.let {
                TimeUtils.getTimeAgo(it.toInt())
            }
            binding.tvPostTime.text = createdAt
        }

        private fun postImage(post : Post){
            Glide.with(context).load(post.post_image).into(binding.ivPostImage)

        }


        private fun postDescription(post : Post){

            if (post.picture_description.isEmpty()) {
                binding.tvDescription.visibility = View.GONE
            } else {


                binding.tvDescription.text = post.picture_description
            }
        }

        private fun tintFavoritePost(post : Post){
            if (!post.liked){
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_border_24))
                binding.ivLike.setColorFilter(ContextCompat.getColor(context,R.color.black))
            } else {
                binding.ivLike.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_favorite_24))
                binding.ivLike.setColorFilter(ContextCompat.getColor(context,R.color.red))
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

        private fun likeClickAction(post: Post){
            binding.ivLike.setOnClickListener {
                if (post.liked) post.apply { liked = false } else post.apply { liked = true }
                tintFavoritePost(post)
                onPostClickListener?.onLikeButtonClick(post,post.liked)
            }
        }
    }
}
interface onPostClickListener{
    fun onLikeButtonClick(post: Post, liked :Boolean)
}