package com.example.blogapp.domain.profile

import com.google.firebase.auth.FirebaseUser

interface ProfileRepo {
    suspend fun getUserInfo() : FirebaseUser?
}