package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectLevelDifficulty : AppCompatActivity() {

    private lateinit var easy: Button
    private lateinit var normal: Button
    private lateinit var hard: Button
    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_level_difficulty)

        easy = findViewById(R.id.easy)
        normal = findViewById(R.id.normal)
        hard = findViewById(R.id.hard)
        back = findViewById(R.id.back)

        // При нажатии запускает лёгкую игру с ботом
        easy.setOnClickListener { startActivity(Intent(this, SinglePlayerEasy::class.java)) }
        // При нажатии запускает нормальную игру с ботом
        normal.setOnClickListener { startActivity(Intent(this, SinglePlayerNormal::class.java)) }
        // При нажатии запускает сложную игру с ботом
        hard.setOnClickListener { startActivity(Intent(this, SinglePlayerHard::class.java)) }
        // При нажатии возвращает обратно
        back.setOnClickListener { startActivity(Intent(this, GameTypeSelection::class.java)) }
    }
}