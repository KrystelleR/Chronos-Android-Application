package com.example.opsc7311_poe_group20

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllProjects : AppCompatActivity() {


    lateinit var items : MutableList<Project>
    var adptr: ListViewAdapter? = null
    lateinit var lvProject: ListView


    companion object {

        var clickedItemPosition: Int = -1
    }

    val database = Firebase.database
    val getProject = database.getReference("Projects")
    var projectList = mutableListOf<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_projects)
        lvProject = findViewById<ListView>(R.id.lvMyProjects)
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
        //btn to navigate to next activity
        var btnNewProj = findViewById<Button>(R.id.btnNewProject)
        btnNewProj.setOnClickListener {
            val intent = Intent(this, AddProject::class.java)
            startActivity(intent)
        }

        //read from database
        getProject.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle the data change event
                //iterate over children in the list
                for (pulledUser in snapshot.children) {
                    val project: Project? = pulledUser.getValue(Project::class.java)
                    if (project != null) {
                        projectList.add(project)
                        ProjectManager.projectList.add(project)
                        Log.d("ProjectName", project.projectName)
                    }
                }

// Set project list to items and pass as a parameter of ListViewAdapter object.
                items = projectList

// Adapter holds data that ListView will display. ListViewAdapter is our custom adapter class.
                adptr = ListViewAdapter(applicationContext, items)

// Set the adapter for the ListView
                lvProject.adapter = adptr

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation event for project retrieval
                Toast.makeText(this@AllProjects, "Error retrieving projects", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        lvProject.setOnItemClickListener { parent, view, i, id ->
            var projName : String = items[i].projectName
            Toast.makeText(this, projName, Toast.LENGTH_SHORT).show()
        }


/*

        //click methods. short and long. Position is the index of the item that was clicked
        lvProject.setOnItemClickListener { parent, view, i, id ->
            var projName : String = items[i].ProjectName
            makeToast("$projName")
            clickedItemPosition=i
            //navigate to details page on short click
            val intentDetails = Intent(this,ProjectDetails::class.java)
            startActivity(intentDetails)
        }

        lvProject.setOnItemLongClickListener { adapterView, view, i, l ->
            makeToast(" ${items[i].ProjectName} removed")
            //alert when deleting a project
            showAlert(this,"Delete","Are you sure you want to Delete this project?",i)
            //to see updated results after long click
            lvProject.adapter = adptr

            true
        }

        //to see updated results
        lvProject.adapter = adptr



    }

    //alert for deleting a project
    fun showAlert(context: Context, title: String, message: String, index : Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        // Set the title and message
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
            // Action to perform when the positive button is clicked
            removeItem(index)
            adptr.notifyDataSetChanged()//update listview
        }

        // Set a negative button and its click listener
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // Action to perform when the negative button is clicked
            val intent = Intent(this,AllProjects::class.java)
            startActivity(intent)
        }

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun makeToast(s:String) {

        var t = Toast.makeText(this,"$s click",Toast.LENGTH_SHORT).show()

    }

    //method to remove an item/project from the list if long pressed
    //make static to access in other classes

    fun removeItem(i : Int){
        items.removeAt(i)
        adptr.notifyDataSetChanged()
        //could also say lvProject.adapter = adptr
    }

    fun navToEdit(position: Int) : Int{
        val intent = Intent(this,EditProject::class.java)
        startActivity(intent)
        return position
    }

*/
    }
}