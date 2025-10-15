package com.ssu.portfolio.chattingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreenActivity : AppCompatActivity() {

    private lateinit var chatFragement: ChatFragement
    private lateinit var profileFragement: ProfileFragement
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)

        chatFragement = ChatFragement()
        profileFragement = ProfileFragement()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        searchButton = findViewById(R.id.btnSearch)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, chatFragement)
                .commit()
            bottomNavigationView.selectedItemId = R.id.menu_chat
        }

        CallChatFragements()

        searchButtonActivity()
    }

    fun CallChatFragements() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, chatFragement)
                        .commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, profileFragement)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }


    fun searchButtonActivity(){
        searchButton.setOnClickListener {
            startActivity(Intent(this, Search_User_Activity::class.java))
            finish()
        }
    }
}