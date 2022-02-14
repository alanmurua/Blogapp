package com.example.blogapp.domain.profile

import com.example.blogapp.data.model.Post
import com.google.firebase.auth.FirebaseUser

interface ProfileRepo {
    suspend fun getUserInfo() : FirebaseUser?
    suspend fun getUserPosts() : List<Post>
}