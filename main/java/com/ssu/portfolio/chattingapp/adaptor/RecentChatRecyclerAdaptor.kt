package com.ssu.portfolio.chattingapp.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.ssu.portfolio.chattingapp.ChatActivity
import com.ssu.portfolio.chattingapp.R
import com.ssu.portfolio.chattingapp.model.ChatroomModel
import com.ssu.portfolio.chattingapp.utils.AndroidUtils  
import com.ssu.portfolio.chattingapp.utils.FireBaseUtils

class RecentChatRecyclerAdaptor(
    options: FirestoreRecyclerOptions<ChatroomModel>,
    private val context: Context
) : FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdaptor.ChatRoomModelViewHolder>(options) {

    class ChatRoomModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otherUsername: TextView = itemView.findViewById(R.id.username_recycleview)
        val lastMessageText: TextView = itemView.findViewById(R.id.last_message_text)
        val lastMessageTime: TextView = itemView.findViewById(R.id.last_message_time_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomModelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_chat_recycler_row, parent, false)
        return ChatRoomModelViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatRoomModelViewHolder,
        position: Int,
        model: ChatroomModel
    ) {
        val otherUserId = model.userIds?.filter { it != FireBaseUtils.currentUserId() }?.getOrNull(0)

        if (otherUserId != null) {
            FireBaseUtils.getUserNameById(otherUserId) { username ->
                if (holder.absoluteAdapterPosition == position) {
                    if (username != null) {
                        holder.otherUsername.text = username
                    } else {
                        holder.otherUsername.text = "Unknown User"
                    }
                }
            }
        } else {
            holder.otherUsername.text = "No User Found"
        }

        holder.lastMessageText.text = model.lastMessage ?: "Say Hi to start chatting"
        holder.lastMessageTime.text = AndroidUtils.timestampToString(model.lastMessageTimestamp)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            AndroidUtils.passChatRoomModelAsIntent(intent, model)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}

