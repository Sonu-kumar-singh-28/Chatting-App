package com.ssu.portfolio.chattingapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ssu.portfolio.chattingapp.data.User

class FireBaseUtils {
    companion object {

        fun currentUserId(): String {
            return FirebaseAuth.getInstance().uid ?: ""
        }

        fun getChatRoomReference(chatroomId: String): DocumentReference {
            return FirebaseFirestore.getInstance()
                .collection("chatrooms")
                .document(chatroomId)
        }

        fun getChatRoomId(UserId1: String, UserId2: String): String {
            return if (UserId1.hashCode() < UserId2.hashCode()) {
                UserId1 + "_" + UserId2
            } else {
                UserId2 + "_" + UserId1
            }
        }

        fun getChatRoomMessageReference(chatroomId: String): CollectionReference {
            return getChatRoomReference(chatroomId)
                .collection("chats")
        }
    }
}