package com.example.opsc7311_poe_group20

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

var selected: String = ""
var checkName: Boolean = false
class AddProject: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        val database = Firebase.database
        val addProjects = database.getReference("Projects")

        val switch = findViewById<Switch>(R.id.billableSwitch)
        val layout = findViewById<LinearLayout>(R.id.myLayout)
        val nameEditText = findViewById<TextView>(R.id.nametxt)

        layout.visibility = View.GONE

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                layout.visibility = View.VISIBLE
            } else {
                layout.visibility = View.GONE
            }
        }

        val colourSpinner = findViewById<Spinner>(R.id.colourSpinner)
        val colours = resources.getStringArray(R.array.colours)
        val colorAdapter =
            ColouredSpinnerAdapter(this, android.R.layout.simple_spinner_item, colours)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colourSpinner.adapter = colorAdapter

        val prioritySpinner = findViewById<Spinner>(R.id.prioritySpinner)
        val priorityArray = resources.getStringArray(R.array.priority)
        val priorityAdapter = PriorityAdapter(
            this,
            android.R.layout.simple_spinner_item,
            priorityArray,
            prioritySpinner
        )
        prioritySpinner.adapter = priorityAdapter

        val mintxt = findViewById<TextView>(R.id.mintxt)
        val minSeekBar = findViewById<SeekBar>(R.id.minSeekBar)

        minSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mintxt.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example, but you can add any necessary actions here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example, but you can add any necessary actions here
            }
        })

        val maxtxt = findViewById<TextView>(R.id.maxtxt)
        val maxSeekBar = findViewById<SeekBar>(R.id.maxSeekBar)

        maxSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                maxtxt.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example, but you can add any necessary actions here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed for this example, but you can add any necessary actions here
            }
        })

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Get SharedPreferences instance
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

                val userEmail = sharedPreferences.getString("email", "")
                val projectName = s.toString()
                if(userEmail != null) {
                    if (isProjectTaken(projectName, userEmail)) {
                        nameEditText.error = "You already have a project with this name"
                        checkName = false
                    } else {
                        nameEditText.error = null // Clear the error
                        checkName = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        val addButton = findViewById<Button>(R.id.btnCreateProject)

        addButton.setOnClickListener {

            // Get SharedPreferences instance
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            val userEmail = sharedPreferences.getString("email", "")

            val projectName = (findViewById<EditText>(R.id.nametxt)).text.toString()
            val priority = prioritySpinner.selectedItem.toString()
            val clientName = (findViewById<EditText>(R.id.clienttxt)).text.toString()
            val colourPicked = colourSpinner.selectedItem.toString()
            val rateText = (findViewById<EditText>(R.id.ratetxt)).text.toString()
            val minText = (findViewById<TextView>(R.id.mintxt)).text.toString()
            val maxText = (findViewById<TextView>(R.id.maxtxt)).text.toString()
            val billable = findViewById<Switch>(R.id.billableSwitch).isChecked

            if (checkName && minText.isNotEmpty() && maxText.isNotEmpty() && projectName.isNotEmpty() && clientName.isNotEmpty()) {
                var rateAmount = rateText.toDouble()
                val min = minText.toInt()
                val max = maxText.toInt()

                if(min <  max){
                    if (userEmail != null) {

                        if(billable == false){
                            rateAmount = 0.0
                        }
                        val newProject = Project(
                            projectName = projectName,
                            projectPriority = priority,
                            clientName = clientName,
                            isBillable = billable,
                            rate = rateAmount,
                            projectColor = colourPicked,
                            maximum_goal = max,
                            minimum_goal = min,
                            email = userEmail,
                            totalHours = 0
                        )

                        addProjects.push().setValue(newProject)


                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("projectID", 2)
                        editor.apply()


                        val intent = Intent(this, AllProjects::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    Toast.makeText(this, "Min hours cannot be more than max hours", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show()
            }
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

    private fun isProjectTaken(name: String, email: String): Boolean {
        val project = ProjectManager.projectList.find { it.projectName == name && it.email == email }
        return project != null
    }


}