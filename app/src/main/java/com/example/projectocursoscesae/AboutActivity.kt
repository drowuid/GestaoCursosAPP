package com.example.projectocursoscesae

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton // Import MaterialButton

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)

        // Find the "Voltar" button by its ID
        val btnBack: MaterialButton = findViewById(R.id.btnBack)

        // Set a click listener for the button
        btnBack.setOnClickListener {
            // When the "Voltar" button is clicked, finish this activity
            // This will navigate back to the previous screen
            finish()
        }

    }
}