package com.example.opsc7311_poe_group20

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView

class PriorityAdapter(
    context: Context,
    resource: Int,
    private val priorities: Array<String>,
    private val spinner: Spinner
) : ArrayAdapter<String>(context, resource, priorities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val priorityTextView = view as TextView
        val priority = priorities[position]
        setSpinnerTextColor(priorityTextView, priority)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val priorityTextView = view as TextView
        val priority = priorities[position]
        setSpinnerTextColor(priorityTextView, priority)
        return view
    }

    private fun setSpinnerTextColor(textView: TextView, priority: String) {
        val priorityColor = getPriorityColor(priority)
        textView.setTextColor(priorityColor)
    }


    private fun getPriorityColor(priority: String): Int {
        return when (priority) {
            "Very High" -> Color.parseColor("#f51505")
            "High" -> Color.parseColor("#f27a18")
            "Medium" -> Color.parseColor("#f2dc18")
            "Low" -> Color.parseColor("#82f218")
            "Very Low" -> Color.parseColor("#0366fc")
            else -> Color.BLACK
        }
    }
}
