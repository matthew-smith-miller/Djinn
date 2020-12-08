package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

class PartialGameAdapter(context: Context, partialGames: ArrayList<PartialGame>) :
    ArrayAdapter<PartialGame>(context, 0, partialGames) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView: View? = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.list_item_partial_game, parent, false
            )
        }
        //Get the Game object located at this point in the list
        val currentPartialGame: PartialGame? = getItem(position)

        if (currentPartialGame != null) {
            if (Game.getGame(currentPartialGame.game) != null) {
                (listItemView?.findViewById<View>(R.id.score) as TextView).text =
                    currentPartialGame.totalScore.toString()
                val badge = listItemView.findViewById<View>(R.id.score_badge)
                badge?.background =
                    when (currentPartialGame.type) {
                        "G" -> ContextCompat.getDrawable(
                            context, R.drawable.round_view_gin
                        )
                        "R" -> ContextCompat.getDrawable(
                            context, R.drawable.round_view_reverse
                        )
                        "B" -> ContextCompat.getDrawable(
                            context, R.drawable.round_view_bonus
                        )
                        else -> ContextCompat.getDrawable(
                            context, R.drawable.round_view_knock
                        )
                    }

                val bonusNote = listItemView.findViewById<View>(R.id.bonus_note) as TextView
                if (currentPartialGame.type == "B") {
                    bonusNote.text = currentPartialGame.note
                    bonusNote.visibility = View.VISIBLE
                } else {
                    bonusNote.text = null
                    bonusNote.visibility = View.INVISIBLE
                }

                val sideDimen = context.resources.getDimension(R.dimen.side_margin).toInt()

                if (currentPartialGame.player == Rivalry.getRivalry(
                        Game.getGame(
                            currentPartialGame.game
                        )?.rivalry
                    )?.visitorPlayer
                ) {
                    val badgeLayoutParams = badge?.layoutParams as RelativeLayout.LayoutParams
                    badgeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                    badgeLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
                    /*badgeLayoutParams.setMargins(
                        context.resources.getDimension(R.dimen.side_margin).toInt(),
                        badgeLayoutParams.topMargin,
                        0,
                        badgeLayoutParams.bottomMargin
                    )*/
                    badgeLayoutParams.marginStart = sideDimen
                    badgeLayoutParams.marginEnd = 0
                    badge.layoutParams = badgeLayoutParams

//                    setMargins(
//                        badge,
//                        sideDimen,
//                        0,
//                        0,
//                        0)

                    /*val badgeMarginParams = badge.layoutParams as LinearLayout.LayoutParams
                    badgeMarginParams.setMargins(
                        context.resources.getDimension(R.dimen.side_margin).toInt(),
                        badgeMarginParams.topMargin,
                        0,
                        badgeMarginParams.bottomMargin)
                    badge.layoutParams = badgeMarginParams*/

                    val bonusNoteLayoutParams =
                        bonusNote.layoutParams as RelativeLayout.LayoutParams
                    bonusNoteLayoutParams.addRule(RelativeLayout.END_OF, R.id.score_badge)
                    bonusNoteLayoutParams.removeRule(RelativeLayout.START_OF)
                    bonusNote.layoutParams = bonusNoteLayoutParams

                } else if (currentPartialGame.player == Rivalry.getRivalry(
                        Game.getGame(
                            currentPartialGame.game
                        )?.rivalry
                    )?.homePlayer
                ) {
                    val badgeLayoutParams = badge?.layoutParams as RelativeLayout.LayoutParams
                    badgeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                    badgeLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
                    /*badgeLayoutParams.setMargins(
                        0,
                        badgeLayoutParams.topMargin,
                        context.resources.getDimension(R.dimen.side_margin).toInt(),
                        badgeLayoutParams.bottomMargin
                    )*/
                    badgeLayoutParams.marginStart = 0
                    badgeLayoutParams.marginEnd = sideDimen
                    badge.layoutParams = badgeLayoutParams

//                    setMargins(
//                        badge,
//                        0,
//                        0,
//                        sideDimen,
//                        0)

                    /*val badgeMarginParams = badge.layoutParams as LinearLayout.LayoutParams
                    badgeMarginParams.setMargins(
                        0,
                        badgeMarginParams.topMargin,
                        context.resources.getDimension(R.dimen.side_margin).toInt(),
                        badgeMarginParams.bottomMargin)
                    badge.layoutParams = badgeMarginParams*/

                    val bonusNoteLayoutParams =
                        bonusNote.layoutParams as RelativeLayout.LayoutParams
                    bonusNoteLayoutParams.addRule(RelativeLayout.START_OF, R.id.score_badge)
                    bonusNoteLayoutParams.removeRule(RelativeLayout.END_OF)
                    bonusNote.layoutParams = bonusNoteLayoutParams
                }
            }
        }

        listItemView?.tag = currentPartialGame?.id

        //Return statement
        return listItemView!!
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val params = view.layoutParams as MarginLayoutParams
            params.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
}