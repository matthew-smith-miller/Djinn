package com.example.djinn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class RivalryActivity : AppCompatActivity() {
    private var currentRivalry: Rivalry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rivalry)

        currentRivalry = Rivalry.getRivalry(
            intent.getIntExtra(RIVALRY, -1)
        )

        Log.d("RivalryActivity", currentRivalry?.id.toString())

        setRivalryInfo()
        setGameListView()
        setAddGameButton()
    }

    override fun onResume() {
        super.onResume()

        setRivalryInfo()
        setGameListView()
        setAddGameButton()
    }

    override fun onContentChanged() {
        super.onContentChanged()
        findViewById<ListView>(R.id.listview_games).emptyView =
            findViewById(R.id.listview_games_empty_text)
    }

    /**
     * Set Rivalry info
     */
    private fun setRivalryInfo() {
        currentRivalry?.visitorScore?.let {
            (findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        currentRivalry?.homeScore?.let {
            (findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        /*currentRivalry?.visitorPlayer?.let {
            (findViewById<View>(R.id.initials_visitor) as TextView).text =
                Player.getPlayer(it)?.initials
            (findViewById<View>(R.id.name_visitor) as TextView).text =
                Player.getPlayer(it)?.name
        }
        currentRivalry?.homePlayer?.let {
            (findViewById<View>(R.id.initials_home) as TextView).text =
                Player.getPlayer(it)?.initials
            (findViewById<View>(R.id.name_home) as TextView).text =
                Player.getPlayer(it)?.name
        }*/
        Player.getPlayer(currentRivalry?.visitorPlayer)?.image.let {
            if (it != null) {
                (findViewById<View>(R.id.round_image_visitor) as ImageView).setImageResource(
                    it
                )
            }
        }
        Player.getPlayer(currentRivalry?.homePlayer)?.image.let {
            if (it != null) {
                (findViewById<View>(R.id.round_image_home) as ImageView).setImageResource(
                    it
                )
            }
        }
    }

    /**
     * Set up game list view
     */
    private fun setGameListView() {
        if (currentRivalry != null) {
            if (currentRivalry!!.games.size > 0) {
                val listView: ListView = findViewById<ListView>(R.id.listview_games)
                val adapter = GameAdapter(
                    this,
                    ArrayList(currentRivalry!!.games.reversed())
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

    /**
     * Set up new game button
     */
    private fun setAddGameButton() {
        (findViewById<View>(R.id.button_add_game) as ImageButton).setOnClickListener {
            if (currentRivalry != null) {
                var id: Int?
                if (currentRivalry!!.games.size > 0) {
                    if (currentRivalry!!.games.reversed()[0].status != "Active") {
                        id = Game.makeGame(currentRivalry!!.id).id
                    } else {
                        id = currentRivalry!!.games.reversed()[0].id
                        Toast.makeText(
                            this,
                            "An active game already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    id = Game.makeGame(currentRivalry!!.id).id
                }
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(GAME, id)
                }
                startActivity(intent)
            }
        }
    }
}