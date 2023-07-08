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
import java.text.SimpleDateFormat
import java.util.*

//create list_row then code class
class ListViewTimesheetApdt(context : Context,items:MutableList<TimesheetItems>) : ArrayAdapter<TimesheetItems>(context,R.layout.list_row,items){
    lateinit var list : MutableList<TimesheetItems>
    //lateinit var context : Context
    init {
        //super.{context,R.layout.list_row,items}
        //this.context=context
        this.list=items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv: View? = convertView

        var layoutInflaterObj = LayoutInflater.from(context)

        cv = cv ?: layoutInflaterObj.inflate(R.layout.timesheet_items, null)

        var rowProject: TextView? = cv?.findViewById(R.id.heading)
        var entryDate: TextView? = cv?.findViewById(R.id.date_text)
        var entryTime: TextView? = cv?.findViewById(R.id.time_text)

        val projectName = list[position].projectName
        val totalDuration = list[position].duration
        val userEmail = list[position].email

        val dateString = list[position].date // Assuming the date is in a valid format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val formattedDate = dateFormat.format(dateString) // Format the date string

        val myHours: Int = totalDuration / 60
        val myMinutes: Int = totalDuration % 60

        rowProject?.text = "$projectName"
        entryDate?.text = "$formattedDate"
        entryTime?.text = "$myHours hours, $myMinutes minutes"

        //for colour:

        val Project = ProjectManager.projectList.find { it.projectName == projectName && it.email == userEmail }
        var colour: TextView? = cv?.findViewById(R.id.colourtxt)

        val colorResourceId = Project?.let { getColorResourceId(it.projectColor) }
        val shapeDrawable = colorResourceId?.let { createCircularShapeDrawable(it) }
        if (colour != null) {
            colour.background = shapeDrawable
        }

        return cv!!
    }

    private fun createCircularShapeDrawable(color: Int): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.setColor(color)
        return shapeDrawable
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
}