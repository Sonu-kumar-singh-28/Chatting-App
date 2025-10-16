package com.ssu.portfolio.chattingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import com.ssu.portfolio.chattingapp.data.User
import com.ssu.portfolio.chattingapp.databinding.ActivityRegisterScreenBinding

class RegisterScreenActivity : AppCompatActivity() {

    private val binding: ActivityRegisterScreenBinding by lazy {
        ActivityRegisterScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        alreadyRegister()
        userRegisterButton()
    }

    private fun alreadyRegister() {
        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginScreenActivity::class.java))
            finish()
        }
    }

    private fun userRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val username = binding.nametextfield.editText?.text?.toString()?.trim().orEmpty()
            val useremail = binding.emailtextfield.editText?.text?.toString()?.trim().orEmpty()
            val userpassword = binding.passwordtextfield.editText?.text?.toString()?.trim().orEmpty()
            val userrepeatpassword = binding.repeattextfield.editText?.text?.toString()?.trim().orEmpty()

            when {
                username.isEmpty() || useremail.isEmpty() || userpassword.isEmpty() || userrepeatpassword.isEmpty() -> {
                    FancyToast.makeText(this, "TextFields are Empty",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
                userpassword != userrepeatpassword -> {
                    FancyToast.makeText(this, "Passwords are not same",
                        FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show()
                }
                else -> {
                    auth.createUserWithEmailAndPassword(useremail, userpassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = task.result?.user
                                val uid = firebaseUser?.uid

                                if (uid != null) {
                                    saveUserDataToFirestore(uid, username, useremail)
                                } else {
                                    handleRegistrationError(Exception("Auth successful but user UID is null."))
                                }
                            } else {
                                handleRegistrationError(task.exception)
                            }
                        }
                }
            }
        }
    }

    //  Data saving logic for Firestore
    private fun saveUserDataToFirestore(uid: String, username: String, email: String) {
        val newUser = User(
            uid = uid,
            username = username,
            email = email,
            username_lowercase = username.lowercase(),
            email_lowercase = email.lowercase()
        )

        firestore.collection("users")
            .document(uid)
            .set(newUser)
            .addOnSuccessListener {
                // Successful Auth and Firestore save
                FancyToast.makeText(this, "Registered Successfully",
                    FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                startActivity(Intent(this, LoginScreenActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                auth.currentUser?.delete()
                handleRegistrationError(e)
            }
    }

    private fun handleRegistrationError(e: Exception?) {
        FancyToast.makeText(this,
            "Error: ${e?.message}",
            FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show()
    }
}