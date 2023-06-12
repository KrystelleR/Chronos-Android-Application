package com.example.opsc7311_poe_group20

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var items : MutableList<TimesheetItems>
    lateinit var adptr :ListViewTimesheetApdt

    companion object {

        var clickedItemPosition: Int = -1
    }

    // the lateint means we will inti later
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var profileImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // Other initialization code for your activity

        // Set the menu to the NavigationView
        navView.menu.clear()
        navView.inflateMenu(R.menu.nav_header_menu)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val timesheetBtn: View = findViewById(R.id.fab)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        // Retrieve email value from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val name = sharedPreferences.getString("name", "")
        val profilePicture = sharedPreferences.getString("profilePicture", null)

        // Set email value to TextView in nav_header.xml
        val headerView = navView.getHeaderView(0)
        val emailTextView: TextView = headerView.findViewById(R.id.emailtxt)
        emailTextView.text = userEmail

        // Set name value to TextView in nav_header.xml
        val nameView: TextView = headerView.findViewById(R.id.user_name)
        nameView.text = name

        // Load and set the profile picture
        profileImageView = headerView.findViewById(R.id.profile_image)
        if (!profilePicture.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(profilePicture, Base64.DEFAULT)
            val decodedBitmap: Bitmap? =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            if (decodedBitmap != null) {
                profileImageView.setImageBitmap(decodedBitmap)
            } else {
                // Failed to decode bitmap
                profileImageView.setImageResource(R.drawable.avatar)
            }
        } else {
            profileImageView.setImageResource(R.drawable.avatar)
        }

        timesheetBtn.setOnClickListener {
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

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var lvTimesheet : ListView =findViewById(R.id.lvTimesheets)
        //set project list to items and pass as param of ListViewAdapter object.
        items = Timesheetobj.timesheetlist

        //adapter holds data that listview will display. ListViewAdapter is our custom adapter class we made
        adptr = ListViewTimesheetApdt(this, items)

        //click methods. short and long. Position is the index of the item that was clicked
        lvTimesheet.setOnItemClickListener { parent, view, i, id ->
            var timeentryName : Int = items[i].timesheetID
            Home.clickedItemPosition =i
            //navigate to details page on short click
            val intentDetails = Intent(this,timesheetDetails::class.java)
            startActivity(intentDetails)
        }

        //to see updated results
        lvTimesheet.adapter = adptr
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                // Handle item1 click
                val intent1 = Intent(this, Home::class.java)
                startActivity(intent1)
            }
            R.id.item2 -> {
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
            R.id.item3 -> {
                // Handle item3 click
                val intent3 = Intent(this, Stopwatch::class.java)
                startActivity(intent3)
            }
            R.id.item4 -> {
                // Handle item4 click
                val intent4 = Intent(this, AllProjects::class.java)
                startActivity(intent4)
            }
            R.id.item5 -> {
                // Handle item5 click
                val intent5 = Intent(this, Report::class.java)
                startActivity(intent5)
            }
            R.id.item6 -> {
                // Handle item6 click
                val intent6 = Intent(this, Pomodoro::class.java)
                startActivity(intent6)
            }
            R.id.item7 -> {
                // Handle item7 click
                val intent7 = Intent(this, Profile::class.java)
                startActivity(intent7)
            }
            R.id.item8 -> {
                // Handle item8 click
                val intent8 = Intent(this, Calendar::class.java)
                startActivity(intent8)
            }
            R.id.item9 -> {
                // Handle item9 click
                val intent9 = Intent(this, Leaderboard::class.java)
                startActivity(intent9)
            }
            R.id.item10 -> {
                // Handle item10 click
                val intent10 = Intent(this, SignIn::class.java)
                startActivity(intent10)
            }

            // Handle other menu items here

            // Return true to indicate that the item selection has been handled
            else -> return true
        }

        // Return false to indicate that the item selection has not been handled
        return false
    }
}


