package com.example.opsc7311_poe_group20

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import java.util.*


//create list_row then code class
class ListViewAdapter(context : Context,items:MutableList<Project>) : ArrayAdapter<Project>(context,R.layout.list_row,items){
    lateinit var list : MutableList<Project>
    //lateinit var context : Context
    init {
        //super.{context,R.layout.list_row,items}
        //this.context=context
        this.list=items
    }



    //convertview refers to each row of our listview. Position is the index of the current row
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        var cv = convertView
        if (cv==null){
            var layoutInflatorObj : LayoutInflater?= context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as? LayoutInflater

            if (layoutInflatorObj != null) {//I hate nullables. So many checks involved
                cv=layoutInflatorObj.inflate(R.layout.list_row,null)

                //make changes to each row (row num, name, image, image )
                //change row number. Create text view object
                var rowNum : TextView = cv.findViewById(R.id.rowNumber)
                rowNum.setText("${position +1}.")
                //change project name
                var rowProject: TextView = cv.findViewById(R.id.rowProjectName)
                rowProject.setText(list[position].ProjectName)

                var projColIcon : TextView = cv.findViewById(R.id.Projcolourtxt)
                val colorResourceId = list[position]?.let { getColorResourceId(it.projectColor) }
                val shapeDrawable = colorResourceId?.let { createCircularShapeDrawable(it) }
                if (projColIcon != null) {
                    projColIcon.background = shapeDrawable
                }



            }

        }


        return cv!!
    }

    private fun getColorResourceId(colorString: String): Int {
        return when (colorString.toLowerCase(Locale.ROOT)) {
            "red" -> Color.parseColor("#db0700")
            "pink" -> Color.parseColor("#f72abd")
            "orange" -> Color.parseColor("#f7912a")
            "gold" -> Color.parseColor("#b08805")
            "yellow" -> Color.parseColor("#f7d80c")
            "lightgreen" -> Color.parseColor("#5cd126")
            "darkgreen" -> Color.parseColor("#337316")
            "teal" -> Color.parseColor("#16735a")
            "lightblue" -> Color.parseColor("#1fa9cf")
            "darkblue" -> Color.parseColor("#0f4757")
            "indigo" -> Color.parseColor("#6916a8")
            "violet" -> Color.parseColor("#ae0fd6")
            "brown" -> Color.parseColor("#734c3c")
            "gray" -> Color.parseColor("#94918f")
            else -> Color.WHITE
        }
    }

    private fun createCircularShapeDrawable(color: Int): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.setColor(color)
        return shapeDrawable
    }


}
