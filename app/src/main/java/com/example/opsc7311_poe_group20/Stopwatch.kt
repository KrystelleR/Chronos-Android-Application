package com.example.opsc7311_poe_group20

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.opsc7311_poe_group20.databinding.ActivityStopwatchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import kotlin.math.roundToInt

class Stopwatch : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding
    private var timerStartedNot = true
    private var paused = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    private lateinit var myStartTime: Calendar
    private lateinit var myEndTime : Calendar
    private lateinit var currentDate : Date
    private lateinit var selectedItem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val Spinner = findViewById<Spinner>(R.id.projectspinner)
        // Filter the projectList based on the email
        val filteredProjects = ProjectManager.projectList.filter { it.email == userEmail }

        // Extract the project names from the filtered projects
        val projectNames = filteredProjects.map { it.projectName }

        // Set up ArrayAdapter to populate the spinner with the project names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, projectNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spinner.adapter = adapter

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

    private fun resetTimer() {
        stopTimer()
        val duration = getTimeStringFromDouble(time)
        myEndTime = calculateEndDate(myStartTime, duration)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val Spinner = findViewById<Spinner>(R.id.projectspinner)

        selectedItem = Spinner.selectedItem.toString()

        val startCalendar = myStartTime

        val endCalendar = myEndTime

        val durationInMillis = endCalendar.timeInMillis - startCalendar.timeInMillis

        val totalMinutes: Int = (durationInMillis / (1000 * 60)).toInt()

        val startTimeMillis = myStartTime.timeInMillis
        val theStartTime = Time(startTimeMillis)

        val endTimeMillis = myEndTime.timeInMillis
        val theEndTime = Time(endTimeMillis)


        if (userEmail != null) {
            val project =
                ProjectManager.projectList.find { it.projectName == selectedItem && it.email == userEmail }
            project?.totalHours = project?.totalHours?.plus(totalMinutes)!!

            val newEntry = TimesheetItems(
                timesheetID = Timesheetobj.timesheetlist.size,
                date = currentDate,
                startTime = theStartTime,
                endTime = theEndTime,
                duration = totalMinutes,
                decsrp = "",
                images = "",
                projectName = selectedItem, //fk project
                email = userEmail//fk user
            )
            Timesheetobj.timesheetlist.add(newEntry)
            Toast.makeText(this, "Time entry Added Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    fun calculateEndDate(startDateTime: Calendar, duration: String): Calendar {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val durationTime = format.parse(duration)
        val durationCalendar = Calendar.getInstance()
        durationCalendar.time = durationTime

        val endDateTime = startDateTime.clone() as Calendar
        endDateTime.add(Calendar.HOUR_OF_DAY, durationCalendar.get(Calendar.HOUR_OF_DAY))
        endDateTime.add(Calendar.MINUTE, durationCalendar.get(Calendar.MINUTE))
        endDateTime.add(Calendar.SECOND, durationCalendar.get(Calendar.SECOND))

        return endDateTime
    }

    private fun startStopTimer()
    {
        //has not started yet
        if(timerStartedNot == true){
            currentDate = Date()
            myStartTime = Calendar.getInstance()

            // Set the time of the Calendar instance to the value of myStartTime
            myStartTime.time = Time(currentDate.time)

            timerStartedNot = false
            val spinner = findViewById<Spinner>(R.id.projectspinner)
            spinner.isEnabled = false
        }
        if(paused)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.startStopButton.text = "Pause"
        binding.startStopButton.setCompoundDrawablesWithIntrinsicBounds(
            getDrawable(R.drawable.pause),
            null, null, null
        )
        paused = true
    }


    private fun stopTimer() {
        stopService(serviceIntent)
        binding.startStopButton.text = "Resume"
        binding.startStopButton.setCompoundDrawablesWithIntrinsicBounds(
            getDrawable(R.drawable.start),
            null, null, null
        )
        paused = false
    }


    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            binding.timeTV.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)
}