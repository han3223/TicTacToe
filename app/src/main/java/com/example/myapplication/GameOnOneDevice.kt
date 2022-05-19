package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameOnOneDevice : AppCompatActivity() {

    private val game: Game = GameImp()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Отображение и взаимодейтвие с полем
        findViewById<RecyclerView>(R.id.fieldRv).apply {
            layoutManager = GridLayoutManager(context, game.field.size)
            adapter = FieldAdapter(game.field, true) { row, col ->
                if (game.actGameOnOneDevice(row, col)) {
                    adapter?.notifyDataSetChanged()
                    if (game.isFinished) {
                        val winnerText: String = when (game.winner) {
                            true -> "The winner is Player 1"
                            false -> "The winner is Player 2"
                            else -> "Tie"
                        }

                        visibleEndBlock(winnerText)
                    }
                    checkColorText()
                }
            }
        }

        // При нажатии начинает игру заново
        findViewById<Button>(R.id.restart).setOnClickListener {
            startActivity(Intent(this, GameOnOneDevice::class.java))
        }
        // При нажатии переходит к выбору типа игры
        findViewById<Button>(R.id.back).setOnClickListener {
            startActivity(Intent(this, GameTypeSelection::class.java))
        }
    }

    // Функция помогает определить какой игрок ходит путём смены цвета и размера текста
    private fun checkColorText() {
        val playerOne = findViewById<TextView>(R.id.playerOne)
        val playerTwo = findViewById<TextView>(R.id.playerTwo)
        if (playerOne.currentTextColor == Color.BLACK) {
            playerOne.setTextColor(Color.parseColor("#424242"))
            playerOne.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            playerTwo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
            playerTwo.setTextColor(Color.BLACK)
        } else {
            playerOne.setTextColor(Color.BLACK)
            playerOne.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
            playerTwo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            playerTwo.setTextColor(Color.parseColor("#424242"))
        }

    }

    // Функция отображает блок конца игры
    private fun visibleEndBlock(winner: String) {
        findViewById<TextView>(R.id.winnerTextView).text = winner
        val endBlock = findViewById<RelativeLayout>(R.id.endBlock)
        if (endBlock.visibility == View.GONE)
            endBlock.visibility = View.VISIBLE
    }
}