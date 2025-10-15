package com.ssu.portfolio.chattingapp.model

import com.google.firebase.Timestamp

data class ChatMessageModel(
    var message: String? = null,
    var senderId: String? = null,
    var timestamp: Timestamp? = null
) {
    constructor() : this(null, null, null)
}