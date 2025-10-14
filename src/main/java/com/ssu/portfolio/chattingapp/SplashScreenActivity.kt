package com.ssu.portfolio.chattingapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        // Call Splash Screen Function
        startSplashScreen()

        // Animations of the Splash Screen
        AnimationSplashScreen()
    }

    private fun startSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, 1800)
    }

    fun AnimationSplashScreen(){
        val glowupAnimation = AnimationUtils.loadAnimation(this,R.anim.glowup_animation)
        val Appnname = findViewById<TextView>(R.id.txt_appName)
        val AppImage =  findViewById<ImageView>(R.id.imageView)
        val Tagline = findViewById<TextView>(R.id.txt_tagline)
        val Sponsored = findViewById<TextView>(R.id.txt_sponsored)

        Appnname.startAnimation(glowupAnimation)
        AppImage.startAnimation(glowupAnimation)
        Tagline.startAnimation(glowupAnimation)
        Sponsored.startAnimation(glowupAnimation)
    }
}
