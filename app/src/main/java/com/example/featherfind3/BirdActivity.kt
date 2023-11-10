package com.example.featherfind3

import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.featherfind3.databinding.ActivityBirdBinding
import com.google.gson.Gson
import java.io.IOException

class BirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityBirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Now, you can set the listener for the bottomNavigationView using data binding
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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
                    true // You're already in the avian activity
                }
                else -> false
            }
        }

        val jsonFileName = "data.json"
        val json: String? = readJsonFileFromAssets(jsonFileName)

        if (json != null) {
            val birdDataList = Gson().fromJson(json, Array<BirdDataItemItem>::class.java)
            val recyclerView: RecyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = BirdAdapter(birdDataList.toList())
        }
    }

    private fun readJsonFileFromAssets(fileName: String): String? {
        val assetManager: AssetManager = assets
        val json: String
        try {
            val inputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }
}
