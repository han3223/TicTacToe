package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.database.*

class GlobalMenu : AppCompatActivity() {

    private lateinit var playerName: String

    private lateinit var button: Button
    private lateinit var editText: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var playerRef: DatabaseReference
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_global_menu)

        editText = findViewById(R.id.editTextTextPersonName)
        button = findViewById(R.id.buttonStart)

        database = FirebaseDatabase.getInstance()

        sharedPref = getSharedPreferences("myPref", 0)
        playerName = sharedPref.getString("playerName", "").toString()

        if (playerName != "") {
            playerRef = database.getReference("players/$playerName")
            addEventListener()
            playerRef.setValue("")
        }

        // Проверка на пользователя, если он есть, то переход в выбор типа игры
        binding.apply {
            val userName = sharedPref.getString("PlayerName", null)
            if (userName != null) {
                startActivity(Intent(this@GlobalMenu, GameTypeSelection::class.java))
            }
        }

        // При нажатии кнопки пользователь регистрируется и данные отправляются в базу данных в реальном времени Firebase
        button.setOnClickListener {
            playerName = editText.text.toString()
            editText.setText("")
            if (playerName != "") {
                button.text = "LOGGING IN"
                button.isEnabled = false
                playerRef = database.getReference("players/$playerName")
                addEventListener()
                playerRef.setValue("")
            }
        }
    }

    private fun addEventListener() {
        playerRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CommitPrefEdits")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (playerName != "") {
                    binding.apply {
                        sharedPref = getSharedPreferences("myPref", 0)
                        val editor = sharedPref.edit()
                        editor.apply {
                            putString("PlayerName", playerName)
                            apply()
                        }

                        startActivity(Intent(applicationContext, GameTypeSelection::class.java))
                        finish()
                    }

                }
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                button.text = "LOG IN"
                button.isEnabled = true
                Toast.makeText(this@GlobalMenu, "ERROR", Toast.LENGTH_LONG).show()
            }

        })
    }
}