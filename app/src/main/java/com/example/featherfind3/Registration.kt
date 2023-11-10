package com.example.featherfind3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Registration : AppCompatActivity() {


    private lateinit var firstName: EditText
    private lateinit var surname: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var regEmail: EditText
    private lateinit var regPassword: EditText
    private lateinit var regSignUp: Button
    private lateinit var regGoogle: Button
    private lateinit var regLogin: Button

    private lateinit var mAuth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        firstName = findViewById(R.id.FirstName)
        surname = findViewById(R.id. Surname)
        phoneNumber = findViewById(R.id.phoneNumber)
        regEmail = findViewById(R.id.regEmail)
        regPassword = findViewById(R.id.regPassword)
        regSignUp = findViewById(R.id.regSignUp)
        regGoogle = findViewById(R.id.regGoogle)
        regLogin = findViewById(R.id.regLogin)

        mAuth = FirebaseAuth.getInstance()

        regSignUp.setOnClickListener {
            registerUser()
        }
    }
    fun registerUser() {
        val email = regEmail.text.toString().trim()
        val userPassword = regPassword.text.toString().trim()

        val userFirstName = firstName.text.toString().trim()
        val userLastName = surname.text.toString().trim()
        val userPhoneNumber = phoneNumber.text.toString().trim()

        //val confirmPassword = regPassword.text.toString().trim()
        if (TextUtils.isEmpty(userFirstName)) {
            Toast.makeText(this, "Please enter your First Name!", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(userLastName)) {
            Toast.makeText(this, "Please enter your Last Name!", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(userPhoneNumber)) {
            Toast.makeText(this, "Please enter your Phone Number!", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show()
            return
        }
        //
        // if (TextUtils.isEmpty(confirmPassword)) {
        //   Toast.makeText(this, "Please confirm the password!", Toast.LENGTH_SHORT).show()
        //    return
        // }

        // if (userPassword != confirmPassword) {
        //   Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
        // return
        //}
        if (userPassword != null) {
            mAuth.createUserWithEmailAndPassword(email, userPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Registration, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Registration Unsuccessful! Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
    fun loginScreen(view: View)

    {
        val intent = Intent(this, Login::class.java)

        startActivity(intent)

    }
}