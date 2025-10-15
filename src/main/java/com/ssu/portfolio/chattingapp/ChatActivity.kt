package com.ssu.portfolio.chattingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.ssu.portfolio.chattingapp.adapter.ChatRecyclerAdapter
import com.ssu.portfolio.chattingapp.model.ChatMessageModel
import com.ssu.portfolio.chattingapp.model.ChatRoomModel
import com.ssu.portfolio.chattingapp.utils.AndroidUtils
import com.ssu.portfolio.chattingapp.utils.FireBaseUtils

class ChatActivity : AppCompatActivity() {
    private lateinit var usernameSetToChat: TextView
    private lateinit var chatMessageInput: EditText
    private lateinit var sendMessageButton: ImageButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var backButton: ImageButton

    private lateinit var chatPartnerUserId: String
    private lateinit var chatroomId: String
    private var chatRoomModel: ChatRoomModel? = null

    private lateinit var chatrecyclerAdapter: ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        initViews()
        getChatPartnerData()
        setupBackToChat()
        createChatroomAndGetModel()
        setupSendMessageButton()
    }

    private fun initViews() {
        usernameSetToChat = findViewById(R.id.tvChatPartnerName)
        chatMessageInput = findViewById(R.id.etMessageInput)
        sendMessageButton = findViewById(R.id.btnSend)
        recyclerview = findViewById(R.id.recycleviewchatmessage)
        backButton = findViewById(R.id.btn_Back)
    }

    private fun getChatPartnerData(){
        chatPartnerUserId = AndroidUtils.getUserIdFromIntent(intent) ?: ""
        val otherUsernameString = AndroidUtils.getUsernameFromIntent(intent) ?: "User"

        usernameSetToChat.text = otherUsernameString
    }

    private fun createChatroomAndGetModel(){
        val currentUserId = FireBaseUtils.currentUserId()

        if (currentUserId.isBlank() || chatPartnerUserId.isBlank()) {
            Log.e("ChatActivity", "User IDs are missing, cannot create chatroom.")
            return
        }

        chatroomId = FireBaseUtils.getChatRoomId(currentUserId, chatPartnerUserId)

        FireBaseUtils.getChatRoomReference(chatroomId).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                chatRoomModel = task.result?.toObject(ChatRoomModel::class.java)

                if (chatRoomModel == null) {
                    chatRoomModel = ChatRoomModel(
                        chatroomId = chatroomId,
                        userIds = listOf(currentUserId, chatPartnerUserId),
                        lastMessageTimestamp = Timestamp.now(),
                        lastMessageSenderId = "",
                        lastMessage = ""
                    )

                    FireBaseUtils.getChatRoomReference(chatroomId).set(chatRoomModel!!)
                        .addOnSuccessListener {
                            SetUpChatRecyclerView()
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatActivity", "FAILED to create chatroom in Firestore: ${e.message}", e)
                        }
                } else {
                    SetUpChatRecyclerView()
                }
            } else{
                Log.e("ChatActivity", "Failed to fetch chatroom document: ${task.exception?.message}")
            }
        }
    }

    fun setupSendMessageButton(){
        sendMessageButton.setOnClickListener {
            val message = chatMessageInput.text.toString().trim()
            if(message.isEmpty()){
                return@setOnClickListener
            }else{
                sendMessageToUser(message)
                chatMessageInput.setText("")
            }
        }
    }

    private fun sendMessageToUser(message: String){
        if (chatRoomModel == null){
            Log.e("ChatActivity", "ChatRoomModel is NULL, cannot send message. Check Firestore fetch result.")
            return
        }

        val currentTimestamp = Timestamp.now()

        chatRoomModel!!.lastMessageTimestamp = currentTimestamp
        chatRoomModel!!.lastMessageSenderId = FireBaseUtils.currentUserId()
        chatRoomModel!!.lastMessage = message
        FireBaseUtils.getChatRoomReference(chatroomId).set(chatRoomModel!!)

        val chatMessageModel = ChatMessageModel(
            message = message,
            senderId = FireBaseUtils.currentUserId(),
            timestamp = currentTimestamp
        )

        FireBaseUtils.getChatRoomReference(chatroomId)
            .collection("chats")
            .add(chatMessageModel)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("ChatActivity", "FAILED to send message to Firestore: ${task.exception?.message}")
                }
            }
    }

    fun SetUpChatRecyclerView(){
        val query = FireBaseUtils.getChatRoomReference(chatroomId)
            .collection("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java).build()

        chatrecyclerAdapter = ChatRecyclerAdapter(options, this)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true

        recyclerview.layoutManager = manager
        recyclerview.adapter = chatrecyclerAdapter

        chatrecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                recyclerview.smoothScrollToPosition(0)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if(::chatrecyclerAdapter.isInitialized) chatrecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(::chatrecyclerAdapter.isInitialized) chatrecyclerAdapter.stopListening()
    }

    fun setupBackToChat(){
        backButton.setOnClickListener {
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }
    }
}