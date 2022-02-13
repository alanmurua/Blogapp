package com.example.blogapp.data.remote.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
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
}