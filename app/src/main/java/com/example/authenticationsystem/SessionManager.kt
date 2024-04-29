package com.example.authenticationsystem


import android.content.Context
import android.content.SharedPreferences
import android.media.session.MediaSession.Token

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SessionPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveAuthToken(token: String) {
        editor.putString("authToken", token)
        editor.apply()
    }
    companion object {
        private const val KEY_LOGGED_IN = "isLoggedIn"
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("authToken", null)
    }

    fun saveRememberMe(rememberMe: Boolean) {
        editor.putBoolean("rememberMe", rememberMe)
        editor.apply()
    }

    fun getRememberMe(): Boolean {
        return sharedPreferences.getBoolean("rememberMe", false)
    }

    fun saveEmail(email: String) {
        editor.putString("email", email)
        editor.apply()
    }

    fun getSavedEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    fun clearSavedEmail() {
        editor.remove("email")
        editor.apply()
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }

}