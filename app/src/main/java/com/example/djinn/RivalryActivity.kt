package com.example.djinn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class RivalryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rivalry)
        val currentRivalry : Rivalry? = Rivalry.getRivalry(
                intent.getIntExtra(RIVALRY, -1)
            )

        //Set Rivalry info
        currentRivalry?.visitorScore?.let {
            (findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        currentRivalry?.visitorPlayer?.let {
            (findViewById<View>(R.id.initials_visitor) as TextView).text =
                Player.getPlayer(it)?.initials
            (findViewById<View>(R.id.name_visitor) as TextView).text =
                Player.getPlayer(it)?.name
        }
        currentRivalry?.homeScore?.let {
            (findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        currentRivalry?.homePlayer?.let {
            (findViewById<View>(R.id.initials_home) as TextView).text =
                Player.getPlayer(it)?.initials
            (findViewById<View>(R.id.name_home) as TextView).text =
                Player.getPlayer(it)?.name
        }

        //Set up games listview
        if (currentRivalry != null) {
            val listView: ListView = findViewById<ListView>(R.id.listview_games)
            val adapter = GameAdapter(
                this,
                currentRivalry.games.reversed() as ArrayList<Game>
            )
            listView.adapter = adapter
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedGameId = view.tag.toString().toInt()
                    val intent = Intent(this, GameActivity::class.java).apply {
                        putExtra(GAME, selectedGameId)
                    }
                    startActivity(intent)
                }
        }
    }
}