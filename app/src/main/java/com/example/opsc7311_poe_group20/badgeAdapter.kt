package com.example.opsc7311_poe_group20

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class BadgeAdapter(private val items: List<myBadges>) :
    RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.bdg1)
        val theText: TextView = itemView.findViewById(R.id.bdg1txt)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedBadge = items[position]
                    Toast.makeText(itemView.context, clickedBadge.desc, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.mini_badge, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myBadge = items[position]
        holder.image.setImageResource(myBadge.image)
        holder.theText.text = "${myBadge.badgeTitle} Badge"
    }

    override fun getItemCount(): Int {
        return items.size
    }

}



