package com.example.djinn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    private val rivalryViewModel: RivalryViewModel by viewModels {
        RivalryViewModelFactory((application as DjinnApplication).rivalryRepository)
    }
    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory((application as DjinnApplication).playerRepository)
    }
    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory((application as DjinnApplication).gameRepository)
    }
    private val partialGameViewModel: PartialGameViewModel by viewModels {
        PartialGameViewModelFactory((application as DjinnApplication).partialGameRepository)
    }
    private val playerImageMap: TreeMap<Int, Int> = TreeMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerViewModel.allPlayers.observe(owner = this) { players ->
            for (player in players) {
                playerImageMap[player.id] = player.image
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.listview_all)
        val adapter = RivalryListAdapter(playerImageMap) { rivalry -> adapterOnClick(rivalry) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        rivalryViewModel.allRivalries
            .observe(owner = this) { rivalries ->
                rivalries.let { adapter.submitList(it) }
            }

        //Temp block to insert Partial Games
        /*partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 6))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 39))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 29))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 8))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 9))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 33, "Knock", 62))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 33, "Knock", 10))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 33, "Knock", 22))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 33, "Knock", 5))
        partialGameViewModel.insert(PartialGame.makePartialGame(57, 35, "Knock", 28))
        partialGameViewModel.insert(PartialGame.makePartialGame(58, 33, "Knock", 57))
        partialGameViewModel.insert(PartialGame.makePartialGame(58, 33, "Knock", 24))
        partialGameViewModel.insert(PartialGame.makePartialGame(58, 33, "Knock", 9))
        partialGameViewModel.insert(PartialGame.makePartialGame(58, 33, "Knock", 33))
        partialGameViewModel.insert(PartialGame.makePartialGame(59, 33, "Gin", 60))
        partialGameViewModel.insert(PartialGame.makePartialGame(59, 35, "Knock", 44))
        partialGameViewModel.insert(PartialGame.makePartialGame(59, 33, "Knock", 27))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Undercut", 2))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Knock", 40))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Knock", 13))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 35, "Knock", 5))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 35, "Gin", 24))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Knock", 13))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Knock", 20))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 35, "Knock", 12))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 35, "Knock", 28))
        partialGameViewModel.insert(PartialGame.makePartialGame(60, 33, "Knock", 14))
        partialGameViewModel.insert(PartialGame.makePartialGame(61, 35, "Knock", 24))
        partialGameViewModel.insert(PartialGame.makePartialGame(61, 33, "Knock", 14))
        partialGameViewModel.insert(PartialGame.makePartialGame(61, 33, "Knock", 11))
        partialGameViewModel.insert(PartialGame.makePartialGame(61, 33, "Knock", 58))
        partialGameViewModel.insert(PartialGame.makePartialGame(61, 33, "Knock", 41))
        partialGameViewModel.insert(PartialGame.makePartialGame(62, 35, "Knock", 30))
        partialGameViewModel.insert(PartialGame.makePartialGame(62, 35, "Knock", 41))
        partialGameViewModel.insert(PartialGame.makePartialGame(62, 33, "Knock", 17))
        partialGameViewModel.insert(PartialGame.makePartialGame(62, 35, "Knock", 19))
        partialGameViewModel.insert(PartialGame.makePartialGame(62, 35, "Knock", 12))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 33, "Knock", 34))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 35, "Knock", 17))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 35, "Undercut", 0))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 33, "Gin", 40))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 35, "Knock", 7))
        partialGameViewModel.insert(PartialGame.makePartialGame(63, 33, "Knock", 42))
        partialGameViewModel.insert(PartialGame.makePartialGame(64, 35, "Knock", 18))
        partialGameViewModel.insert(PartialGame.makePartialGame(64, 35, "Knock", 23))
        partialGameViewModel.insert(PartialGame.makePartialGame(64, 35, "Undercut", 3))
        partialGameViewModel.insert(PartialGame.makePartialGame(64, 35, "Knock", 45))
        partialGameViewModel.insert(PartialGame.makePartialGame(64, 35, "Knock", 10))*/
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