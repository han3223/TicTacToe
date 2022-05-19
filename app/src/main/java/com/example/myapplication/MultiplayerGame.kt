package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlin.properties.Delegates

class MultiplayerGame : AppCompatActivity() {

    private val game: Game = GameImp()
    private lateinit var fieldRv: RecyclerView

    private var role = ""
    private var massage = ""
    private var roomName = ""
    private var playerName = ""

    private var buffRow: Int = -1
    private var buffCol: Int = -1

    private var buffMark: Boolean = false
    private var mark by Delegates.notNull<Boolean>()
    private var clickField by Delegates.notNull<Boolean>()

    private lateinit var button: Button
    private lateinit var player: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var massageRef: DatabaseReference
    private lateinit var rowRef: DatabaseReference
    private lateinit var colRef: DatabaseReference
    private lateinit var markRef: DatabaseReference
    private lateinit var sharedPref: SharedPreferences


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_game)

        button = findViewById(R.id.poke)
        button.isEnabled = false
        fieldRv = findViewById(R.id.fieldRv)

        database = FirebaseDatabase.getInstance()

        player = findViewById(R.id.playerOne)

        sharedPref = getSharedPreferences("myPref", 0)
        playerName = sharedPref.getString("PlayerName", "").toString()

        // Определение типа пользователя
        val extras: Bundle? = intent.extras
        if (extras != null) {
            roomName = extras.getString("roomName").toString()
            role = if (roomName == playerName)
                "host"
            else
                "guest"
        }

        rowRef = database.getReference("rooms/$roomName/row")
        colRef = database.getReference("rooms/$roomName/col")
        markRef = database.getReference("rooms/$roomName/mark")
        massageRef = database.getReference("rooms/$roomName/massage")

        if (role == "host") {
            player.text = "You turn"
        } else {
            player.text = "Enemy makes a move"
        }
        player.visibility = View.VISIBLE
        clickField = role == "host"
        mark = role == "host"

        field(clickField, mark)

        checkMove()
        // При нажатии отображается ход противника и включается поле для хода
        button.setOnClickListener {
            button.isEnabled = false
            field(true, mark)
            paint(buffRow, buffCol, !mark)
        }

        // При нажатии переходит в выбор типа игры
        findViewById<Button>(R.id.back).setOnClickListener {
            startActivity(Intent(this, GameTypeSelection::class.java))
        }
    }

    // Функция с полем
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun field(clickField: Boolean, userMark: Boolean) {
        fieldRv.layoutManager = GridLayoutManager(fieldRv.context, game.field.size)
        fieldRv.adapter = FieldAdapter(game.field, clickField) { row, col ->
            rowRef.setValue(row)
            colRef.setValue(col)
            markRef.setValue(game.userMark)
            if (game.actMultiplayerGame(row, col, userMark)) {
                fieldRv.adapter?.notifyDataSetChanged()
                if (game.isFinished) {
                    if (game.tie)
                        visibleEndBlock("Tie")
                    else
                        visibleEndBlock("You Win")
                }
                if (role == "host") {
                    massageRef.setValue(true)
                    player.text = "Enemy makes a move"
                } else {
                    massageRef.setValue(false)
                    player.text = "Enemy makes a move"
                }

            }
            field(!clickField, userMark)
        }
    }

    // Функция отображения хода противника
    @SuppressLint("NotifyDataSetChanged")
    private fun paint(n: Int, m: Int, k: Boolean) {
        game.actMultiplayerGame(n, m, k)
        fieldRv.adapter?.notifyDataSetChanged()
        if (game.isFinished) {
            if (game.tie)
                visibleEndBlock("Tie")
            else
                visibleEndBlock("You Lose")
        }
    }

    // Функция позволяет получить данные из базы данных для дальнейшего использования
    private fun getRowCol() {
        colRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                buffCol = snapshot.getValue(Int::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        rowRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                buffRow = snapshot.getValue(Int::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        markRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                buffMark = snapshot.getValue(Boolean::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Функция проверяет чей ход и включает кнопку для отображения хода другого пользователя
    private fun checkMove() {
        massageRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Boolean::class.java) == true && role == "guest") {
                    button.isEnabled = true
                    player.text = "Your turn"
                    getRowCol()
                } else if (snapshot.getValue(Boolean::class.java) == false && role == "host") {
                    button.isEnabled = true
                    player.text = "Your turn"
                    getRowCol()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                massageRef.setValue(massage)
            }
        })
    }

    // Функция отображает блок конца игры
    private fun visibleEndBlock(winner: String) {
        findViewById<TextView>(R.id.winnerTextView).text = winner
        val endBlock = findViewById<RelativeLayout>(R.id.endBlock)
        if (endBlock.visibility == View.GONE)
            endBlock.visibility = View.VISIBLE
    }

}