package com.ssu.portfolio.chattingapp.utils

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Locale

object FireBaseUtils {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }


    fun currentUserId(): String {
        return auth.uid ?: ""
    }


    fun getChatRoomReference(chatroomId: String): DocumentReference {
        return firestore.collection("chatrooms").document(chatroomId)
    }


    fun getChatRoomId(UserId1: String, UserId2: String): String {
        return if (UserId1.hashCode() < UserId2.hashCode()) {
            "${UserId1}_${UserId2}"
        } else {
            "${UserId2}_${UserId1}"
        }
    }

    fun getChatRoomMessageReference(chatroomId: String): CollectionReference {
        return getChatRoomReference(chatroomId).collection("chats")
    }

    fun AllchatRoomCollectionsRefrence(): CollectionReference {
        return firestore.collection("chatrooms")
    }

    fun getUserNameById(userId: String, callback: (String?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")
                callback(username)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getOtherProfilePicStorageRef(otherUserId: String): StorageReference {
        return storage.reference.child("profile_pic").child(otherUserId)
    }


    fun timestampToString(timestamp: Timestamp?): String {
        return timestamp?.let {
            val date = it.toDate()
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(date)
        } ?: ""
    }
}
