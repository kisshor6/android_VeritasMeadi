package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.daos.PostDao
import com.example.chatapplication.databinding.ActivityGetUserBinding
import com.example.chatapplication.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class getUser : AppCompatActivity(), IPostListener {

    private lateinit var binding: ActivityGetUserBinding
    private lateinit var postDao: PostDao
    private  var auth = FirebaseAuth.getInstance()
    private var firestore =  FirebaseFirestore.getInstance()
    private lateinit var postArrayList : ArrayList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolBar)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, createPost::class.java)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        setUpRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecyclerView() {
        postDao = PostDao()
        postArrayList = ArrayList()
        val postCollection = postDao.collection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        query.addSnapshotListener { snapshot, error ->
            if (error != null){
                return@addSnapshotListener
            }
            postArrayList.clear()

            if (snapshot != null){
                for (document in snapshot.documents){
                    val post : Post? = document.toObject(Post::class.java)
                    if (post != null){
                        val postItem = Post(post.text, post.createdBy, post.createdAt, post.likedBy, document.id)
                        postArrayList.add(postItem)
                    }
                }
                val adapter = PostAdapter(postArrayList, this)
                adapter.notifyDataSetChanged()
                binding.recyclerView.adapter = adapter
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.deleteData -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure you want to LogOut")
                builder.setPositiveButton("logout") { dialog, which ->
                    logout()
                }
                builder.setNegativeButton("cancel") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        auth.signOut()
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        firestore.clearPersistence()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}