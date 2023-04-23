package com.example.chatapplication.models

class Post(
    val text: String = "",
    val createdBy: Users = Users(),
    val createdAt : Long = 0L,
    val likedBy : ArrayList<String> = ArrayList(),
    val postId : String = ""
)