package com.example.featherfind3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.featherfind3.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)



// Now, you can set the listener for the bottomNavigationView using data binding
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_notifications -> true

                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.navigation_dashboard -> {
                    startActivity(Intent(applicationContext, SaveBirdObservation::class.java))
                    finish()
                    true
                }

                R.id.navigation_notifications -> {
                    startActivity(Intent(applicationContext, AboutUs::class.java))
                    finish()
                    true
                }
                R.id.navigation_education -> {
                    startActivity(Intent(applicationContext, FeatherEd::class.java))
                    finish()
                    true
                }
                R.id.navigation_avian -> {
                    startActivity(Intent(applicationContext, BirdActivity::class.java))
                    finish()
                    true
                }



                else -> false
            }
        }



    }
}