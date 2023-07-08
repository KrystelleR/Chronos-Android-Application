package com.example.opsc7311_poe_group20

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class timesheetDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_details)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = Timesheetobj.timesheetlist[Home.clickedItemPosition].date
        val startTime = Timesheetobj.timesheetlist[Home.clickedItemPosition].startTime
        val endTime = Timesheetobj.timesheetlist[Home.clickedItemPosition].endTime

        val formattedDate = dateFormat.format(date)
        val formattedStartTime = timeFormat.format(startTime)
        val formattedEndTime = timeFormat.format(endTime)

        val start = findViewById<TextView>(R.id.startview)
        start.text = formattedStartTime

        val end = findViewById<TextView>(R.id.endview)
        end.text = formattedEndTime

        val theDate = findViewById<TextView>(R.id.dateview)
        theDate.text = formattedDate

        val theHours = findViewById<TextView>(R.id.hours)
        val theMinutes = findViewById<TextView>(R.id.minutes)

        val duration = Timesheetobj.timesheetlist[Home.clickedItemPosition].duration

        val totalHours: Int = duration / 60
        val totalMinutes: Int = duration % 60

        theHours.text = totalHours.toString() + " hour/s"
        theMinutes.text = totalMinutes.toString() + " minute/s"

        val project = findViewById<TextView>(R.id.selecedProjecttxt)
        project.text = Timesheetobj.timesheetlist[Home.clickedItemPosition].projectName

        val description = findViewById<TextView>(R.id.descriptiontxt)

        if(Timesheetobj.timesheetlist[Home.clickedItemPosition].decsrp == ""){
            description.text = "No Description"
        }
        else{
            description.text =Timesheetobj.timesheetlist[Home.clickedItemPosition].decsrp
        }


        var theImage = findViewById<ImageView>(R.id.projimage)
        var image = Timesheetobj.timesheetlist[Home.clickedItemPosition].images

        if(image != ""){
            if (!image.isNullOrEmpty()) {
                val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
                val decodedBitmap: Bitmap? = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                if (decodedBitmap != null) {
                    theImage.setImageBitmap(decodedBitmap)
                } else {
                    // Failed to decode bitmap
                    theImage.setImageResource(R.drawable.avatar)
                }
            } else {
                theImage.setImageResource(R.drawable.avatar)
            }
        }
        else{
            theImage.visibility = View.GONE
        }

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