package com.example.chatapplication.daos

import com.example.chatapplication.models.Post
import com.example.chatapplication.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    private val db = FirebaseFirestore.getInstance()
    val collection = db.collection("post")
    private val auth = Firebase.auth

    fun addPost(text :String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch{
            val userDao = UserDao()
            val userN = userDao.getUserByID(currentUserId).await().toObject(Users::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text, userN, currentTime)
            collection.document().set(post)

        }
    }
    fun postById(postId: String) : Task<DocumentSnapshot>{
        return collection.document(postId).get()
    }
    fun updateLikes(postId : String){
        GlobalScope.launch{
            val currentUser = auth.currentUser!!.uid
            val post = postById(postId).await().toObject(Post::class.java)
            val isLiked = post!!.likedBy.contains(currentUser)

            if (isLiked){
                post.likedBy.remove(currentUser)
            }else{
                post.likedBy.add(currentUser)
            }
            collection.document(postId).set(post)
        }
    }
}