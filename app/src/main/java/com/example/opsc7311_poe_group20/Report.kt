package com.example.opsc7311_poe_group20

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Dataset
import android.view.View
import android.widget.*
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.bottomnavigation.BottomNavigationView

//graph imports
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.ValueFormatter

//date imports
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar
import kotlin.collections.ArrayList

class Report : AppCompatActivity() {
    // on below line we are creating
    // variables for our graph view
    //store timesheet entries
    lateinit var lineList:ArrayList<Entry>
    //store max goal entries
    lateinit var maxGoalDataList:ArrayList<Entry>
    //store min goal entries
    lateinit var minGoalDataList:ArrayList<Entry>
    lateinit var lineDataset: LineDataSet

    lateinit var maxDataSet:LineDataSet
    lateinit var minDataSet: LineDataSet

    lateinit var lineData :LineData

    //date picker variables
    //format date
    var myFormat = SimpleDateFormat("dd MM YY")
    lateinit var startDisplay : String
    lateinit var endDisplay : String

    //date picker variables
    private var startDate: Date = Date()
    private var endDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val graph=findViewById<LineChart>(R.id.line_chart)
        graph.visibility= View.INVISIBLE
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_timesheet -> {
                    if(ProjectManager.projectList.size == 0){
                        // Handle item1 click
                        val intent1 = Intent(this, AddProject::class.java)
                        startActivity(intent1)
                        Toast.makeText(this, "Add a project first" , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        // Handle item1 click
                        val intent1 = Intent(this, Timesheets::class.java)
                        startActivity(intent1)
                    }
                    true
                }
                R.id.ic_home -> {
                    // Handle item2 click
                    val intent2 = Intent(this, Home::class.java)
                    startActivity(intent2)
                    true
                }
                R.id.ic_report -> {
                    // Handle item3 click
                    val intent3 = Intent(this, Report::class.java)
                    startActivity(intent3)
                    true
                }
                else -> false
            }
        }

        //graph code

        //populate list for entries
        lineList= ArrayList()
        lineList.add(Entry(dateToFloat("03/07/23"),1f))
        lineList.add(Entry(dateToFloat("04/07/23"),3f))
        lineList.add(Entry(dateToFloat("05/07/23"),2f))
        lineList.add(Entry(dateToFloat("06/07/23"),6f))
        lineList.add(Entry(dateToFloat("07/07/23"),5f))
        lineList.add(Entry(dateToFloat("08/07/23"),9f))


        maxGoalDataList= ArrayList()
        maxGoalDataList.add(Entry(dateToFloat("03/07/23"),8f))
        maxGoalDataList.add(Entry(dateToFloat("04/07/23"),8f))
        maxGoalDataList.add(Entry(dateToFloat("05/07/23"),8f))
        maxGoalDataList.add(Entry(dateToFloat("06/07/23"),8f))
        maxGoalDataList.add(Entry(dateToFloat("07/07/23"),8f))
        maxGoalDataList.add(Entry(dateToFloat("08/07/23"),8f))

        minGoalDataList= ArrayList()
        minGoalDataList.add((Entry(dateToFloat("03/07/23"),2f)))
        minGoalDataList.add((Entry(dateToFloat("04/07/23"),2f)))
        minGoalDataList.add((Entry(dateToFloat("05/07/23"),2f)))
        minGoalDataList.add((Entry(dateToFloat("06/07/23"),2f)))
        minGoalDataList.add((Entry(dateToFloat("07/07/23"),2f)))
        minGoalDataList.add((Entry(dateToFloat("08/07/23"),2f)))



        //create line
        lineDataset= LineDataSet(lineList,"TimeSheet Entries")
        maxDataSet= LineDataSet(maxGoalDataList,"Max goal")
        maxDataSet.setColor(Color.GREEN)
        maxDataSet.valueTextColor=Color.WHITE
        minDataSet = LineDataSet(minGoalDataList,"Min Goal")
        minDataSet.setColor(Color.RED)
        minDataSet.valueTextColor=Color.WHITE

        //supply line
        lineData= LineData(lineDataset,maxDataSet,minDataSet)


        var chart = findViewById<LineChart>(R.id.line_chart)
        chart.data=lineData
        // Set axis text color
        val xAxis = chart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.valueFormatter = DateAxisValueFormatter()
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.labelRotationAngle = -45f

        val yAxisLeft = chart.axisLeft
        yAxisLeft.textColor = Color.WHITE

        val yAxisRight = chart.axisRight
        yAxisRight.textColor = Color.WHITE

        // Set legend text color
        val legend = chart.legend
        legend.textColor = Color.WHITE


        lineDataset.setColors(*ColorTemplate.JOYFUL_COLORS)
        lineDataset!!.valueTextColor=Color.WHITE
        lineDataset.valueTextSize=20f

        val timesheetEntries = Timesheetobj.timesheetlist
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        // Filter the projectList based on the email
        val filteredProjects = ProjectManager.projectList.filter { it.email == userEmail }

        // Extract the project names from the filtered projects
        val projectNames = filteredProjects.map { it.projectName }
        // Set up ArrayAdapter to populate the spinner with the project names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, projectNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val reportSpinner = findViewById<Spinner>(R.id.projOnReportSpinner)
        reportSpinner.adapter = adapter

