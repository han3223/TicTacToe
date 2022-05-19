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

class SinglePlayerHard : AppCompatActivity() {

    private val game: Game = GameImp()

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_single_player_hard)

        val sharedPref = getSharedPreferences("myPref", 0)

        // Загрузка из памяти данных о имени игрока и отображение в тексте
        binding.apply {
            val userName = sharedPref.getString("PlayerName", null)
            findViewById<TextView>(R.id.user_name).text = "$userName vs Hard Bot"
        }

        // Отображение поля и взаимодействие с ним
        findViewById<RecyclerView>(R.id.fieldRv).apply {
            layoutManager = GridLayoutManager(context, game.field.size)
            adapter = FieldAdapter(game.field, true) { row, col ->
                if (game.actSingleGameHard(row, col)) {
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
        findViewById<Button>(R.id.restartHard).setOnClickListener {
            startActivity(Intent(this, SinglePlayerHard::class.java))
        }
        // При нажатии возвращает в выбор типа игры
        findViewById<Button>(R.id.backHard).setOnClickListener {
            startActivity(Intent(this, GameTypeSelection::class.java))
        }
    }

    // Функция отображает блок конца игры
    private fun visibleEndBlock(winner: String) {
        findViewById<TextView>(R.id.winnerTextViewHard).text = winner
        val endBlock = findViewById<RelativeLayout>(R.id.endBlockHard)
        if (endBlock.visibility == View.GONE)
            endBlock.visibility = View.VISIBLE
    }
}