package com.ssu.portfolio.chattingapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.ssu.portfolio.chattingapp.data.User
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.auth.FirebaseAuth
import com.ssu.portfolio.chattingapp.model.ChatroomModel

class AndroidUtils {
    companion object {

        fun currentUserId(): String {
            return FirebaseAuth.getInstance().uid ?: ""
        }

        // ADDED FUNCTION TO SET PROFILE PICTURE USING GLIDE
        fun setProfilePic(context: Context?, imageUri: Uri?, imageView: ImageView) {
            if (context == null || imageUri == null) return

            Glide.with(context).load(imageUri).into(imageView)
        }
        // END OF ADDED FUNCTION

        fun passUserModelAsIntent(intent: Intent, user: User) {
            intent.putExtra("username", user.username)
            intent.putExtra("email", user.email)
            intent.putExtra("userId", user.uid)
        }

        fun getUserIdFromIntent(intent: Intent): String? {
            return intent.getStringExtra("userId")
        }

        fun getUsernameFromIntent(intent: Intent): String? {
            return intent.getStringExtra("username")
        }

        fun getEmailIdFormIntent(intent: Intent): String?{
            return intent.getStringExtra("email")
        }

        fun timestampToString(timestamp: Timestamp?): String {
            if (timestamp == null) return "..."
            return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(timestamp.toDate())
        }

        fun passChatRoomModelAsIntent(intent: Intent, chatroomModel: ChatroomModel) {
            intent.putExtra("chatroomId", chatroomModel.chatroomId)
            intent.putStringArrayListExtra("userIds", chatroomModel.userIds?.let { ArrayList(it) })
        }

        fun getChatRoomIdFromIntent(intent: Intent): String? {
            return intent.getStringExtra("chatroomId")
        }

    }
}