package com.example.myapplication

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*

class LocalGame : AppCompatActivity() {

    lateinit var button : Button

    private var playerName = ""
    private var roomName = ""
    var role = ""
    var massage = ""

    private lateinit var database: FirebaseDatabase
    lateinit var massageRef: DatabaseReference
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_game)

        button = findViewById(R.id.poke)
        button.isEnabled = false

        database = FirebaseDatabase.getInstance()

        sharedPref= getSharedPreferences("myPref", 0)
        playerName = sharedPref.getString("PlayerName", "").toString()

        val extras: Bundle? = intent.extras
        if (extras != null) {
            roomName = extras.getString("roomName").toString()
            role = if (roomName == playerName)
                "host"
            else
                "guest"
        }


        button.setOnClickListener {
            button.isEnabled = false
            massage = "$role:Poked"
            massageRef.setValue(massage)
        }

        massageRef = database.getReference("rooms/$roomName/massage")
        massage = "$role:Poked"
        massageRef.setValue(massage)
        addRoomEventListener()
    }

    private fun addRoomEventListener() {
        massageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (role == "host") {
                    if (snapshot.getValue(String::class.java)!!.contains("guest:")) {
                        button.isEnabled = true
                        Toast.makeText(this@LocalGame,
                            snapshot.getValue(String::class.java)!!.replace("guest:", ""), Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    if (snapshot.getValue(String::class.java)!!.contains("host:")) {
                        button.isEnabled = true
                        Toast.makeText(this@LocalGame,
                            snapshot.getValue(String::class.java)!!.replace("host:", ""), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                massageRef.setValue(massage)
            }
        })
    }
}