package com.example.chronokan.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var currentUser by mutableStateOf(auth.currentUser)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // 1. SIGN UP FUNCTION
    fun signUp(email: String, password: String, onResult: (String?) -> Unit) {
        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    onResult(null)
                } else {
                    onResult(task.exception?.message ?: "Sign Up Failed")
                }
            }
    }

    // 2. SIGN IN FUNCTION
    fun signIn(email: String, password: String, onResult: (String?) -> Unit) {
        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    onResult(null)
                } else {
                    onResult(task.exception?.message ?: "Login Failed")
                }
            }
    }

    // 3. GOOGLE SIGN IN FUNCTION
    fun signInWithGoogle(idToken: String, onResult: (String?) -> Unit) {
        isLoading = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    onResult(null)
                } else {
                    onResult(task.exception?.message ?: "Google Sign-in Failed")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        currentUser = null
    }
}