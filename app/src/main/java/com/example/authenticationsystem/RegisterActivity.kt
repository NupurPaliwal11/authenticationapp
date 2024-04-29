package com.example.authenticationsystem

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneNumberEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        fullNameEditText = findViewById(R.id.editTextText)
        emailEditText = findViewById(R.id.editTextText2)
        passwordEditText = findViewById(R.id.editTextText3)
        phoneNumberEditText = findViewById(R.id.editTextText4)

        val registerButton: Button = findViewById(R.id.button)
        registerButton.setOnClickListener {
            registerUser()
        }

        val loginTextView: TextView = findViewById(R.id.textView3)
        loginTextView.setOnClickListener {
            // Navigate to LoginActivity
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.error = "Please enter your full name"
            fullNameEditText.requestFocus()
            return
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Please enter your email"
            emailEditText.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Please enter a password"
            passwordEditText.requestFocus()
            return
        }

        if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.error = "Please enter your phone number"
            phoneNumberEditText.requestFocus()
            return
        }

        // Register the user using Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, update UI or navigate to login activity
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    // Registration failed, display an error message
                    Toast.makeText(this@RegisterActivity, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}