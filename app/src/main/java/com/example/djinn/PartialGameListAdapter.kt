package com.example.djinn

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class PartialGameListAdapter(
    private val homePlayerId: Int,
    private val badgeOnClick: (PartialGame) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<PartialGame, PartialGameListAdapter.PartialGameViewHolder>(
        PartialGamesComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialGameViewHolder {
        return PartialGameViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PartialGameViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current,
            when (current.player) {
                homePlayerId -> "Home"
                else -> "Visitor"
            }
        ) { badgeOnClick }
    }

    class PartialGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val badgeView = itemView.findViewById<View>(R.id.score_badge)
        private val scoreTextView = itemView.findViewById<TextView>(R.id.score)
        private val noteTextView = itemView.findViewById<TextView>(R.id.note)

        fun bind(current: PartialGame, playerRole: String, badgeOnClick: (PartialGame) -> Unit) {
            badgeView.background = when (current.type) {
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
            badgeView.setOnLongClickListener {
                badgeOnClick(current)
                Log.d("PartialGameListAdapter", current.toString())
                return@setOnLongClickListener true
            }
            scoreTextView.text = current.totalScore.toString()
            noteTextView.text = current.note
            itemView.tag = current.id
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