package com.example.opsc7311_poe_group20

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

class Pomodoro : AppCompatActivity() {

    private var timeSelected: Int = 0
    private var breakSelected: Int = 0
    private var roundsSelected: Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true
    private var roundsLeft: Int = 0

    private var originalTime: Int = 0
    private var totalRounds: Int = 0

    private lateinit var myStartTime: Calendar
    private lateinit var myEndTime: Calendar
    private lateinit var currentDate: Date
    private lateinit var selectedItem: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        val breakGames = findViewById<Button>(R.id.breakGamesbtn)
        breakGames.visibility = View.GONE

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
                    if (ProjectManager.projectList.size == 0) {
                        // Handle item1 click
                        val intent1 = Intent(this, AddProject::class.java)
                        startActivity(intent1)
                        Toast.makeText(this, "Add a project first", Toast.LENGTH_SHORT).show()
                    } else {
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

        val addBtn: Button = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            setTimeFunction()
        }
        val startBtn: Button = findViewById(R.id.btnPlayPause)
        startBtn.setOnClickListener {
            startTimerSetup()
        }

    }

    private fun resetTime() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
            timeProgress = 0
            timeSelected = 0
            pauseOffSet = 0
            timeCountDown = null
            val startBtn: Button = findViewById(R.id.btnPlayPause)
            startBtn.text = "Start"
            isStart = true
            val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
            progressBar.progress = 0
            val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
            timeLeftTv.text = "0" + ":00"
        }
    }

    private fun timePause() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
        }
    }

    private fun startTimerSetup() {
        val startBtn: Button = findViewById(R.id.btnPlayPause)
        val midText = findViewById<TextView>(R.id.midTxt)
        if (timeSelected > timeProgress) {
            if (isStart) {
                currentDate = Date()
                myStartTime = Calendar.getInstance()

                // Set the time of the Calendar instance to the value of myStartTime
                myStartTime.time = Time(currentDate.time)

                val spinner = findViewById<Spinner>(R.id.projectspinner)
                spinner.isEnabled = false

                midText.text = " minutes left"
                startBtn.text = "Pause"
                startTimer(pauseOffSet)
                isStart = false
            } else {
                midText.text = "Ready?"
                isStart = true
                startBtn.text = "Resume"
                timePause()
            }
        } else {
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffSetL: Long) {
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        progressBar.progress = timeProgress

        val startBtn: Button = findViewById(R.id.btnPlayPause)
        val midText = findViewById<TextView>(R.id.midTxt)

        startBtn.text = "Pause"
        midText.text = "minutes left"

        timeCountDown = object : CountDownTimer(
            (timeSelected * 1000).toLong() - pauseOffSetL * 1000, 1000
        ) {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong() - p0 / 1000
                progressBar.progress = timeSelected - timeProgress
                val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
                timeLeftTv.text = (timeSelected - timeProgress).toString()  + ":00"
            }

            override fun onFinish() {
                if (roundsLeft == 0) {
                    Toast.makeText(this@Pomodoro, "Pomodoro completed!", Toast.LENGTH_SHORT).show()
                    resetTime()

                } else {
                    showBreakAlert()
                }
            }
        }.start()
    }

    //method named firstTimeStart set startDate and then boolean=true at end so that only runs once

    fun calculateEndDate(startDateTime: Calendar, duration: Int): Calendar {
        val endDateTime = startDateTime.clone() as Calendar
        endDateTime.add(Calendar.MINUTE, duration)
        return endDateTime
    }



    private fun showBreakAlert() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Pomodoro Completed")
            .setMessage("Time for a break!")
            .setPositiveButton("Start Break") { dialog: DialogInterface?, which: Int ->
                startBreakTimer()
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                // Handle cancel
            }
            .setCancelable(false)
            .create()

        val breakGames = findViewById<Button>(R.id.breakGamesbtn)
        breakGames.visibility = View.VISIBLE

        alertDialog.show()
    }

    private fun startBreakTimer() {
        timeProgress = 0
        pauseOffSet = 0
        roundsLeft--
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        progressBar.max = breakSelected

        val startBtn: Button = findViewById(R.id.btnPlayPause)
        val midText = findViewById<TextView>(R.id.midTxt)

        startBtn.text = "Pause"
        midText.text = "Take a break!"

        val breakGames = findViewById<Button>(R.id.breakGamesbtn)

        breakGames.setOnClickListener(){

            val menu = Dialog(this)
            menu.setContentView(R.layout.breakgamesmenu)
            menu.show()

            val play = menu.findViewById<Button>(R.id.playBtn)
            play.setOnClickListener(){
                val intent = Intent(this, snake::class.java)
                startActivity(intent)
                menu.hide()

            }
        }

        timeCountDown = object : CountDownTimer((breakSelected * 1000).toLong(), 1000) {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = breakSelected.toLong() - p0 / 1000
                progressBar.progress = breakSelected - timeProgress
                val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
                timeLeftTv.text = (breakSelected - timeProgress).toString() + ":00"
            }

            override fun onFinish() {
                if (roundsLeft == 0) {

                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val userEmail = sharedPreferences.getString("email", "")

                    val Spinner = findViewById<Spinner>(R.id.projectspinner)

                    selectedItem = Spinner.selectedItem.toString()

                    val duration = originalTime * totalRounds //this is in minutes
                    myEndTime = calculateEndDate(myStartTime, duration)

                    val startCalendar = myStartTime
                    val endCalendar = myEndTime

                    val startTimeMillis = myStartTime.timeInMillis
                    val theStartTime = Time(startTimeMillis)

                    val endTimeMillis = myEndTime.timeInMillis
                    val theEndTime = Time(endTimeMillis)

                    Log.d("Tag", theStartTime.toString())
                    Log.d("Tag", theEndTime.toString())
                    Log.d("Tag", duration.toString())
                    Log.d("Tag",selectedItem)

                    val newEntry = userEmail?.let {
                        TimesheetItems(
                            timesheetID = Timesheetobj.timesheetlist.size,
                            date = currentDate,
                            startTime = theStartTime,
                            endTime = theEndTime,
                            duration = duration,
                            decsrp = "",
                            images = "",
                            projectName = selectedItem, //fk project
                            email = it//fk user
                        )
                    }


                    if (newEntry != null) {
                        Timesheetobj.timesheetlist.add(newEntry)
                    }

                    resetTime()
                    Toast.makeText(this@Pomodoro, "Pomodoro completed!", Toast.LENGTH_SHORT).show()
                } else {
                    showWorkAlert()
                }
            }
        }.start()
    }

    private fun showWorkAlert() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Break Completed")
            .setMessage("Time to get back to work!")
            .setPositiveButton("Start Work") { dialog: DialogInterface?, which: Int ->
                timeSelected = originalTime
                timeProgress = 0
                pauseOffSet = 0
                startTimer(pauseOffSet)
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                // Handle cancel
            }
            .setCancelable(false)
            .create()

        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        progressBar.max = originalTime

        val breakGames = findViewById<Button>(R.id.breakGamesbtn)
        breakGames.visibility = View.GONE

        alertDialog.show()
    }


    private fun setTimeFunction() {
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        val breakSet = timeDialog.findViewById<EditText>(R.id.etGetBreakLength)
        val roundsSet = timeDialog.findViewById<EditText>(R.id.etGetRounds)
        val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
        val btnStart: Button = findViewById(R.id.btnPlayPause)
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)

        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty() || breakSet.text.isEmpty() || roundsSet.text.isEmpty()) {
                Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show()
            } else {
                resetTime()
                timeLeftTv.text = timeSet.text
                btnStart.text = "Start"
                timeSelected = timeSet.text.toString().toInt()

                originalTime = timeSet.text.toString().toInt()

                breakSelected = breakSet.text.toString().toInt()
                roundsSelected = roundsSet.text.toString().toInt()

                totalRounds = roundsSet.text.toString().toInt()

                roundsLeft = roundsSelected
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress = 0
        }
    }

}