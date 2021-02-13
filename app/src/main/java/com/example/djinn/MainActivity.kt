package com.example.djinn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as DjinnApplication).playerRepository,
            (application as DjinnApplication).rivalryRepository
        )
    }

    private val gameActivityViewModel: GameActivityViewModel by viewModels {
        GameActivityViewModelFactory(
            (application as DjinnApplication).playerRepository,
            (application as DjinnApplication).rivalryRepository,
            (application as DjinnApplication).gameRepository,
            (application as DjinnApplication).partialGameRepository
        )
    }
    private val playerImageMap: TreeMap<Int, Int> = TreeMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.allPlayers.observe(this) { players ->
            for (player in players) {
                playerImageMap[player.id] = player.image
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.listview_all)
        val adapter = RivalryListAdapter(playerImageMap) { rivalry -> adapterOnClick(rivalry) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allRivalries
            .observe(this) { rivalries ->
                rivalries.let { adapter.submitList(it) }
            }

        //Temp block to insert Partial Games
        val controller = 0
        if (controller == 1) {
            gameActivityViewModel.insertPartialGames(
                listOf(
                    PartialGame.makePartialGame(57, 35, "Knock", 6),
                    PartialGame.makePartialGame(57, 35, "Knock", 39),
                    PartialGame.makePartialGame(57, 35, "Knock", 29),
                    PartialGame.makePartialGame(57, 35, "Knock", 8),
                    PartialGame.makePartialGame(57, 35, "Knock", 9),
                    PartialGame.makePartialGame(57, 33, "Knock", 62),
                    PartialGame.makePartialGame(57, 33, "Knock", 10),
                    PartialGame.makePartialGame(57, 33, "Knock", 22),
                    PartialGame.makePartialGame(57, 33, "Knock", 5),
                    PartialGame.makePartialGame(57, 35, "Knock", 28),
                    PartialGame.makePartialGame(58, 33, "Knock", 57),
                    PartialGame.makePartialGame(58, 33, "Knock", 24),
                    PartialGame.makePartialGame(58, 33, "Knock", 9),
                    PartialGame.makePartialGame(58, 33, "Knock", 33),
                    PartialGame.makePartialGame(59, 33, "Gin", 60),
                    PartialGame.makePartialGame(59, 35, "Knock", 44),
                    PartialGame.makePartialGame(59, 33, "Knock", 27),
                    PartialGame.makePartialGame(60, 33, "Undercut", 2),
                    PartialGame.makePartialGame(60, 33, "Knock", 40),
                    PartialGame.makePartialGame(60, 33, "Knock", 13),
                    PartialGame.makePartialGame(60, 35, "Knock", 5),
                    PartialGame.makePartialGame(60, 35, "Gin", 24),
                    PartialGame.makePartialGame(60, 33, "Knock", 13),
                    PartialGame.makePartialGame(60, 33, "Knock", 20),
                    PartialGame.makePartialGame(60, 35, "Knock", 12),
                    PartialGame.makePartialGame(60, 35, "Knock", 28),
                    PartialGame.makePartialGame(60, 33, "Knock", 14),
                    PartialGame.makePartialGame(61, 35, "Knock", 24),
                    PartialGame.makePartialGame(61, 33, "Knock", 14),
                    PartialGame.makePartialGame(61, 33, "Knock", 11),
                    PartialGame.makePartialGame(61, 33, "Knock", 58),
                    PartialGame.makePartialGame(61, 33, "Knock", 41),
                    PartialGame.makePartialGame(62, 35, "Knock", 30),
                    PartialGame.makePartialGame(62, 35, "Knock", 41),
                    PartialGame.makePartialGame(62, 33, "Knock", 17),
                    PartialGame.makePartialGame(62, 35, "Knock", 19),
                    PartialGame.makePartialGame(62, 35, "Knock", 12),
                    PartialGame.makePartialGame(63, 33, "Knock", 34),
                    PartialGame.makePartialGame(63, 35, "Knock", 17),
                    PartialGame.makePartialGame(63, 35, "Undercut", 0),
                    PartialGame.makePartialGame(63, 33, "Gin", 40),
                    PartialGame.makePartialGame(63, 35, "Knock", 7),
                    PartialGame.makePartialGame(63, 33, "Knock", 42),
                    PartialGame.makePartialGame(64, 35, "Knock", 18),
                    PartialGame.makePartialGame(64, 35, "Knock", 23),
                    PartialGame.makePartialGame(64, 35, "Undercut", 3),
                    PartialGame.makePartialGame(64, 35, "Knock", 45),
                    PartialGame.makePartialGame(64, 35, "Knock", 10)
                )
            )
        }
    }

    /**
     * Function passed to list adapter to set onClick behavior
     */
    private fun adapterOnClick(rivalry: Rivalry) {
        val intent = Intent(this, RivalryActivity::class.java).apply {
            putExtra(RIVALRY, rivalry.id)
            putExtra(VISITOR_IMAGE, playerImageMap[rivalry.visitorPlayer])
            putExtra(HOME_IMAGE, playerImageMap[rivalry.homePlayer])
        }
        startActivity(intent)
    }
}