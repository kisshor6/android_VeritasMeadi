package com.example.chatapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.chatapplication.daos.PostDao

class createPost : AppCompatActivity() {

    private lateinit var postDao: PostDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        val postBtn = findViewById<Button>(R.id.postBtn)
        val postContent = findViewById<EditText>(R.id.postContent)

        postBtn.setOnClickListener {
            val input = postContent.text.toString().trim()
            if (input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
        }
    }
}