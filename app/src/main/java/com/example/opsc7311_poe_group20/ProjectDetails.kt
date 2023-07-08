package com.example.opsc7311_poe_group20

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates

class ProjectDetails : AppCompatActivity() {
    //formate date
    var myFormat = SimpleDateFormat("dd MMMM YYYY")
    lateinit var startDisplay : String
    lateinit var endDisplay : String

    //date picker variables
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private lateinit var myProjName: String
    private var totalHours: Int = 0
    private var totalMinutes: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val myProjectName = sharedPreferences.getString("projectName", "")
        val Project = ProjectManager.projectList.find { it.projectName == myProjectName }

        val projectName = findViewById<TextView>(R.id.nametxt)
        myProjName = projectName.text.toString()
        val priority = findViewById<TextView>(R.id.prioritytxt)
        val client = findViewById<TextView>(R.id.clienttxt)
        val billable = findViewById<TextView>(R.id.billabletxt)
        val rate = findViewById<TextView>(R.id.ratetxt)
        val hours = findViewById<TextView>(R.id.hourstxt)
        val minutes = findViewById<TextView>(R.id.minutestxt)
        val min = findViewById<TextView>(R.id.mintxt)
        val max = findViewById<TextView>(R.id.maxtxt)
        val colour = findViewById<TextView>(R.id.colourtxt)

        if (Project != null) {
            projectName.text = Project.projectName
            priority.text = Project.projectPriority
            client.text = Project.clientName
            if (Project.isBillable) {
                billable.text = "Yes"
            } else {
                billable.text = "No"
            }
            rate.text = (Project.rate).toString()
            min.text = (Project.minimum_goal).toString()
            max.text = (Project.maximum_goal).toString()

            val totalDuration = ProjectManager.projectList[AllProjects.clickedItemPosition].totalHours.toInt()

            val totalHours: Int = totalDuration / 60
            val totalMinutes: Int = totalDuration % 60

            //converting total minutes to hours and minutes
            hours.text = totalHours.toString() + " hours"
            minutes.text = totalMinutes.toString() + " minutes"

            val colorResourceId = getColorResourceId(Project.projectColor)
            val shapeDrawable = createCircularShapeDrawable(colorResourceId)
            colour.background = shapeDrawable

            val textColor = getTextColor(Project.projectPriority)
            priority.setTextColor(textColor)
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_timesheet -> {
                    // Handle item1 click
                    val intent1 = Intent(this, Timesheets::class.java)
                    startActivity(intent1)
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

        var btnViewProjReport: Button = findViewById<Button>(R.id.btnViewReport)
        btnViewProjReport.setOnClickListener {
            var intent = Intent(this, Report::class.java)
            startActivity(intent)
        }

        var backToAllProj: Button = findViewById(R.id.btnGoToAllProjects)
        backToAllProj.setOnClickListener {
            var intent = Intent(this, AllProjects::class.java)
            startActivity(intent)
        }

        var btnToEdit: Button = findViewById(R.id.btnGoToEdit)
        btnToEdit.setOnClickListener {
            var intent = Intent(this, EditProject::class.java)
            startActivity(intent)
        }

        //date picker
        // Find the dateRangePickerBtn button in the layout
        val startDatePickerBtn: Button = findViewById(R.id.startDatePickerBtn)
        val endDatePickerBtn: Button = findViewById(R.id.endDatePickerBtn)

        // Set click listeners for the start and end date buttons
        startDatePickerBtn.setOnClickListener {
            showDatePickerDialog(true)
        }

        endDatePickerBtn.setOnClickListener {
            showDatePickerDialog(false)

//            if(endDate!=null){
//                showAlertEndDateOnly(this,"Date Required","Please ensure a start date is entered first")
//            }else{
//
//            }

        }

        var btnSetDateRange: Button = findViewById(R.id.btnSetDateRange)
        btnSetDateRange.setOnClickListener {
            calcProjTotalTime()
            val hours = findViewById<TextView>(R.id.hourstxt)
            val minutes = findViewById<TextView>(R.id.minutestxt)

            hours.text = totalHours.toString() + " hours"
            minutes.text = totalMinutes.toString() + " minutes"
        }
    }


    private fun createCircularShapeDrawable(color: Int): GradientDrawable {
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.OVAL
        shapeDrawable.setColor(color)
        return shapeDrawable
    }

    private fun getTextColor(priority: String): Int {
        return when (priority) {
            "Very High" -> Color.parseColor("#f51505")
            "High" -> Color.parseColor("#f27a18")
            "Medium" -> Color.parseColor("#f2dc18")
            "Low" -> Color.parseColor("#82f218")
            "Very Low" -> Color.parseColor("#0366fc")
            else -> Color.BLACK
        }
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

    //date picker method
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

    private fun calcProjTotalTime() {
//        val btnstart : Button=findViewById(R.id.startDatePickerBtn)
//        val btnend: Button =findViewById((R.id.endDatePickerBtn))
        val project = ProjectManager.projectList[AllProjects.clickedItemPosition].projectName
        val timesheetEntries = Timesheetobj.timesheetlist
        var totalTime: Int = 0

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")

        if (startDate.compareTo(endDate)<0) {
            for (entry in timesheetEntries) {
                if (entry.email == userEmail && entry.projectName == project) {
                    if (entry.date >= startDate && entry.date <= endDate) {
                        totalTime += entry.duration
                    }
                }
            }

            // Convert totalTime to hours and minutes
            totalHours = totalTime / 60
            totalMinutes = totalTime % 60
        } else if(startDate.compareTo(endDate)>0){
            // Handle the case when start date or end date is not selected
            showAlertEndDateOnly(this,"Try again","The end date cannot be before the start date ")
        }else{
            showAlertEndDateOnly(this,"Try again","Please supply a complete range of dates ")
        }
    }




}
