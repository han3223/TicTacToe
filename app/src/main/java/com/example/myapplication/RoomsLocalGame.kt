package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import java.util.ArrayList

class RoomsLocalGame : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var button: Button
    private lateinit var roomsList: ArrayList<String>
    var playerName = ""
    var roomName = ""
    private lateinit var database: FirebaseDatabase
    lateinit var roomRef: DatabaseReference
    private lateinit var roomsRef: DatabaseReference
    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_local_game)

        database = FirebaseDatabase.getInstance()

        sharedPref = getSharedPreferences("myPref", 0)
        playerName = sharedPref.getString("PlayerName", "").toString()
        roomName = playerName

        listView = findViewById(R.id.listView)
        button = findViewById(R.id.createRoom)

        roomsList = ArrayList<String>()

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                button.text = "CREATING ROOM"
                button.isEnabled = false
                roomName = playerName
                roomRef = database.getReference("rooms/$roomName/player1")
                addRoomEventListener()
                roomRef.setValue(playerName)
            }
        })

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                roomName = roomsList[p2]
                roomRef = database.getReference("rooms/$roomName/player2")
                addRoomEventListener()
                roomRef.setValue(playerName)
            }

        }
        addRoomsEventListener()
    }

    private fun addRoomEventListener() {
        roomRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                button.text = "CREATE ROOM"
                button.isEnabled = true
                val intent = Intent(applicationContext, LocalGame::class.java)
                intent.putExtra("roomName", roomName)
                startActivity(intent)
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                button.text = "CREATE ROOM"
                button.isEnabled = true
                Toast.makeText(this@RoomsLocalGame, "ERROR", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun addRoomsEventListener() {
        roomsRef = database.getReference("rooms")
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomsList.clear()
                val rooms: Iterable<DataSnapshot> = snapshot.children
                for (snap: DataSnapshot in rooms) {
                    roomsList.add(snap.key.toString())

                    val adapter: ArrayAdapter<String> = ArrayAdapter(this@RoomsLocalGame, android.R.layout.simple_list_item_1, roomsList)
                    listView.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}