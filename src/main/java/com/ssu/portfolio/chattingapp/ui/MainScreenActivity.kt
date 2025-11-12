package com.ssu.portfolio.chattingapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssu.portfolio.chattingapp.R

class MainScreenActivity : AppCompatActivity() {

    private lateinit var chatFragment: ChatFragement
    private lateinit var profileFragment: ProfileFragement
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)

        // Initialize fragments
        chatFragment = ChatFragement()
        profileFragment = ProfileFragement()

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        searchButton = findViewById(R.id.btnSearch)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, chatFragment)
                .commit()
            bottomNavigationView.selectedItemId = R.id.menu_chat
        }

        setupBottomNavigation()
        setupSearchButton()
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, chatFragment)
                        .commit()
                    true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            startActivity(Intent(this, Search_User_Activity::class.java))
        }
    }
}
