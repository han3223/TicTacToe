package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class RoomsMultiplayerGame : AppCompatActivity() {

    private var playerName = ""
    private var roomName = ""

    private lateinit var roomsList: ArrayList<String>

    private lateinit var button: Button
    private lateinit var back: Button

    private lateinit var listView: ListView

    private lateinit var database: FirebaseDatabase
    private lateinit var roomRef: DatabaseReference
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
        back = findViewById(R.id.back)

        roomsList = ArrayList()

        button.setOnClickListener {
            button.text = "CREATING ROOM"
            button.isEnabled = false
            roomName = playerName
            roomRef = database.getReference("rooms/$roomName/player1")
            addRoomEventListener()
            roomRef.setValue(playerName)
        }

        // При нажатии возвращает пользователя обратно
        back.setOnClickListener { startActivity(Intent(this, GameTypeSelection::class.java)) }

        // При нажатии на комнату записывает пользователя в бд
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ ->
                roomName = roomsList[p2]
                roomRef = database.getReference("rooms/$roomName/player2")
                addRoomEventListener()
                roomRef.setValue(playerName)
            }
        addRoomsEventListener()
    }

    private fun addRoomEventListener() {
        roomRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                button.text = "CREATE ROOM"
                button.isEnabled = true
                val intent = Intent(applicationContext, MultiplayerGame::class.java)
                intent.putExtra("roomName", roomName)
                startActivity(intent)
            }

            @SuppressLint("SetTextI18n")
            override fun onCancelled(error: DatabaseError) {
                button.text = "CREATE ROOM"
                button.isEnabled = true
                Toast.makeText(this@RoomsMultiplayerGame, "ERROR", Toast.LENGTH_LONG).show()
            }

        })
    }

    // Функция отображения комнат
    private fun addRoomsEventListener() {
        roomsRef = database.getReference("rooms")
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomsList.clear()
                val rooms: Iterable<DataSnapshot> = snapshot.children
                for (snap: DataSnapshot in rooms) {
                    roomsList.add(snap.key.toString())

                    val adapter: ArrayAdapter<String> = ArrayAdapter(
                        this@RoomsMultiplayerGame,
                        android.R.layout.simple_list_item_1,
                        roomsList
                    )
                    listView.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}