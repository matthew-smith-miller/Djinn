package com.example.djinn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    /**
     * Function passed to list adapter to set onClick behavior
     */
    private fun adapterOnClick(rivalry: Rivalry) {
        val intent = Intent(this, RivalryActivity::class.java).apply {
            putExtra(RIVALRY, rivalry.id)
        }
        startActivity(intent)
    }
}