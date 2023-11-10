package com.example.featherfind3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class FunFactsActivity : AppCompatActivity() {
    private val funFactsManager = FunFactsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun_facts)

        val earnFeatherButton = findViewById<Button>(R.id.earnFeatherButton)
        val funFactTextView = findViewById<TextView>(R.id.funFactTextView)

        earnFeatherButton.setOnClickListener {
            funFactsManager.earnFeather()
            updateFunFact(funFactTextView)
        }

        updateFunFact(funFactTextView)
    }

    private fun updateFunFact(textView: TextView) {
        textView.text = funFactsManager.getRandomFunFact()
    }
}
