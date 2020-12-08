package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

class RivalryAdapter(context: Context, rivalries: ArrayList<Rivalry>) :
    ArrayAdapter<Rivalry>(context, 0, rivalries) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View? = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.list_item_rivalry, parent, false
            )
        }

        //Get the Rivalry object located at this point in the list
        val currentRivalry: Rivalry? = getItem(position)

        //Set Rivalry info
        currentRivalry?.visitorScore?.let {
            (listItemView?.findViewById<View>(R.id.score_visitor) as TextView).text = it.toString()
        }
        currentRivalry?.homeScore?.let {
            (listItemView?.findViewById<View>(R.id.score_home) as TextView).text = it.toString()
        }
        currentRivalry?.visitorPlayer?.let {
            (listItemView?.findViewById<View>(R.id.initials_visitor) as TextView).text =
                Player.getPlayer(it)?.initials
        }
        currentRivalry?.homePlayer?.let {
            (listItemView?.findViewById<View>(R.id.initials_home) as TextView).text =
                Player.getPlayer(it)?.initials
        }
        listItemView?.tag = currentRivalry?.id

        //Return statement
        return listItemView!!
    }
}