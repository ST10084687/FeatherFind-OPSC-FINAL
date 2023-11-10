package com.example.featherfind3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.featherfind3.databinding.ActivityFeatherEdBinding

class FeatherEd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feather_ed)

        val binding = ActivityFeatherEdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the "Play Avian Quest" button by ID
        val playAvianQuestButton = binding.root.findViewById<Button>(R.id.button)

        //OnClickListener for the button
                playAvianQuestButton.setOnClickListener {
                    // Create an Intent to start the FunFactsActivity
                    val intent = Intent(this, FunFactsActivity::class.java)
                    startActivity(intent)
                }




// bottomNavigationView using data binding
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_education -> true

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