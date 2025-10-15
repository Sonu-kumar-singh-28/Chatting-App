package com.ssu.portfolio.chattingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.ssu.portfolio.chattingapp.R
import com.ssu.portfolio.chattingapp.model.ChatMessageModel
import com.ssu.portfolio.chattingapp.utils.FireBaseUtils

class ChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatMessageModel>,
    val context: Context
) : FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder>(options) {

    class ChatModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftChatLayout: LinearLayout = view.findViewById(R.id.left_chat_layout)
        val rightChatLayout: LinearLayout = view.findViewById(R.id.right_chat_layout)
        val leftChatTextview: TextView = view.findViewById(R.id.left_chat_textview)
        val rightChatTextview: TextView = view.findViewById(R.id.right_chat_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatModelViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.chat_message_recycler_row, parent, false)
        return ChatModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatModelViewHolder, position: Int, model: ChatMessageModel) {
        val currentUserId = FireBaseUtils.currentUserId()

        holder.leftChatLayout.visibility = View.GONE
        holder.rightChatLayout.visibility = View.GONE

        if (model.senderId == currentUserId) {
            holder.rightChatLayout.visibility = View.VISIBLE
            holder.rightChatTextview.text = model.message
        } else {
            // Incomplete code fix
            holder.leftChatLayout.visibility = View.VISIBLE
            holder.leftChatTextview.text = model.message
        }
    }
}