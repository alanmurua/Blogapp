package com.example.blogapp.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun singIn(email: String, password: String): FirebaseUser?
    suspend fun singUp(username: String, email: String, password: String): FirebaseUser?
    suspend fun updateUserProfile(imageBitmap: Bitmap,username: String)
}