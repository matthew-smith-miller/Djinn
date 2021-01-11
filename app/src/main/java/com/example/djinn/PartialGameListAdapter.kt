package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PartialGameListAdapter :
    androidx.recyclerview.widget.ListAdapter<PartialGame, PartialGameListAdapter.PartialGameViewHolder>(
        PartialGamesComparator()
    ) {

    private val homePlayerId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialGameViewHolder {
        return PartialGameViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PartialGameViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current.type,
            current.totalScore.toString(),
            current.note,
            current.id,
            when (current.player) {
                homePlayerId -> "Home"
                else -> "Visitor"
            }
        )
    }

    class PartialGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val badgeView = itemView.findViewById<View>(R.id.score_badge)
        private val scoreTextView = itemView.findViewById<TextView>(R.id.score)
        private val noteTextView = itemView.findViewById<TextView>(R.id.note)

        fun bind(type: String, score: String, note: String?, id: Int, playerRole: String) {
            badgeView.background = when (type) {
                "Gin" -> ContextCompat.getDrawable(
                    itemView.context, R.drawable.round_view_gin
                )
                "Undercut" -> ContextCompat.getDrawable(
                    itemView.context, R.drawable.round_view_undercut
                )
                "Bonus" -> ContextCompat.getDrawable(
                    itemView.context, R.drawable.round_view_bonus
                )
                else -> ContextCompat.getDrawable(
                    itemView.context, R.drawable.round_view_knock
                )
            }
            scoreTextView.text = score
            noteTextView.text = note
            itemView.tag = id
            val sideDimen = itemView.context.resources.getDimension(R.dimen.side_margin).toInt()
            when (playerRole) {
                "Visitor" -> (badgeView.layoutParams as RelativeLayout.LayoutParams).let {
                    it.addRule(RelativeLayout.ALIGN_PARENT_START)
                    it.removeRule(RelativeLayout.ALIGN_PARENT_END)
                    it.marginStart = sideDimen
                    it.marginEnd = 0
                    badgeView.layoutParams = it
                }.also {
                    (noteTextView.layoutParams as RelativeLayout.LayoutParams).let {
                        it.addRule(RelativeLayout.END_OF, R.id.score_badge)
                        it.removeRule(RelativeLayout.START_OF)
                        noteTextView.layoutParams = it
                    }
                }
                else -> (badgeView.layoutParams as RelativeLayout.LayoutParams).let {
                    it.addRule(RelativeLayout.ALIGN_PARENT_END)
                    it.removeRule(RelativeLayout.ALIGN_PARENT_START)
                    it.marginStart = 0
                    it.marginEnd = sideDimen
                    badgeView.layoutParams = it
                }.also {
                    (noteTextView.layoutParams as RelativeLayout.LayoutParams).let {
                        it.addRule(RelativeLayout.START_OF, R.id.score_badge)
                        it.removeRule(RelativeLayout.END_OF)
                        noteTextView.layoutParams = it
                    }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): PartialGameViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_partial_game, parent, false)
                return PartialGameViewHolder(view)
            }
        }
    }

    class PartialGamesComparator : DiffUtil.ItemCallback<PartialGame>() {
        override fun areItemsTheSame(oldItem: PartialGame, newItem: PartialGame): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PartialGame, newItem: PartialGame): Boolean {
            return oldItem == newItem
        }
    }
}