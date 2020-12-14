package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class GameAdapter(context: Context, games: ArrayList<Game>) :
    ArrayAdapter<Game>(context, 0, games) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View? = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.list_item_game, parent, false
            )
        }

        //Get the Game object located at this point in the list
        val currentGame: Game? = getItem(position)

        //Set Game info
        currentGame?.visitorScore?.let {
            (listItemView?.findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        currentGame?.homeScore?.let {
            (listItemView?.findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        if (currentGame?.endDate != null) {
            val dateFormat = SimpleDateFormat("MM/dd")
            dateFormat.format(currentGame.endDate!!).let {
                (listItemView?.findViewById<View>(R.id.game_date) as TextView).text = it
            }
        } else {
            (listItemView?.findViewById<View>(R.id.game_date) as TextView).text =
                currentGame?.status
        }
        listItemView?.tag = currentGame?.id

        //Return statement
        return listItemView!!
    }
}