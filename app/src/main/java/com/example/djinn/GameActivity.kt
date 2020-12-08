package com.example.djinn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val currentGame : Game? = Game.getGame(
            intent.getIntExtra(GAME, -1)
        )

        //Set Game info
        if (currentGame != null) {
            currentGame.visitorScore.let {
                (findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
            }
            Rivalry.getRivalry(currentGame.rivalry)?.visitorPlayer?.let {
                (findViewById<View>(R.id.initials_visitor) as TextView).text =
                    Player.getPlayer(it)?.initials
            }
            currentGame.homeScore.let {
                (findViewById<View>(R.id.score_home) as TextView).text = it.toString()
            }
            Rivalry.getRivalry(currentGame.rivalry)?.homePlayer?.let {
                (findViewById<View>(R.id.initials_home) as TextView).text =
                    Player.getPlayer(it)?.initials
            }

            currentGame.number.let {
                (findViewById<View>(R.id.game_title) as TextView).text = "Game" + it.toString()
            }

            //Set up partial games listview
            val listView : ListView = findViewById<ListView>(R.id.listview_games)
            val adapter = PartialGameAdapter(this, currentGame.partialGames)
            listView.adapter = adapter
        }
    }

}