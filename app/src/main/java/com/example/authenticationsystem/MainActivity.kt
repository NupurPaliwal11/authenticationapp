package com.example.authenticationsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val logoutButton: Button = findViewById(R.id.button3)
        logoutButton.setOnClickListener {
            // Perform logout
            FirebaseAuth.getInstance().signOut()

            // Navigate back to the login activity
            SessionManager(this).clearSession()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            //startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // Close this activity to prevent the user from coming back using the back button
        }
    }
}