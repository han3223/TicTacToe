package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding

class SinglePlayerNormal : AppCompatActivity() {

    private val game: Game = GameImp()

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_single_player_normal)

        val sharedPref = getSharedPreferences("myPref", 0)

        // Загрузка из памяти данных о имени игрока и отображение в тексте
        binding.apply {
            val userName = sharedPref.getString("PlayerName", null)
            findViewById<TextView>(R.id.userNameNormal).text = "$userName vs Normal Bot"
        }
        // Отображение поля и взаимодействие с ним
        findViewById<RecyclerView>(R.id.fieldRv).apply {
            layoutManager = GridLayoutManager(context, game.field.size)
            adapter = FieldAdapter(game.field, true) { row, col ->
                if (game.actSingleGameNormal(row, col)) {
                    adapter?.notifyDataSetChanged()
                    if (game.isFinished) {
                        binding.apply {
                            val userName = sharedPref.getString("PlayerName", null)
                            val winnerText: String = when (game.winner) {
                                game.userMark -> "The winner is $userName"
                                !game.userMark -> "The winner is Bot"
                                else -> "Tie"
                            }
                            visibleEndBlock(winnerText)
                        }
                    }
                }
            }
        }
        // При нажатии запускает игру заново
        findViewById<Button>(R.id.restartNormal).setOnClickListener {
            startActivity(Intent(this, SinglePlayerNormal::class.java))
        }
        // При нажатии возвращает в выбор типа игры
        findViewById<Button>(R.id.backNormal).setOnClickListener {
            startActivity(Intent(this, GameTypeSelection::class.java))
        }
    }

    // Функция отображает блок конца игры
    private fun visibleEndBlock(winner: String) {
        findViewById<TextView>(R.id.winnerTextViewNormal).text = winner
        val endBlock = findViewById<RelativeLayout>(R.id.endBlockNormal)
        if (endBlock.visibility == View.GONE)
            endBlock.visibility = View.VISIBLE
    }
}