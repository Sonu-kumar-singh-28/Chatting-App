package com.ssu.portfolio.chattingapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssu.portfolio.chattingapp.adaptor.SearchUserRecyclerAdaptor
import com.ssu.portfolio.chattingapp.data.User
import com.ssu.portfolio.chattingapp.databinding.ActivitySearchUserBinding

class Search_User_Activity : AppCompatActivity() {

    private val binding: ActivitySearchUserBinding by lazy {
        ActivitySearchUserBinding.inflate(layoutInflater)
    }

    private lateinit var adaptor: SearchUserRecyclerAdaptor
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Setup RecyclerView
        adaptor = SearchUserRecyclerAdaptor()
        binding.searchUserRecycleView.layoutManager = LinearLayoutManager(this)
        binding.searchUserRecycleView.adapter = adaptor

        binding.btnback.setOnClickListener {
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }

        binding.searchUserNameBtn.setOnClickListener {
            val searchTerm = binding.searchUsernameInputField.editText?.text.toString().trim()
            if (searchTerm.isEmpty() || searchTerm.length < 2) {
                FancyToast.makeText(
                    this,
                    "Invalid Username or Email ❌",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            } else {
                fetchUsersFromFirestore(searchTerm)
            }
        }
    }

    private fun fetchUsersFromFirestore(searchTerm: String) {
        val term = searchTerm.lowercase()

        // 1. Username Query
        val usernameQuery = firestore.collection("users")
            .whereGreaterThanOrEqualTo("username_lowercase", term)
            .whereLessThanOrEqualTo("username_lowercase", term + "\uf8ff")
            .get()

        // 2. Email Query
        val emailQuery = firestore.collection("users")
            .whereGreaterThanOrEqualTo("email_lowercase", term)
            .whereLessThanOrEqualTo("email_lowercase", term + "\uf8ff")
            .get()


        Tasks.whenAllSuccess<QuerySnapshot>(usernameQuery, emailQuery)
            .addOnSuccessListener { results ->
                val users = results.flatMap { snapshot ->

                    snapshot.documents.mapNotNull { doc -> doc.toObject(User::class.java) }
                }
                    .distinctBy { it.uid }

                if (users.isEmpty()) {
                    FancyToast.makeText(
                        this,
                        "No users found ⚠️",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        false
                    ).show()
                }
                adaptor.updateData(users)
            }
            .addOnFailureListener { e ->
                FancyToast.makeText(
                    this,
                    "Error fetching users: ${e.message}",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
    }

    fun UserClickPersonToChat(){

    }
}