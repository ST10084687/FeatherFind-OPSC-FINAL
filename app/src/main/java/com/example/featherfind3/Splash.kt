package com.example.featherfind3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.droidsonroids.gif.GifImageView
import android.media.MediaPlayer

class Splash : AppCompatActivity() {
    private lateinit var image : GifImageView
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize MediaPlayer with the audio file from resources
        mediaPlayer = MediaPlayer.create(this, R.raw.birdsong)

        image = findViewById(R.id.gifImageView)
        image.alpha = 0f
        image.animate().setDuration(2300).alpha(1f).withEndAction {
            val intent = Intent(this, Login::class.java)

            // Start playing the audio
            mediaPlayer?.start()

            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer when your activity is destroyed
        mediaPlayer?.release()
    }
}
