package com.example.opsc7311_poe_group20

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColouredSpinnerAdapter(context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val itemText = getItem(position)
        val itemColor = getColorForItem(itemText)

        view.setBackgroundColor(itemColor)
        view.findViewById<TextView>(android.R.id.text1).setTextColor(Color.WHITE) // You can change the text color if needed

        return view
    }



    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_color_dropdown, parent, false)
        val itemText = getItem(position)
        val itemColor = getColorForItem(itemText)

        val colorBox = view.findViewById<View>(R.id.colorBox)
        val colorTextView = view.findViewById<TextView>(android.R.id.text1)

        colorBox.setBackgroundColor(itemColor)
        colorTextView.text = itemText

        return view
    }

    private fun getColorForItem(itemText: String?): Int {
        return when (itemText) {
            "Red" -> Color.parseColor("#db0700")
            "Pink" -> Color.parseColor("#f72abd")
            "Orange" -> Color.parseColor("#f7912a")
            "Gold" -> Color.parseColor("#b08805")
            "Yellow" -> Color.parseColor("#f7d80c")
            "LightGreen" -> Color.parseColor("#5cd126")
            "DarkGreen" -> Color.parseColor("#337316")
            "Teal" -> Color.parseColor("#16735a")
            "LightBlue" -> Color.parseColor("#1fa9cf")
            "DarkBlue" -> Color.parseColor("#0f4757")
            "Indigo" -> Color.parseColor("#6916a8")
            "Violet" -> Color.parseColor("#ae0fd6")
            "Brown" -> Color.parseColor("#734c3c")
            "Gray" -> Color.parseColor("#94918f")
            else -> Color.WHITE
        }
    }
}
