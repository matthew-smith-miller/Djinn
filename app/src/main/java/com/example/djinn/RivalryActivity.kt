package com.example.djinn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class RivalryActivity : AppCompatActivity() {
    private var currentRivalry: Rivalry? = null
    private var games: List<Game> = ArrayList()
    private val viewModel: RivalryActivityViewModel by viewModels {
        RivalryActivityViewModelFactory(
            (application as DjinnApplication).playerRepository,
            (application as DjinnApplication).rivalryRepository,
            (application as DjinnApplication).gameRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rivalry)

        val recyclerView: RecyclerView = findViewById(R.id.listview_games)
        val visitorImage = findViewById<ImageView>(R.id.round_image_visitor)
        val visitorName = findViewById<TextView>(R.id.name_visitor)
        val homeImage = findViewById<ImageView>(R.id.round_image_home)
        val homeName = findViewById<TextView>(R.id.name_home)

        val adapter = GameListAdapter { game -> adapterOnClick(game) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getRivalryWithGamesById(intent.getIntExtra(RIVALRY, -1))
            .observe(this) { rivalryWithGames ->
                rivalryWithGames.let { rivalryWithGames ->
                    currentRivalry = rivalryWithGames.rivalry
                    Log.d("players: ", currentRivalry!!.visitorPlayer.toString())
                    games = rivalryWithGames.games.sortedByDescending { it.id }
                    findViewById<TextView>(R.id.score_visitor).text =
                        rivalryWithGames.rivalry.visitorScore.toString()
                    findViewById<TextView>(R.id.score_home).text =
                        rivalryWithGames.rivalry.homeScore.toString()
                    adapter.submitList(games)
                    viewModel.getPlayerById(rivalryWithGames.rivalry.visitorPlayer)
                        .observe(this) { player ->
                            visitorImage.setImageResource(
                                resources.getIdentifier(
                                    player.imageName,
                                    "drawable",
                                    packageName
                                )
                            )
                            visitorName.text = player.name
                        }
                    viewModel.getPlayerById(rivalryWithGames.rivalry.homePlayer)
                        .observe(this) { player ->
                            homeImage.setImageResource(
                                resources.getIdentifier(
                                    player.imageName,
                                    "drawable",
                                    packageName
                                )
                            )
                            homeName.text = player.name
                        }
                }
            }

        (findViewById<View>(R.id.button_add_game) as ImageButton).setOnClickListener {
            if (currentRivalry != null) {
                var id by Delegates.notNull<Int>()
                var newGameCreated = false
                if (games.isNotEmpty()) {
                    if (games[0].status != "Active") {
                        viewModel.insert(Game.makeGame(games.size + 1, currentRivalry!!.id))
                        newGameCreated = true
                    } else {
                        Toast.makeText(
                            this,
                            "An active game already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    viewModel.insert(Game.makeGame(1, currentRivalry!!.id))
                    newGameCreated = true
                }
                if (newGameCreated) {
                    viewModel.insertedGameId.observe(this) {
                        id = it.toInt()
                        val intent = Intent(this, GameActivity::class.java).apply {
                            putExtra(GAME, id)
                            putExtra(VISITOR_PLAYER_ID, currentRivalry?.visitorPlayer)
                            putExtra(HOME_PLAYER_ID, currentRivalry?.homePlayer)
                        }
                        startActivity(intent)
                    }
                } else {
                    adapterOnClick(games[0])
                }

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
        }
        startActivity(intent)
    }
}