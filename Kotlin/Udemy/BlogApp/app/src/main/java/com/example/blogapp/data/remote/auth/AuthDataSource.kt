package com.example.blogapp.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.example.blogapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AuthDataSource {

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val authResult =
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun signUp(username: String, email: String, password: String): FirebaseUser? {
        val authRegister =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        authRegister.user?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(User(username, email, "FOTO_URL.PNG")).await()
        }
        return authRegister.user
    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, username: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef =
            FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_pictures")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()

        user?.updateProfile(profileUpdates)?.await()
    }

}