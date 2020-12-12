package com.example.djinn

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class GameActivity : FragmentActivity(),
    PartialGameDialogFragment.PartialGameDialogListener {
    var currentGame: Game? = null

    fun showPartialGameDialog() {
        val dialog = PartialGameDialogFragment()
        dialog.show(supportFragmentManager, "PartialGameDialogFragment")
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        player: Int,
        type: String,
        rawScore: Int
    ) {
        if (currentGame != null) {
            PartialGame.makePartialGame(
                currentGame!!.id,
                player = player,
                type = type,
                rawScore = rawScore
            )
            setPartialGameListView(currentGame!!)
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //Nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        currentGame = Game.getGame(
            intent.getIntExtra(GAME, -1)
        )
        currentGame?.let { setGameInfo(it) }
        currentGame?.let { setAddPartialGameButton(it) }
        currentGame?.let { setPartialGameListView(it) }
    }

    /**
     * Sets up game info
     */
    fun setGameInfo(currentGame: Game) {
        currentGame.visitorScore.let {
            (findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        Rivalry.getRivalry(currentGame.rivalry)?.visitorPlayer?.let {
            (findViewById<View>(R.id.initials_visitor) as TextView).text =
                Player.getPlayer(it)?.initials
        }
        currentGame.homeScore.let {
            (findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        Rivalry.getRivalry(currentGame.rivalry)?.homePlayer?.let {
            (findViewById<View>(R.id.initials_home) as TextView).text =
                Player.getPlayer(it)?.initials
        }
        currentGame.number.let {
            (findViewById<View>(R.id.game_title) as TextView).text = "Game " + it.toString()
        }
    }

    /**
     * Sets up or hides add partial game button
     */
    fun setAddPartialGameButton(currentGame: Game) {

        if (currentGame.status == "Active") {
            (findViewById<View>(R.id.button_add_partial_game) as ImageButton).setOnClickListener {
                showPartialGameDialog()
            }
            val swipeButton = findViewById<View>(R.id.button_swipe_add_partial_game)
            val partialGameGestureListener = GestureDetector(
                this, PartialGameGestureListener()
            )
            val touchListener =
                OnTouchListener { v, event ->
                    partialGameGestureListener.onTouchEvent(event)
                }
            swipeButton.setOnTouchListener(touchListener)
        } else {
            findViewById<View>(R.id.button_add_partial_game).visibility = View.GONE
            findViewById<View>(R.id.button_swipe_add_partial_game).visibility = View.GONE
        }
    }

    fun moveSwipeButton(x: Float) {
        val swipeButton = findViewById<View>(R.id.button_swipe_add_partial_game)
    }

    /**
     * Sets up partial games listview
     */
    fun setPartialGameListView(currentGame: Game) {
        val listView: ListView = findViewById<ListView>(R.id.listview_games)
        val adapter = PartialGameAdapter(this, currentGame.partialGames)
        listView.adapter = adapter
    }

    class PartialGameGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.i("Game", "Scrolling: $e1, $e2, $distanceX, $distanceY")
            //Move swipe button
            return true
        }
    }
}