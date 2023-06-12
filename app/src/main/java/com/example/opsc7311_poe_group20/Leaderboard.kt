package com.example.opsc7311_poe_group20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class Leaderboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)


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