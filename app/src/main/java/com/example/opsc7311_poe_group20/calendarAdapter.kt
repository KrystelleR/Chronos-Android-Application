package com.example.opsc7311_poe_group20

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

//create list_row then code class
class calendarAdapter(context: Context, items: MutableList<TimesheetItems>, private val selectedDate: Date) :
    ArrayAdapter<TimesheetItems>(context, R.layout.list_row, items) {
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

        cv = cv ?: layoutInflaterObj.inflate(R.layout.hours_view, null)

        val itemDate = list[position].date

        val theSelectedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(selectedDate.time)
        val theItemDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(itemDate.time)

        if (theItemDate == theSelectedDate){

            var rowProject: TextView? = cv?.findViewById(R.id.projectNametxt)
            var startTime: TextView? = cv?.findViewById(R.id.startTimetxt)
            var endTime: TextView? = cv?.findViewById(R.id.endTimetxt)
            var duration: TextView? = cv?.findViewById(R.id.totalTimetxt)

            val projectName = list[position].projectName
            val totalDuration = list[position].duration
            val userEmail = list[position].email
            val start = list[position].startTime
            val end = list[position].endTime

            val myHours: Int = totalDuration / 60
            val myMinutes: Int = totalDuration % 60

            rowProject?.text = "$projectName"
            val startTimeFormatted = formatDate(start.toString())
            startTime?.text = "Start Time: $startTimeFormatted"

            val endTimeFormatted = formatDate(end.toString())
            endTime?.text = "End Time: $endTimeFormatted"

            duration?.text = "$myHours hr/s $myMinutes min"

            //for colour:

            val Project =
                ProjectManager.projectList.find { it.projectName == projectName && it.email == userEmail }
            var colour: TextView? = cv?.findViewById(R.id.colourtxt)

            val colorResourceId = Project?.let { getColorResourceId(it.projectColor) }
            val shapeDrawable = colorResourceId?.let { createCircularShapeDrawable(it) }
            if (colour != null) {
                colour.background = shapeDrawable
            }

        }
        else{
            var myView: LinearLayout? = cv?.findViewById(R.id.wholeView)
            if (myView != null) {
                myView.visibility = View.GONE
            }
        }

        return cv!!
    }

    private fun createCircularShapeDrawable(color: Int): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.setColor(color)
        return shapeDrawable
    }
    private fun formatDate(time: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(time)
        return outputFormat.format(date)
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