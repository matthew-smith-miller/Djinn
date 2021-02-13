package com.example.djinn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class RivalryActivity : AppCompatActivity() {
    private var visitorImageId by Delegates.notNull<Int>()
    private var homeImageId by Delegates.notNull<Int>()
    private var currentRivalry: Rivalry? = null
    private var games: List<Game> = ArrayList()
    private val rivalryViewModel: RivalryViewModel by viewModels {
        RivalryViewModelFactory((application as DjinnApplication).rivalryRepository)
    }
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory(
            (application as DjinnApplication).gameRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rivalry)

        val recyclerView: RecyclerView = findViewById(R.id.listview_games)
        val adapter = GameListAdapter { game -> adapterOnClick(game) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        visitorImageId = intent.getIntExtra(VISITOR_IMAGE, -1)
        homeImageId = intent.getIntExtra(HOME_IMAGE, -1)

        rivalryViewModel.getRivalryWithGamesById(intent.getIntExtra(RIVALRY, -1))
            .observe(this) { rivalryWithGames ->
                rivalryWithGames.let {
                    currentRivalry = it.rivalry
                    games = it.games
                    findViewById<TextView>(R.id.score_visitor).text =
                        it.rivalry.visitorScore.toString()
                    findViewById<ImageView>(R.id.round_image_visitor).setImageResource(
                        visitorImageId
                    )
                    findViewById<ImageView>(R.id.round_image_home).setImageResource(
                        homeImageId
                    )
                    findViewById<TextView>(R.id.score_home).text =
                        it.rivalry.homeScore.toString()
                    adapter.submitList(games)
                }
            }

        (findViewById<View>(R.id.button_add_game) as ImageButton).setOnClickListener {
            if (currentRivalry != null) {
                if (games.isNotEmpty()) {
                    if (games.reversed()[0].status != "Active") {
                        gameViewModel.insert(Game.makeGame(games.size + 1, currentRivalry!!.id))
                    } else {
                        Toast.makeText(
                            this,
                            "An active game already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    gameViewModel.insert(Game.makeGame(1, currentRivalry!!.id))
                }
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(GAME, games.reversed()[0].id)
                    putExtra(VISITOR_PLAYER_ID, currentRivalry?.visitorPlayer)
                    putExtra(HOME_PLAYER_ID, currentRivalry?.homePlayer)
                    putExtra(VISITOR_IMAGE, visitorImageId)
                    putExtra(HOME_IMAGE, homeImageId)
                }
                startActivity(intent)
            }
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()
        //findViewById<RecyclerView>(R.id.listview_games).emptyView =
        //    findViewById(R.id.listview_games_empty_text)
    }

    /**
     * Function passed to list adapter to set onClick behavior
     */
    private fun adapterOnClick(game: Game) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(GAME, game.id)
            putExtra(HOME_PLAYER_ID, currentRivalry?.homePlayer)
            putExtra(VISITOR_PLAYER_ID, currentRivalry?.visitorPlayer)
            putExtra(VISITOR_IMAGE, visitorImageId)
            putExtra(HOME_IMAGE, homeImageId)
        }
        startActivity(intent)
    }
}