package com.example.djinn

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RivalryActivity : AppCompatActivity() {
    private var currentRivalry: Rivalry? = null
    private val rivalryViewModel: RivalryViewModel by viewModels {
        RivalryViewModelFactory((application as DjinnApplication).rivalryRepository)
    }
    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory((application as DjinnApplication).playerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rivalry)

        val recyclerView: RecyclerView = findViewById(R.id.listview_games)
        val adapter = GameListAdapter { game -> adapterOnClick(game) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val playerImageMap: TreeMap<Int, Int> = TreeMap()

        playerViewModel.allPlayers.observe(owner = this) { players ->
            for (player in players) {
                playerImageMap[player.id] = player.image
            }
        }

        rivalryViewModel.getRivalryWithGamesById(intent.getIntExtra(RIVALRY, -1))
            .observe(owner = this) { rivalryWithGames ->
                rivalryWithGames.let {
                    currentRivalry = it.rivalry
                    findViewById<TextView>(R.id.score_visitor).text =
                        it.rivalry.visitorScore.toString()
                    if (playerImageMap[it.rivalry.visitorPlayer] != null) {
                        findViewById<ImageView>(R.id.round_image_visitor).setImageResource(
                            playerImageMap[it.rivalry.visitorPlayer]!!
                        )
                    }
                    if (playerImageMap[it.rivalry.homePlayer] != null) {
                        findViewById<ImageView>(R.id.round_image_home).setImageResource(
                            playerImageMap[it.rivalry.homePlayer]!!
                        )
                    }
                    findViewById<TextView>(R.id.score_home).text =
                        it.rivalry.homeScore.toString()
                    adapter.submitList(it.games)
                }
            }

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

    override fun onContentChanged() {
        super.onContentChanged()
        findViewById<ListView>(R.id.listview_games).emptyView =
            findViewById(R.id.listview_games_empty_text)
    }

    /**
     * Function passed to list adapter to set onClick behavior
     */
    private fun adapterOnClick(game: Game) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(GAME, game.id)
        }
        startActivity(intent)
    }
}