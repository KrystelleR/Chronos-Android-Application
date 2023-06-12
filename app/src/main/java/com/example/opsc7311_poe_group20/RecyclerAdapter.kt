package com.example.opsc7311_poe_group20

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var time = arrayOf("1h00", "2h00", "3h00", "4h00", "5h00", "6h00", "7h00", "8h00", "9h00", "10h00", "11h00", "12h00", "13h00", "14h00", "15h00", "16h00", "17h00", "18h00", "19h00", "20h00", "21h00", "22h00", "23h00", "0h00")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hours_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return time.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.hour.text = time[position]
        if(position == 12){
            holder.card.setVisibility(View.VISIBLE)
        }
        else{
            holder.card.setVisibility(View.GONE)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var hour: TextView
        var card : CardView
        var viewEntry : Button

        init{
            hour = itemView.findViewById(R.id.hourtxt)
            card = itemView.findViewById(R.id.cvtimeEntry)
            viewEntry = itemView.findViewById(R.id.viewbtn)
        }
    }

}