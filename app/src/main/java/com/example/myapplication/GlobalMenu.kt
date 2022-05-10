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

    private lateinit var binding: ActivityMainBinding

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var playerRef : DatabaseReference
    private lateinit var sharedPref: SharedPreferences
    lateinit var playerName: String

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

        //val editor = sharedPref.edit()

        /*binding.apply {
            val userName = sharedPref.getString("user_name", null)
            if (userName == "" || userName == null) {
                button.setOnClickListener {
                    if (editText.text.toString() == "") {
                        Toast.makeText(applicationContext, "Please enter your nickname", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this@GlobalMenu, GameTypeSelection::class.java)
                        startActivity(intent)
                        val name = editText.text.toString()

                        editor.apply {
                            putString("user_name", name)
                            apply()
                        }
                    }
                }
            }else{
                val intent = Intent(this@GlobalMenu, GameTypeSelection::class.java)
                startActivity(intent)
            }
        }*/
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