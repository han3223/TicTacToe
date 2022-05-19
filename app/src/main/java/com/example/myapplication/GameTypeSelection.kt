package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding

class GameTypeSelection : AppCompatActivity() {

    private lateinit var buttonGameOnOneDevice: Button
    private lateinit var buttonSinglePlayer: Button
    private lateinit var buttonLocalGame: Button
    private lateinit var userName: TextView

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game_type_selection)

        buttonGameOnOneDevice = findViewById(R.id.gameOnOneDevice)
        buttonSinglePlayer = findViewById(R.id.singlePlayer)
        userName = findViewById(R.id.user_name)
        buttonLocalGame = findViewById(R.id.localGame)

        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)

        // Получение данных о имени пользователя из памяти и отображение в виде приветствия
        binding.apply {
            val userName = sharedPref.getString("PlayerName", null)
            findViewById<TextView>(R.id.user_name).text = "Hello, $userName"
        }

        // При нажатии переходит в игру на одном устройстве
        buttonGameOnOneDevice.setOnClickListener {
            val intent = Intent(this, GameOnOneDevice::class.java)
            startActivity(intent)
        }
        // При нажатии переходит в выбор уровня сложности для одиночной игры
        buttonSinglePlayer.setOnClickListener {
            val intent = Intent(this, SelectLevelDifficulty::class.java)
            startActivity(intent)
        }
        // При нажатии переходи в создание комныты или подключение
        buttonLocalGame.setOnClickListener {
            val intent = Intent(this, RoomsMultiplayerGame::class.java)
            startActivity(intent)
        }
    }
}