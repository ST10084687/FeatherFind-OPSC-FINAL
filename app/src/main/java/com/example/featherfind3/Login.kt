package com.example.featherfind3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

        companion object {

        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var emailAddress: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    private lateinit var mAuth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        mAuth = FirebaseAuth.getInstance()


        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            signIn()
        }




        // Initialize views and Firebase Authentication
        emailAddress = findViewById(R.id.EmailAddress)
        password = findViewById(R.id.Password)
        loginButton = findViewById(R.id.LoginButton)
        mAuth = FirebaseAuth.getInstance()

        // Set click listener for login button
        loginButton.setOnClickListener {
            loginUser()
        }

        // Set click listener for "Sign up" text
        val textViewClickable = findViewById<TextView>(R.id.ForgotPassword)
        textViewClickable.setOnClickListener {
            // Navigate to registration activity
            val intent = Intent(this@Login, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Login.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser() {
        try {
            val email = emailAddress.text.toString().trim()
            val passwords = password.text.toString().trim()

            // Validate email and password
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter an email address!", Toast.LENGTH_SHORT).show()
                emailAddress.requestFocus()
                return
            }
            if (TextUtils.isEmpty(passwords)) {
                Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show()
                password.requestFocus()
                return
            }

            // Sign in user with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, passwords).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to Home activity
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login unsuccessful, display error message
                    Toast.makeText(this, "Login Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()
                    emailAddress.setText("")
                    password.setText("")
                    emailAddress.requestFocus()
                }
            }
        } catch (eer: Exception) {
            // Handle and display any exceptions that occur during login
            Toast.makeText(this, "Error Occurred: " + eer.message, Toast.LENGTH_SHORT).show()
        }
    }

    // Function to handle "SignUp" button click
    fun regScreen(view: View) {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }


}