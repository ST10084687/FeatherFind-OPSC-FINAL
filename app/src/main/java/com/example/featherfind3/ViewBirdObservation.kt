package com.example.featherfind3

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class ViewBirdObservations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bird_observation)

        val observationsContainer = findViewById<LinearLayout>(R.id.observationsContainer)

        // Access a Cloud Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Query the "observations" collection in Firebase Firestore
        db.collection("observations")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val birdName = document.getString("birdName")
                    val birdSpecies = document.getString("birdSpecies")
                    val latitude = document.getDouble("latitude")
                    val longitude = document.getDouble("longitude")
                    val imagePath = document.getString("imagePath")

                    val observationView = layoutInflater.inflate(R.layout.observation_item, null) as RelativeLayout
                    val imageView = observationView.findViewById<ImageView>(R.id.observationImageView)
                    val textView = observationView.findViewById<TextView>(R.id.observationTextView)

                    // Set the observation text
                    textView.text = "Bird Name: $birdName\nBird Species: $birdSpecies\nLocation: ($latitude, $longitude)"

                    // Load and display the image if imagePath is not null
                    if (imagePath != null) {
                        val imageFile = File(imagePath)
                        if (imageFile.exists()) {
                            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                            imageView.setImageBitmap(imageBitmap)
                        }
                    }

                    observationsContainer.addView(observationView)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                exception.printStackTrace()
            }
    }
}
