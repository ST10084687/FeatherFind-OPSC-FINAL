package com.example.featherfind3
import android.content.Context
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var distanceUnitSpinner: Spinner
    private lateinit var maximumDistanceEditText: EditText
    private lateinit var saveSettingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views
        distanceUnitSpinner = findViewById(R.id.distanceUnitSpinner)
        maximumDistanceEditText = findViewById(R.id.maximumDistanceEditText)
        saveSettingsButton = findViewById(R.id.saveSettingsButton)

        // Populate the spinner with distance unit options (Miles and Kilometers)
        val distanceUnitOptions = arrayOf("Miles", "Kilometers")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, distanceUnitOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        distanceUnitSpinner.adapter = adapter

        // Load and set the saved settings, if available
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val savedDistanceUnit = sharedPreferences.getString("distanceUnit", "Miles")
        val savedMaximumDistance = sharedPreferences.getFloat("maximumDistance", 10f)

        // Set the spinner and edit text to the saved values
        val distanceUnitPosition = distanceUnitOptions.indexOf(savedDistanceUnit)
        distanceUnitSpinner.setSelection(distanceUnitPosition)
        maximumDistanceEditText.setText(savedMaximumDistance.toString())

        // Handle spinner item selection
        distanceUnitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Handle the selected distance unit (Miles or Kilometers)
                // You can store this value in SharedPreferences or another storage method.
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Handle Save Settings button click
        saveSettingsButton.setOnClickListener {
            // Inside the method where you save the settings (e.g., saveSettingsButton click listener)
            val selectedDistanceUnit = distanceUnitOptions[distanceUnitSpinner.selectedItemPosition]
            val maximumDistanceString = maximumDistanceEditText.text.toString()
            val maximumDistance = maximumDistanceString.toFloatOrNull()

            // Convert the maximum distance to the selected unit (Miles or Kilometers)
            val convertedMaximumDistance = when (selectedDistanceUnit) {
                "Miles" -> maximumDistance ?: 0f
                "Kilometers" -> (maximumDistance ?: 0f) * 1.60934f // 1 Mile = 1.60934 Kilometers
                else -> maximumDistance ?: 0f
            }

            // Store these values in SharedPreferences or another persistent storage method
            val editor = sharedPreferences.edit()
            editor.putString("distanceUnit", selectedDistanceUnit)
            editor.putFloat("maximumDistance", convertedMaximumDistance)
            editor.apply()
            finish() // Close the SettingsActivity after saving settings
        }
    }
}
