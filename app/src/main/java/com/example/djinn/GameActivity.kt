package com.example.djinn

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class GameActivity : FragmentActivity(),
    PartialGameDialogFragment.PartialGameDialogListener {
    private var currentGame: Game? = null
    private var partialGames: List<PartialGame>? = null
    private var homePlayerId: Int = -1
    private var visitorPlayerId: Int = -1

    private val rivalryViewModel: RivalryViewModel by viewModels {
        RivalryViewModelFactory((application as DjinnApplication).rivalryRepository)
    }
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory((application as DjinnApplication).gameRepository)
    }
    private val partialGameViewModel: PartialGameViewModel by viewModels {
        PartialGameViewModelFactory((application as DjinnApplication).partialGameRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Set Toolbar since FragmentActivity doesn't come with it by default
        val toolbar = findViewById<Toolbar>(R.id.custom_toolbar)
        setActionBar(toolbar)

        homePlayerId = intent.getIntExtra(HOME_PLAYER_ID, -1)
        visitorPlayerId = intent.getIntExtra(VISITOR_PLAYER_ID, -1)
        val visitorImageId = intent.getIntExtra(VISITOR_IMAGE, -1)
        val homeImageId = intent.getIntExtra(HOME_IMAGE, -1)

        val recyclerView = findViewById<RecyclerView>(R.id.listview_games)
        val adapter = PartialGameListAdapter(homePlayerId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Retrieve Game with child Partial Games; Populate Game info & Partial Game RecyclerView
        gameViewModel.getGameWithPartialGamesById(intent.getIntExtra(GAME, -1))
            .observe(owner = this) { gameWithPartialGames ->
                gameWithPartialGames.let {
                    currentGame = it.game
                    partialGames = it.partialGames
                    findViewById<TextView>(R.id.score_visitor).text =
                        it.game.visitorScore.toString()
                    findViewById<TextView>(R.id.score_home).text = it.game.homeScore.toString()
                    findViewById<TextView>(R.id.game_title).text = "Game" + it.game.number
                    findViewById<ImageView>(R.id.round_image_visitor).setImageResource(
                        visitorImageId
                    )
                    findViewById<ImageView>(R.id.round_image_home).setImageResource(
                        homeImageId
                    )
                    adapter.submitList(partialGames)
                }
            }

        //Sets up or hides add partial game button depending on Game status
        if (currentGame?.status == "Active") {
            (findViewById<View>(R.id.fab_add_partial_game) as FloatingActionButton).setOnClickListener {
                showPartialGameDialog()
            }
        } else {
            findViewById<View>(R.id.fab_add_partial_game).visibility = View.GONE
        }
    }

    private fun showPartialGameDialog() {
        val bundle = Bundle()
        bundle.putIntegerArrayList(PLAYER_IDS, arrayListOf(visitorPlayerId, homePlayerId))
        val dialog = PartialGameDialogFragment()
        dialog.arguments = bundle;
        dialog.show(supportFragmentManager, "PartialGameDialogFragment")
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        player: Int,
        type: String,
        rawScore: Int
    ) {
        if (currentGame != null) {
            val partialGame = PartialGame.makePartialGame(
                currentGame!!.id,
                player,
                type,
                rawScore
            )
            partialGameViewModel.insert(partialGame)
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //Nothing
    }
}