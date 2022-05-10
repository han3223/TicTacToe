package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding

class GlobalMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_global_menu)

        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.apply {
            val userName = sharedPref.getString("user_name", null)
            if (userName == "" || userName == null) {
                findViewById<Button>(R.id.buttonStart).setOnClickListener {
                    if (findViewById<EditText>(R.id.editTextTextPersonName).text.toString() == "") {
                        Toast.makeText(applicationContext, "Please enter your nickname", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this@GlobalMenu, GameTypeSelection::class.java)
                        startActivity(intent)
                        val name = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()

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
        }
    }
}