package com.example.djinn

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GameActivity : FragmentActivity(),
    PartialGameDialogFragment.PartialGameDialogListener {
    private var currentGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        currentGame = Game.getGame(
            intent.getIntExtra(GAME, -1)
        )
        Log.d("GameActivity", currentGame?.id.toString())
        setGameInfo()
        setPartialGameRecyclerView()
        setAddPartialGameButton()
        setToolbar()
    }

    override fun onResume() {
        super.onResume()
        setGameInfo()
        setPartialGameRecyclerView()
        setAddPartialGameButton()
        setToolbar()

    }

    private fun showPartialGameDialog() {
        val bundle = Bundle()
        bundle.putStringArray(
            "playerNames",
            arrayOf(
                Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.visitorPlayer)?.name,
                Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.homePlayer)?.name
            )
        )
        bundle.putIntegerArrayList(
            "playerIds",
            arrayListOf(
                Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.visitorPlayer)?.id,
                Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.homePlayer)?.id
            )
        )
        val dialog = PartialGameDialogFragment()
        dialog.arguments = bundle;
        dialog.show(supportFragmentManager, "PartialGameDialogFragment")
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        player: Int,
        playerRole: String,
        type: String,
        rawScore: Int
    ) {
        if (currentGame != null) {
            PartialGame.makePartialGame(
                currentGame!!.id,
                player,
                type,
                rawScore
            )
            setGameInfo()
            setPartialGameRecyclerView()
            setAddPartialGameButton()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //Nothing
    }

    /**
     * Sets up game info
     */
    private fun setGameInfo() {
        currentGame?.visitorScore.let {
            (findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        currentGame?.homeScore.let {
            (findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        currentGame?.number.let {
            (findViewById<View>(R.id.game_title) as TextView).text = "Game " + it.toString()
        }

        Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.visitorPlayer)?.image.let {
            if (it != null) {
                (findViewById<View>(R.id.round_image_visitor) as ImageView).setImageResource(
                    it
                )
            }
        }
        Player.getPlayer(Rivalry.getRivalry(currentGame?.rivalry)?.homePlayer)?.image.let {
            if (it != null) {
                (findViewById<View>(R.id.round_image_home) as ImageView).setImageResource(
                    it
                )
            }
        }
    }

    /**
     * Sets up or hides add partial game button
     * Checks if active because we don't want to add partial to completed game
     */
    private fun setAddPartialGameButton() {
        if (currentGame?.status == "Active") {
            (findViewById<View>(R.id.fab_add_partial_game) as FloatingActionButton).setOnClickListener {
                showPartialGameDialog()
            }
        } else {
            findViewById<View>(R.id.fab_add_partial_game).visibility = View.GONE
        }
    }

    /**
     * Sets up partial games RecyclerView
     */
    private fun setPartialGameRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.listview_games)
        val adapter = PartialGameListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Sets up toolbar since this is a FragmentActivity
     */
    private fun setToolbar() {
        val toolbar = findViewById<android.widget.Toolbar>(R.id.custom_toolbar)
        setActionBar(toolbar)
    }
}