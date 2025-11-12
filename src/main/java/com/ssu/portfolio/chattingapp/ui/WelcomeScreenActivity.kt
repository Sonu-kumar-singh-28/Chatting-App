package com.ssu.portfolio.chattingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ssu.portfolio.chattingapp.R

class WelcomeScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_screen)
        //  Get Started Button
        WelcometoLogin()
    }

    fun WelcometoLogin(){
        val getstartedbutton = findViewById<Button>(R.id.getstartedbutton)
        getstartedbutton.setOnClickListener {
            startActivity(Intent(this, LoginScreenActivity::class.java))
            finish()
        }
    }
}