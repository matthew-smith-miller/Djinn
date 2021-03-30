package com.example.djinn

import android.content.Context
import android.opengl.Visibility
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
    private val playerMap: TreeMap<Int, Player>,
    private val onClick: (Rivalry) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<Rivalry, RivalryListAdapter.RivalryViewHolder>(
        RivalriesComparator()
    ) {

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RivalryViewHolder {
        this.parent = parent
        return RivalryViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: RivalryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(
            current.id,
            current.visitorScore,
            playerMap[current.visitorPlayer]?.imageName.let {
                parent.context.resources.getIdentifier(it, "drawable", parent.context.packageName)
            },
            playerMap[current.visitorPlayer]?.initials,
            current.homeScore,
            playerMap[current.homePlayer]?.imageName.let {
                parent.context.resources.getIdentifier(it, "drawable", parent.context.packageName)
            },
            playerMap[current.homePlayer]?.initials
        )
        holder.view.setOnClickListener {
            onClick(current)
        }
    }

    class RivalryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val visitorScoreTextView = itemView.findViewById<TextView>(R.id.score_visitor)
        private val visitorImageView = itemView.findViewById<ImageView>(R.id.round_image_visitor)
        private val visitorInitialsTextView = itemView.findViewById<TextView>(R.id.initials_visitor)
        private val homeImageView = itemView.findViewById<ImageView>(R.id.round_image_home)
        private val homeScoreTextView = itemView.findViewById<TextView>(R.id.score_home)
        private val homeInitialsTextView = itemView.findViewById<TextView>(R.id.initials_home)
        val view = itemView

        fun bind(
            id: Int,
            visitorScore: Int,
            visitorImage: Int?,
            visitorInitials: String?,
            homeScore: Int,
            homeImage: Int?,
            homeInitials: String?
        ) {
            visitorScoreTextView.text = visitorScore.toString()
            if (visitorImage != null && visitorImage != 0) {
                visitorImageView.setImageResource(visitorImage)
                visitorImageView.visibility = View.VISIBLE
                visitorInitialsTextView.visibility = View.GONE
            } else {
                visitorImageView.visibility = View.GONE
                visitorInitialsTextView.visibility = View.VISIBLE
                visitorInitialsTextView.text = visitorInitials
            }
            if (homeImage != null && homeImage != 0) {
                homeImageView.setImageResource(homeImage)
                homeImageView.visibility = View.VISIBLE
                homeInitialsTextView.visibility = View.GONE
            } else {
                homeImageView.visibility = View.GONE
                homeInitialsTextView.visibility = View.VISIBLE
                homeInitialsTextView.text = homeInitials
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