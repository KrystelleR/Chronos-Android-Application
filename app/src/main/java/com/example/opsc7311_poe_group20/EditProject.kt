package com.example.opsc7311_poe_group20

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import java.util.*

class EditProject : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project)

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val projectID = sharedPreferences.getInt("projectID", -1)
       val Project = ProjectManager.projectList


        var priority = findViewById<Spinner>(R.id.prioritySpinner)
        var client = findViewById<TextView>(R.id.clienttxt)
        var billable = findViewById<TextView>(R.id.billabletxt)
        var rate = findViewById<TextView>(R.id.ratetxt)
        var hours = findViewById<TextView>(R.id.hours)
        var minutes = findViewById<TextView>(R.id.minutes)
        var min = findViewById<TextView>(R.id.mintxt)
        var max = findViewById<TextView>(R.id.maxtxt)
        var colour = findViewById<Spinner>(R.id.colourSpinner)
        var projectName = findViewById<TextView>(R.id.editprojecttxt)


        if (Project != null) {
            //projectName.text = ProjectManager.projectList[AllProjects.clickedItemPosition].ProjectName

            //priority.text = ProjectManager.projectList[AllProjects.clickedItemPosition].ProjectPriority
            client.text = ProjectManager.projectList[AllProjects.clickedItemPosition].clientName
            if(ProjectManager.projectList[AllProjects.clickedItemPosition].isBillable) {
                billable.text = "Yes"
            }
            else{
                billable.text = "No"
            }
            /*if (billable.text=="No"){
                rate.text="0.0"
            }else{
                rate.text = (ProjectManager.projectList[AllProjects.clickedItemPosition].Rate).toString()
            }*/
            rate.text = (ProjectManager.projectList[AllProjects.clickedItemPosition].rate).toString()
            min.text = (ProjectManager.projectList[AllProjects.clickedItemPosition].minimum_goal).toString()
            max.text = (ProjectManager.projectList[AllProjects.clickedItemPosition].maximum_goal).toString()

            val totalDuration = ProjectManager.projectList[AllProjects.clickedItemPosition].totalHours.toInt()

            val myHours: Int = totalDuration / 60
            val myMinutes: Int = totalDuration % 60

            hours.text = myHours.toString() + " hours"
            minutes.text = myMinutes.toString() + " minutes"

            //project color spinner
            val colorResourceId = getColorResourceId(ProjectManager.projectList[AllProjects.clickedItemPosition].projectColor)

            var colourSpinner = findViewById<Spinner>(R.id.colourSpinner)
            val colours = resources.getStringArray(R.array.colours)

            val colorAdapter =
                ColouredSpinnerAdapter(this, android.R.layout.simple_spinner_item, colours)
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            colourSpinner.adapter = colorAdapter

            var colourPicked = ProjectManager.projectList[AllProjects.clickedItemPosition].projectColor
            val selectedPosition = colours.indexOf(colourPicked)
            if (selectedPosition != -1) {
                colourSpinner.setSelection(selectedPosition)
            }

            //priority spinner
            var prioritySpinner = findViewById<Spinner>(R.id.prioritySpinner)
            val priority = resources.getStringArray(R.array.priority)

            val priorityArray = resources.getStringArray(R.array.priority)
            val priorityAdapter = PriorityAdapter(
                this,
                android.R.layout.simple_spinner_item,
                priorityArray,
                prioritySpinner
            )
            prioritySpinner.adapter = priorityAdapter

            var priorityPicked = ProjectManager.projectList[AllProjects.clickedItemPosition].projectPriority
            val selectedPositionPri = priority.indexOf(priorityPicked)
            if (selectedPositionPri != -1) {
                prioritySpinner.setSelection(selectedPositionPri)
            }

            //save changes made by capturing user input
            val btnApplyEditProject = findViewById<Button>(R.id.btnApplyEdit)
            btnApplyEditProject.setOnClickListener {

                projectName.text = "Edit Project: " + (ProjectManager.projectList[AllProjects.clickedItemPosition].projectName)
                ProjectManager.projectList[AllProjects.clickedItemPosition].clientName=client.text.toString()
                ProjectManager.projectList[AllProjects.clickedItemPosition].rate=rate.text.toString().toDouble()
                ProjectManager.projectList[AllProjects.clickedItemPosition].projectColor= colourSpinner.selectedItem.toString()
                ProjectManager.projectList[AllProjects.clickedItemPosition].projectPriority= prioritySpinner.selectedItem.toString()
                ProjectManager.projectList[AllProjects.clickedItemPosition].minimum_goal= min.text.toString().toInt()
                ProjectManager.projectList[AllProjects.clickedItemPosition].maximum_goal=max.text.toString().toInt()

                val intentAllProj = Intent(this,AllProjects::class.java)
                startActivity(intentAllProj)
                var t = Toast.makeText(this,"Project Updated successfully", Toast.LENGTH_SHORT).show()

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
}