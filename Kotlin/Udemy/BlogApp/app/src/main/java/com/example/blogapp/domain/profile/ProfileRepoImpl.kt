package com.example.blogapp.domain.profile

import com.example.blogapp.data.model.Post
import com.example.blogapp.data.remote.profile.ProfileDataSource
import com.google.firebase.auth.FirebaseUser

class ProfileRepoImpl(private val dataSource : ProfileDataSource) :ProfileRepo {
    override suspend fun getUserInfo(): FirebaseUser? = dataSource.getUserInfo()
    override suspend fun getUserPosts(): List<Post> =dataSource.getUserPosts()
}