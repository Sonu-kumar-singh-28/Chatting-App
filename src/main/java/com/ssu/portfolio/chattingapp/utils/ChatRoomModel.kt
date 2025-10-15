package com.ssu.portfolio.chattingapp.utils
import com.google.firebase.Timestamp

data class ChatRoomModel(

    var chatroomId: String? = null,
    var userIds: List<String>? = null,
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderId: String? = null
) {
    constructor() : this(null, null, null, null)
}
