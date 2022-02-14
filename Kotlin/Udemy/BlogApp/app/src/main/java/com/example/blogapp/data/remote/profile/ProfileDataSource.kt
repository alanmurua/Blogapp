package com.example.blogapp.data.remote.profile

import com.example.blogapp.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileDataSource {
    suspend fun getUserInfo() :FirebaseUser?{
        return withContext(Dispatchers.IO){
            val userRef = FirebaseAuth.getInstance().currentUser
            userRef?.let {
                userRef
            }
        }
    }

    suspend fun getUserPosts() :List<Post>{
        val postList = mutableListOf<Post>()
        withContext(Dispatchers.IO){
            val userRef = FirebaseAuth.getInstance().currentUser
            userRef?.let {
                val querySnapshot = FirebaseStorage.getInstance().reference.child("${userRef.uid}/posts/").listAll().await()
                querySnapshot.items.forEach {
                    postList.add(it)
                }
            }
        }
        return postList
    }

}