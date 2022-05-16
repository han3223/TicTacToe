package com.example.myapplication

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class LocalGame: AppCompatActivity() {
    private val game: Game = GameImp()
    private lateinit var fieldRv: RecyclerView

    lateinit var button : Button
    private var playerName = ""
    private var roomName = ""
    var role = ""

    var testRow: Int = -1
    var testCol: Int = -1

    var listRow = mutableListOf<Int>()
    var listCol = mutableListOf<Int>()

    private lateinit var database: FirebaseDatabase
    lateinit var moveRef: DatabaseReference
    lateinit var rowRef: DatabaseReference
    lateinit var colRef: DatabaseReference
    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_game)
        fieldRv = findViewById(R.id.fieldRv)

        button = findViewById(R.id.poke)
        button.isEnabled = false

        database = FirebaseDatabase.getInstance()

        sharedPref= getSharedPreferences("myPref", 0)
        playerName = sharedPref.getString("PlayerName", "").toString()



        val extras: Bundle? = intent.extras
        if (extras != null) {
            roomName = extras.getString("roomName").toString()
            role = if (roomName == playerName){
                "host"
            }
            else
                "guest"
        }

        rowRef = database.getReference("rooms/$roomName/row")
        colRef = database.getReference("rooms/$roomName/col")
        moveRef = database.getReference("rooms/$roomName/move")
        moveRef.setValue(3)

        var moveBool: Boolean
        if (role == "host")
            moveBool = true
        else
            moveBool = false


        move(moveBool)

        /*if (role == "host") {
            fieldRv.layoutManager = GridLayoutManager(fieldRv.context, game.field.size)
            fieldRv.adapter = FieldAdapter(game.field, true) { row, col ->
                rowRef.setValue(row)
                colRef.setValue(col)
                addRoomEventListener()
                moveRef.setValue(1)
                if (game.actMultiplayerGame(row, col, game.userMark)) {
                    fieldRv.adapter?.notifyDataSetChanged()
                }
            }
        }

        if (role == "guest") {
            fieldRv.layoutManager = GridLayoutManager(fieldRv.context, game.field.size)
            fieldRv.adapter = FieldAdapter(game.field, true) { row, col ->
                rowRef.setValue(row)
                colRef.setValue(col)
                addRoomEventListener()
                moveRef.setValue(0)
                if (game.actMultiplayerGame(row, col, game.userMark)) {
                    fieldRv.adapter?.notifyDataSetChanged()
                }
            }
        }*/

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun move(bool: Boolean) {
        var k = bool
        if (!bool) {
            val n = listRow.size
            rowRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(Int::class.java)?.let { listRow.add(it) }
                    if (listRow.size == n)
                        move(!bool)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            colRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(Int::class.java)?.let { listCol.add(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            if (listRow.size > 0) {
                for (i in 0 until listRow.size) {
                    game.actMultiplayerGame(listRow[i], listCol[i], true)
                }
                k = true
            }
        }

        //запись данных в массив
        //отрисовка
        //добавление

        fieldRv.layoutManager = GridLayoutManager(fieldRv.context, game.field.size)
        fieldRv.adapter = FieldAdapter(game.field, k) { row, col ->
            rowRef.setValue(row)
            colRef.setValue(col)
            addRoomEventListener()
            if (game.actMultiplayerGame(row, col, game.userMark)) {
                fieldRv.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun addRoomEventListener() {
        rowRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testRow = snapshot.getValue(Int::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        colRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                testCol = snapshot.getValue(Int::class.java)!!

                move(false)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}