package com.example.blogapp.data.remote.home

import com.example.blogapp.core.Result
import com.example.blogapp.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeScreenDataSource {

    suspend fun getLatestPosts(): Result<List<Post>> {
        val postList = mutableListOf<Post>()

        withContext(Dispatchers.IO) {
            val querySnapshot = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING).get().await()
            postList.clear()
            for (post in querySnapshot.documents) {
                post.toObject(Post::class.java)?.let {
                    val isLiked = FirebaseAuth.getInstance().currentUser?.let { safeUser->
                        isPostLiked(post.id,safeUser.uid)
                    }
                    it.apply {
                        created_at = post.getTimestamp(
                            "created_at",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()
                        postList.add(it)
                        id = post.id

                        if (isLiked != null){
                            liked = isLiked
                        }
                    }
                }
            }
        }
        return Result.Success(postList)
    }


    private suspend fun isPostLiked(postId: String, uid: String): Boolean {
        val post =
            FirebaseFirestore.getInstance().collection("postsLikes").document(postId).get().await()

        if (!post.exists()) return false
        val likeArray: List<String> = post.get("likes") as List<String>
        return likeArray.contains(uid)
    }

    fun registerLikeButtonState(postId: String, liked: Boolean) {
        val increment = FieldValue.increment(1)
        val decrement = FieldValue.increment(-1)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val postRef = FirebaseFirestore.getInstance().collection("posts").document(postId)
        val postLikeRef = FirebaseFirestore.getInstance().collection("postsLikes").document(postId)

        val database = FirebaseFirestore.getInstance()

        database.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val likeCount = snapshot.getLong("likes")
            likeCount?.let {
                if (likeCount >= 0) {
                    if (liked) {
                        if (transaction.get(postLikeRef).exists()) {
                            transaction.update(postLikeRef, "likes", FieldValue.arrayUnion(uid))
                        } else {
                            transaction.set(
                                postLikeRef, hashMapOf("likes" to arrayListOf(uid)),
                                SetOptions.merge()
                            )
                        }
                        transaction.update(postRef, "likes", increment)
                    } else {
                        transaction.update(postRef, "likes", decrement)
                        transaction.update(postLikeRef, "likes", FieldValue.arrayRemove(uid))
                    }
                }
            }
        }.addOnFailureListener {
            throw Exception(it.message)
        }

    }
}