package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding

class GameTypeSelection : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game_type_selection)

        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)

        binding.apply {
            val userName = sharedPref.getString("user_name", null)
            findViewById<TextView>(R.id.user_name).text = "Hello, $userName"
        }



        findViewById<Button>(R.id.gameOnOneDevice).setOnClickListener {
            val intent = Intent(this, GameOnOneDevice::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.singlePlayer).setOnClickListener {
            val intent = Intent(this, SinglePlayer::class.java)
            startActivity(intent)
        }
    }
}