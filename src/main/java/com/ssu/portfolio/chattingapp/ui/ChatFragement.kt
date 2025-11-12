package com.ssu.portfolio.chattingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.ssu.portfolio.chattingapp.R
import com.ssu.portfolio.chattingapp.adaptor.RecentChatRecyclerAdaptor
import com.ssu.portfolio.chattingapp.model.ChatroomModel
import com.ssu.portfolio.chattingapp.utils.FireBaseUtils

class ChatFragement : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: RecentChatRecyclerAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_fragement, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        val query: Query = FireBaseUtils.AllchatRoomCollectionsRefrence()
            .whereArrayContains("userIds", FireBaseUtils.currentUserId())
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<ChatroomModel>()
            .setQuery(query, ChatroomModel::class.java)
            .build()

        adapter = RecentChatRecyclerAdaptor(options, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onResume() {
        super.onResume()
    }
}