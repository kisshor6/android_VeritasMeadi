package com.example.chatapplication.daos

import com.example.chatapplication.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun addUser(users: Users?){
        users?.let {
            GlobalScope.launch ( Dispatchers.IO ){
                userCollection.document(users.uid).set(it)
            }
        }
    }
    fun getUserByID(uid : String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }
}