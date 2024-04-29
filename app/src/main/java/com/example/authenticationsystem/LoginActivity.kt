package com.example.authenticationsystem

import android.content.ContentProviderClient
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.authenticationsystem.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var sessionManager: SessionManager
    private lateinit var rememberMeCheckBox: CheckBox
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        emailEditText = findViewById(R.id.editTextText5)
        passwordEditText = findViewById(R.id.editTextText6)
        sessionManager = SessionManager(this)
        rememberMeCheckBox = findViewById(R.id.checkBox)
//        googleAuth= findViewById(R.id.button4)
//        val auth = FirebaseAuth.getInstance()
//        val database = FirebaseDatabase.getInstance()

        if (sessionManager.isLoggedIn()) {
            // If user is logged in, navigate to MainActivity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish() // Finish LoginActivity to prevent going back when pressing back button
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Set up Google Sign-In client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set click listener for sign-in button
        findViewById<Button>(R.id.button4).setOnClickListener {
            signIn()
        }
        val loginButton: Button = findViewById(R.id.button2)
        loginButton.setOnClickListener {
            loginUser()
        }

        val createAccountTextView: TextView = findViewById(R.id.textView4)
        createAccountTextView.setOnClickListener {
            // Navigate to RegisterActivity
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

        private fun signIn() {
            mAuth.signOut()
            mGoogleSignInClient.signOut()
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                    Log.d(TAG, "sign in successfull")
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI accordingly
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

        private fun firebaseAuthWithGoogle(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            Log.d("Tag", "signInWithCredential:success")
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth.currentUser

                        Toast.makeText(this@LoginActivity, "Login successfull", Toast.LENGTH_SHORT).show()
                        // Navigate to MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish() // Finish LoginActivity to prevent going back when pressing back button
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        // Update UI
                    }
                }
        }


//
//        val loginButton: Button = findViewById(R.id.button2)
//        loginButton.setOnClickListener {
//            loginUser()
//        }
//
//        val createAccountTextView: TextView = findViewById(R.id.textView4)
//        createAccountTextView.setOnClickListener {
//            // Navigate to RegisterActivity
//            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//            finish()
//        }


    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Please enter your email"
            emailEditText.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Please enter a password"
            passwordEditText.requestFocus()
            return
        }

        // Login the user using Firebase Authentication
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (rememberMeCheckBox.isChecked) {
                        sessionManager.setLoggedIn(true)
                    }
                    // Login success, save the session
                    sessionManager.saveAuthToken(email)// Save session with email
                    // Login success, update UI or navigate to main activity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    // Login failed, display an error message
                    Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {
        private const val TAG = "GoogleSignIn"
    }

}
