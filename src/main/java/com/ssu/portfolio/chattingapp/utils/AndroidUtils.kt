package com.ssu.portfolio.chattingapp.utils

import android.content.Context
import android.content.Intent
import com.ssu.portfolio.chattingapp.data.User

class AndroidUtils {
    companion object {

        fun passUserModelAsIntent(intent: Intent, user: User) {
            intent.putExtra("username", user.username)
            intent.putExtra("email", user.email)
            intent.putExtra("userId", user.uid)
        }

        fun getUserIdFromIntent(intent: Intent): String? {
            return intent.getStringExtra("userId")
        }

        fun getUsernameFromIntent(intent: Intent): String? {
            return intent.getStringExtra("username")
        }

        fun getEmailIdFormIntent(intent: Intent): String?{
            return intent.getStringExtra("email")
        }
    }
}
