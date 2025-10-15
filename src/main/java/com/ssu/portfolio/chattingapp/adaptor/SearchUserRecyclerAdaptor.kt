package com.ssu.portfolio.chattingapp.adaptor

import android.content.Intent
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.ssu.portfolio.chattingapp.ChatActivity
import com.ssu.portfolio.chattingapp.R
import com.ssu.portfolio.chattingapp.data.User
import com.ssu.portfolio.chattingapp.utils.AndroidUtils

class SearchUserRecyclerAdaptor :
    RecyclerView.Adapter<SearchUserRecyclerAdaptor.UserModelViewHolder>() {

    private val userList = mutableListOf<User>()

    class UserModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username_recycleview)
        val email: TextView = itemView.findViewById(R.id.emailid_recycleview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserModelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_user_layout_row, parent, false)
        return UserModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int) {
        val user = userList[position]
        holder.username.text = user.username ?: "Unknown User"
        holder.email.text = user.email ?: "No Email"


        //user Click the Message You are Send
        holder.itemView.setOnClickListener {
            val context= holder.itemView.context
            val intent = Intent(context, ChatActivity::class.java)
            AndroidUtils.passUserModelAsIntent(intent,user)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}