//        for (entry in timesheetEntries) {
//            if (entry.email == userEmail && entry.projectName == reportSpinner.selectedItem.toString()) {
//
//                    lineList.add(Entry(dateToFloat(entry.date.toString()),(entry.duration/60).toFloat()))
//
//
//            }
//        }//supply start and end dates into this method to generate a list of dates between these supplied dates


        //date range code
        //date picker
        // Find the dateRangePickerBtn button in the layout
        val startDatePickerBtn: Button = findViewById(R.id.btnStartPeriod)
        val endDatePickerBtn: Button = findViewById(R.id.btnEndPeriod)

        // Set click listeners for the start and end date buttons
        startDatePickerBtn.setOnClickListener {
            showDatePickerDialog(true)
        }

        endDatePickerBtn.setOnClickListener {
            showDatePickerDialog(false)


//
        }



        //search date button
        var btnSetDateRange: Button = findViewById(R.id.btnGraphDateRange)
        btnSetDateRange.setOnClickListener {
            graph.visibility= View.VISIBLE
//            var totalTime = 0.0
//            //check if dates are correct.
//            if (startDate.compareTo(endDate)<0) {
//                for (entry in timesheetEntries) {
//                    if (entry.email == userEmail && entry.projectName == reportSpinner.selectedItem.toString()) {
//                        if (entry.date >= startDate && entry.date <= endDate) {
//                            //totalTime+=entry.duration
//                            lineList.add(Entry(dateToFloat(entry.date.toString()),(entry.duration/60).toFloat()))
//                            lineData= LineData(lineDataset,maxDataSet,minDataSet)
//                            chart.data=lineData
//                        }
//                    }
//                }//supply start and end dates into this method to generate a list of dates between these supplied dates
//            }else if(startDate.compareTo(endDate)>0){
//                // Handle the case when start date or end date is not selected
//                showAlertEndDateOnly(this,"Try again","The end date cannot be before the start date ")
//            }else{
//                showAlertEndDateOnly(this,"Try again","Please supply a complete range of dates ")
//            }
        }
    }

    //date picker methods
    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                if (isStartDate) {
                    startDate = selectedDate.time
                    startDisplay = myFormat.format(selectedDate.time)
                    val startDateText =findViewById<TextView>(R.id.txtdisplayStart)
                    startDateText.text=startDisplay

                } else {
                    endDate = selectedDate.time
                    endDisplay = myFormat.format(selectedDate.time)
                    val endDateText =findViewById<TextView>(R.id.txtdisplayEnd)
                    endDateText.text=endDisplay
                }

                // You can perform further actions based on the selected start and end dates
            },
            year,
            month,
            day
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }

    fun showAlertEndDateOnly(context: Context, title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
            // Action to perform when the positive button is clicked

        }


        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    //method to auto generate dates between 2 selected dates.
    private fun getDates(dateString1: String, dateString2: String): List<Date> {
        val dates = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = android.icu.util.Calendar.getInstance()
        cal1.time = date1

        val cal2 = android.icu.util.Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(android.icu.util.Calendar.DATE, 1)
        }
        return dates
    }

    //convert dates to float to display on graph
    fun dateToFloat(dateString: String): Float {
        val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val date = format.parse(dateString)
        return date.time.toFloat()
    }

}
class DateAxisValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    override fun getFormattedValue(value: Float): String {
        return dateFormat.format(Date(value.toLong()))
    }
}