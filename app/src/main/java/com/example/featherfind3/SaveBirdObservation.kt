package com.example.featherfind3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.featherfind3.databinding.ActivitySaveBirdObservationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.concurrent.Executor

class SaveBirdObservation : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 100 // Request code for location permission
    private lateinit var birdName: String
    private lateinit var birdSpecies: String
    private lateinit var currentLocation: Location
    private lateinit var saveObservationButton: Button
    private lateinit var birdNameEditText: EditText
    private lateinit var birdSpeciesEditText: EditText
    private lateinit var cameraCaptureButton: Button
    private lateinit var birdImageView: ImageView // Changed to ImageView

    // CameraX variables
    private lateinit var cameraExecutor: Executor
    private lateinit var imageCapture: ImageCapture
    private var imageCaptureFile: File? = null
    private lateinit var previewView: PreviewView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_bird_observation)


        val binding = ActivitySaveBirdObservationBinding.inflate(layoutInflater)
        setContentView(binding.root)


// Now, you can set the listener for the bottomNavigationView using data binding
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> true

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


        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        birdNameEditText = findViewById(R.id.birdNameEditText)
        birdSpeciesEditText = findViewById(R.id.birdSpeciesEditText)
        saveObservationButton = findViewById(R.id.saveObservationButton)
        //cameraCaptureButton = findViewById(R.id.captureImageButton)
        // Replace the following line
// cameraCaptureButton = findViewById(R.id.captureImageButton)

// with this line
        val captureImageButton = findViewById<Button>(R.id.captureImageButton)

        birdImageView = findViewById(R.id.birdImage)
        previewView = findViewById(R.id.cameraPreview)

        // Initialize CameraX
        cameraExecutor = ContextCompat.getMainExecutor(this)
        imageCapture = ImageCapture.Builder().build()

        // Request camera and location permissions
        requestCameraPermission()

        // Find the "View Observations" button by its ID
        val viewObservationsButton = findViewById<Button>(R.id.viewObservationsButton)

        // Set an OnClickListener for the button
        viewObservationsButton.setOnClickListener {
            // Start the ViewBirdObservations activity
            val intent = Intent(this, ViewBirdObservations::class.java)
            startActivity(intent)
        }

        saveObservationButton.setOnClickListener {
            birdName = birdNameEditText.text.toString()
            birdSpecies = birdSpeciesEditText.text.toString()

            if (birdName.isNotEmpty() && birdSpecies.isNotEmpty()) {
                // Get the current location
                getCurrentLocation()
            }
        }

        captureImageButton.setOnClickListener {
            captureImage(imageCapture)
        }

    }

    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA
        if (ActivityCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        // Camera permission granted, setup the camera
                        setupCamera()
                    } else {
                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            requestPermissionLauncher.launch(permission)
        } else {
            setupCamera()
        }
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, cameraExecutor)
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        val preview = Preview.Builder().build()

        // Bind the cameraProvider to the lifecycle
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        // Attach the preview to the PreviewView
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    private fun getCurrentLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = it
                        // Save the bird observation (bird name, species, and location)
                        saveBirdObservation(birdName, birdSpecies, currentLocation)
                    }
                }
        } else {
            // Request location permission from the user
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    private fun captureImage(imageCapture: ImageCapture) {
        val file = createImageFile()
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = file.toUri()
                    runOnUiThread {
                        birdImageView.setImageURI(savedUri)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@SaveBirdObservation,
                            "Image capture failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir("bird_images")
        val imageFile = File.createTempFile("bird_image_", ".jpg", storageDir)
        imageCaptureFile = imageFile
        return imageFile
    }

    private fun saveBirdObservation(birdName: String, birdSpecies: String, location: Location) {
        // Access a Cloud Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Create a new observation object with the provided data
        val observation = hashMapOf(
            "birdName" to birdName,
            "birdSpecies" to birdSpecies,
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "imagePath" to imageCaptureFile?.absolutePath
        )

        // Add a new document with a generated ID
        db.collection("observations")
            .add(observation)
            .addOnSuccessListener {
                Toast.makeText(this, "Observation saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save observation: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}


