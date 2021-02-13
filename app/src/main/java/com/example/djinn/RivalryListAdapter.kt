package com.example.djinn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RivalryListAdapter(
    private val playerImageMap: TreeMap<Int, Int>,
    private val onClick: (Rivalry) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<Rivalry, RivalryListAdapter.RivalryViewHolder>(
        RivalriesComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RivalryViewHolder {
        return RivalryViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: RivalryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current.id,
            current.visitorScore,
            playerImageMap[current.visitorPlayer],
            playerImageMap[current.homePlayer],
            current.homeScore
        )
        holder.view.setOnClickListener {
            onClick(current)
        }
    }

    class RivalryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val visitorScoreTextView = itemView.findViewById<TextView>(R.id.score_visitor)
        private val visitorImageView = itemView.findViewById<ImageView>(R.id.round_image_visitor)
        private val homeImageView = itemView.findViewById<ImageView>(R.id.round_image_home)
        private val homeScoreTextView = itemView.findViewById<TextView>(R.id.score_home)
        val view = itemView

        fun bind(id: Int, visitorScore: Int, visitorImage: Int?, homeImage: Int?, homeScore: Int) {
            visitorScoreTextView.text = visitorScore.toString()
            if (visitorImage != null) {
                visitorImageView.setImageResource(visitorImage)
            }
            if (homeImage != null) {
                homeImageView.setImageResource(homeImage)
            }
            homeScoreTextView.text = homeScore.toString()
            itemView.tag = id
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (Rivalry) -> Unit): RivalryViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_rivalry, parent, false)
                return RivalryViewHolder(view)
            }
        }
    }

    class RivalriesComparator : DiffUtil.ItemCallback<Rivalry>() {
        override fun areItemsTheSame(oldItem: Rivalry, newItem: Rivalry): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Rivalry, newItem: Rivalry): Boolean {
            return oldItem == newItem
        }
    }
}