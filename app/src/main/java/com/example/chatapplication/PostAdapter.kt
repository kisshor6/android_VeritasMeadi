package com.example.chatapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.databinding.ActivityCreatePostBinding
import com.example.chatapplication.databinding.PostitemBinding
import com.example.chatapplication.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(private val postItem : ArrayList<Post>, private val listener: IPostListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding : PostitemBinding = PostitemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.postitem, parent, false))

        return view
    }

    override fun getItemCount(): Int {
        return postItem.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postItem = postItem[position]
        holder.binding.post.text = postItem.text
        holder.binding.userName.text = postItem.createdBy.userName
        Glide.with(holder.binding.userImage.context).load(postItem.createdBy.imageUrl).circleCrop().into(holder.binding.userImage)
        holder.binding.likeCount.text = postItem.likedBy.size.toString()
        holder.binding.uploadedDate.text = Utils.getImageAgo(postItem.createdAt)

        holder.binding.likeImg.setOnClickListener {
            listener.onLikeClicked(postItem.postId)
        }

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = postItem.likedBy.contains(currentUserId)
        if (isLiked){
            holder.binding.likeImg.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeImg.context, R.drawable.love))
        }else{
            holder.binding.likeImg.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeImg.context, R.drawable.heart))
        }
    }
}

interface IPostListener {
    fun onLikeClicked(postId : String)
}