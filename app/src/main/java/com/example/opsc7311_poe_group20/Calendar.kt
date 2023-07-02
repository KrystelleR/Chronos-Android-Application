package com.example.opsc7311_poe_group20

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import android.widget.CalendarView
import android.widget.TextView

class Calendar : AppCompatActivity() {

    lateinit var items : MutableList<TimesheetItems>
    lateinit var adptr :ListViewTimesheetApdt
    lateinit var theUserDate :Date
    //create new adapter KRYSTELLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        var chosenDay = findViewById<TextView>(R.id.monthDayText)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        chosenDay.text = currentDate.toString()

        val calendar = Calendar.getInstance()
        calendar.time = Date() // Set the calendar instance to the current date
        theUserDate = calendar.time

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

// Convert the day of week to a string representation
        val dayOfWeekString = when (dayOfWeek) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Unknown"
        }

        var day = findViewById<TextView>(R.id.dayOfWeek)
        day.text = dayOfWeekString


        val forwardBtn = findViewById<Button>(R.id.forward)
        forwardBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = theUserDate // Set the user-provided date

            calendar.add(Calendar.DAY_OF_MONTH, 1) // Increment the date by one day

            val nextDate = calendar.time // Get the updated date

            val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(nextDate)
            chosenDay.text = formattedDate

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Get the day of the week

            val dayOfWeekString = when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Unknown"
            }

            val day = findViewById<TextView>(R.id.dayOfWeek)
            day.text = dayOfWeekString

            val calendarView = findViewById<CalendarView>(R.id.calendarView)
            calendarView.setDate(calendar.timeInMillis, true, true) // Set the updated date in the CalendarView

            theUserDate = nextDate

            //ADAPTER
            var lvTimesheetEntry : ListView =findViewById(R.id.lvTimesheetEntries)
            //set project list to items and pass as param of ListViewAdapter object.
            val items = Timesheetobj.timesheetlist

            //adapter holds data that listview will display. ListViewAdapter is our custom adapter class we made
            val adptr = calendarAdapter(this, items, theUserDate)


            //to see updated results
            lvTimesheetEntry.adapter = adptr
        }


        val backwardsBtn = findViewById<Button>(R.id.backwards)
        backwardsBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = theUserDate // Set the user-provided date

            calendar.add(Calendar.DAY_OF_MONTH, -1) // Increment the date by one day

            val previousDate = calendar.time // Get the updated date

            val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(previousDate)
            chosenDay.text = formattedDate

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Get the day of the week

            val dayOfWeekString = when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Unknown"
            }

            val day = findViewById<TextView>(R.id.dayOfWeek)
            day.text = dayOfWeekString

            val calendarView = findViewById<CalendarView>(R.id.calendarView)
            calendarView.setDate(calendar.timeInMillis, true, true) // Set the updated date in the CalendarView

            theUserDate = previousDate

            //ADAPTER
            var lvTimesheetEntry : ListView =findViewById(R.id.lvTimesheetEntries)
            //set project list to items and pass as param of ListViewAdapter object.
            val items = Timesheetobj.timesheetlist

            //adapter holds data that listview will display. ListViewAdapter is our custom adapter class we made
            val adptr = calendarAdapter(this, items, theUserDate)


            //to see updated results
            lvTimesheetEntry.adapter = adptr
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Retrieve the selected date
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val theSelectedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(selectedDate.time)

            val chosenDay = findViewById<TextView>(R.id.monthDayText)
            chosenDay.text = theSelectedDate

            val dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)
            val dayOfWeekString = when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Unknown"
            }

            val day = findViewById<TextView>(R.id.dayOfWeek)
            day.text = dayOfWeekString

            val calendar = Calendar.getInstance()
            calendar.time = selectedDate.time

            theUserDate = calendar.time

            val forwardBtn = findViewById<Button>(R.id.forward)
            forwardBtn.isEnabled = true

            val backwardsBtn = findViewById<Button>(R.id.backwards)
            backwardsBtn.isEnabled = true

            //ADAPTER
            var lvTimesheetEntry : ListView =findViewById(R.id.lvTimesheetEntries)
            //set project list to items and pass as param of ListViewAdapter object.
            val items = Timesheetobj.timesheetlist

            //adapter holds data that listview will display. ListViewAdapter is our custom adapter class we made
            val adptr = calendarAdapter(this, items, theUserDate)


            //to see updated results
            lvTimesheetEntry.adapter = adptr
        }

        
        //ADAPTER
        var lvTimesheetEntry : ListView =findViewById(R.id.lvTimesheetEntries)
        //set project list to items and pass as param of ListViewAdapter object.
        val items = Timesheetobj.timesheetlist

        //adapter holds data that listview will display. ListViewAdapter is our custom adapter class we made
        val adptr = calendarAdapter(this, items, theUserDate)

        //click methods. short and long. Position is the index of the item that was clicked
        lvTimesheetEntry.setOnItemClickListener { parent, view, i, id ->
            var timeentryName : Int = items[i].timesheetID
            Home.clickedItemPosition =i
            //navigate to details page on short click
            val intentDetails = Intent(this,timesheetDetails::class.java)
            startActivity(intentDetails)
        }

        //to see updated results
        lvTimesheetEntry.adapter = adptr


        //Navigation
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
    }
}