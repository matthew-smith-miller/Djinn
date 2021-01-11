package com.example.djinn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class GameListAdapter(
    private val onClick: (Game) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<Game, GameListAdapter.GameViewHolder>(
        GamesComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current.id,
            current.visitorScore,
            current.endDate,
            current.status,
            current.homeScore
        )
    }

    class GameViewHolder(itemView: View, val onClick: (Game) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val visitorScoreTextView = itemView.findViewById<TextView>(R.id.score_visitor)
        private val gameDateTextView = itemView.findViewById<TextView>(R.id.game_date)
        private val homeScoreTextView = itemView.findViewById<TextView>(R.id.score_home)
        private var currentGame: Game? = null

        init {
            itemView.setOnClickListener {
                currentGame?.let {
                    onClick(it)
                }
            }
        }

        fun bind(id: Int, visitorScore: Int, endDate: Date?, status: String, homeScore: Int) {
            visitorScoreTextView.text = visitorScore.toString()
            gameDateTextView.text = if (endDate != null) {
                SimpleDateFormat("MM/dd").format(endDate)
            } else {
                status
            }
            homeScoreTextView.text = homeScore.toString()
            itemView.tag = id
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (Game) -> Unit): GameViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_game, parent, false)
                return GameViewHolder(view, onClick)
            }
        }
    }

    class GamesComparator : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }
    }
}