package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.time.format.DateTimeFormatter
import java.util.*

class PartialGameAdapter(context: Context, partialGames: ArrayList<PartialGame>) :
    ArrayAdapter<PartialGame>(context, 0, partialGames) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View? = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.list_item_game, parent, false
            )
        }

        //Get the Game object located at this point in the list
        val currentPartialGame: PartialGame? = getItem(position)

        if (currentPartialGame != null) {
            if (Game.getGame(currentPartialGame.game) != null) {
                val visitorPlayerId =
                    Rivalry.getRivalry(Game.getGame(currentPartialGame.id)!!.rivalry)?.visitorPlayer
                val homePlayerId =
                    Rivalry.getRivalry(Game.getGame(currentPartialGame.id)!!.rivalry)?.homePlayer
                if (currentPartialGame.player == visitorPlayerId) {
                    (listItemView?.findViewById<View>(R.id.score_visitor) as TextView).text =
                        currentPartialGame.totalScore.toString()
                    listItemView.findViewById<View>(R.id.score_home)?.visibility =
                        View.INVISIBLE
                    listItemView.findViewById<View>(R.id.score_badge_home)?.visibility =
                        View.INVISIBLE
                } else if (currentPartialGame.player == homePlayerId) {
                    (listItemView?.findViewById<View>(R.id.score_home) as TextView).text =
                        currentPartialGame.totalScore.toString()
                    listItemView.findViewById<View>(R.id.score_visitor)?.visibility =
                        View.INVISIBLE
                    listItemView.findViewById<View>(R.id.score_badge_visitor)?.visibility =
                        View.INVISIBLE
                }
            }
        }

        listItemView?.tag = currentPartialGame?.id

        //Return statement
        return listItemView!!
    }
}