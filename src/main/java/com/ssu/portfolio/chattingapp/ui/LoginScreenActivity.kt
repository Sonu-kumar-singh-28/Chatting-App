package com.ssu.portfolio.chattingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssu.portfolio.chattingapp.R
import com.ssu.portfolio.chattingapp.databinding.ActivityLoginScreenBinding

class LoginScreenActivity : AppCompatActivity() {
    private val binding: ActivityLoginScreenBinding  by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        // SignupButton Functions
        SignupButtonClick()

        // Login Register User
        LoginUserFunction()

        // UserAlready Login
        UserAlreadyLogin()
    }
    fun SignupButtonClick(){
        val signupbutton = findViewById<TextView>(R.id.signupOption)
        signupbutton.setOnClickListener {
            startActivity(Intent(this, RegisterScreenActivity::class.java))
            finish()
        }
    }

    fun LoginUserFunction(){
        val loginButton = findViewById<Button>(R.id.loginButton)
        binding.loginButton.setOnClickListener {
            val email = binding.emailtextfield.editText?.text?.toString()?.trim().orEmpty()
            val password = binding.passwordInputField.editText?.text?.toString()?.trim().orEmpty()

            if (email.isEmpty() || password.isEmpty()) {
                FancyToast.makeText(this, "Please fill all fields",
                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FancyToast.makeText(this, "Login successful",
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, MainScreenActivity::class.java))
                            finish()
                        } else {
                            FancyToast.makeText(this, "Error: ${task.exception?.message}",
                                FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                        }
                    }
            }
        }
    }

    fun UserAlreadyLogin(){
        val currentuser = auth.currentUser
        if(currentuser!=null){
            FancyToast.makeText(this, "Welcome back ${currentuser.email}",
                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }
    }
}