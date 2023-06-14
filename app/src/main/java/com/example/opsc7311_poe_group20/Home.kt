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
import androidx.appcompat.widget.AppCompatButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var items : MutableList<TimesheetItems>
    lateinit var adptr :ListViewTimesheetApdt

    private lateinit var mindate: Date
    private lateinit var Maxdate:Date

    var minHours : Int = 0
    var maxHours : Int = 0
    var totalTime : Int = 0

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

        var maxdatebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.maxiumdate_fab)
        var mindatebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.mindate_fab)

        mindatebtn.setOnClickListener {
            showDateMinPickerDialog(mindatebtn)
        }
        maxdatebtn.setOnClickListener {
            showDateMaxPickerDialog(maxdatebtn)
        }
        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")

        val user = UserManager.userList.find { it.email == userEmail }
        if (user != null) {
            minHours= user.min
            maxHours = user.max

            val mintxt = findViewById<TextView>(R.id.mintxt)
            mintxt.text = "Min: " + minHours + " Hour/s"

            val maxtxt = findViewById<TextView>(R.id.maxtxt)
            maxtxt.text = "Max: " + maxHours + " Hour/s"
        }

        val timesheetEntries = Timesheetobj.timesheetlist

        for (entry in timesheetEntries) {
            if(entry.email ==userEmail) {
                totalTime += entry.duration
            }
        }

        val myHours = totalTime / 60
        val myMinutes = totalTime % 60

        val total = findViewById<TextView>(R.id.totaltxt)
        total.text = "Total Time done today: $myHours Hour/s and $myMinutes Minute/s"

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // Other initialization code for your activity

        // Set the menu to the NavigationView
        navView.menu.clear()
        navView.inflateMenu(R.menu.nav_header_menu)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val timesheetBtn: View = findViewById(R.id.fab)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

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

        var searchbtn: View = findViewById(R.id.search)
        //var minidateBtn: View = findViewById(R.id.mindate_fab)
        //var maxiumdatebtn: View = findViewById(R.id.maxiumdate_fab)
        //var filterBtn: View = findViewById(R.id.search)
        //var lstTimesheet : ListView =findViewById(R.id.lvTimesheets)
        searchbtn.setOnClickListener {
            val filteredList = Timesheetobj.timesheetlist.filter { dataItem ->
                val itemDate = dataItem.date // Access the date property of your data item
                // Implement your filtering logic here based on minDate and maxDate
                itemDate in mindate..Maxdate
            }
            adptr.clear()
            adptr.addAll(filteredList)
            adptr.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDateMinPickerDialog(v: View) {
        val newFragment = MinDatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
    fun showDateMaxPickerDialog(v: View) {
        val newFragment = MaxDatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
    fun addMiniDateTo(date: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        val formattedDateStr = outputFormat.format(parsedDate)

        mindate = outputFormat.parse(formattedDateStr)

        val theDate = findViewById<TextView>(R.id.minidateview)
        theDate.text = outputFormat.format(mindate)
    }
    fun addMaxDateTo(date: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        val formattedDateStr = outputFormat.format(parsedDate)

        Maxdate = outputFormat.parse(formattedDateStr)

        val theDate = findViewById<TextView>(R.id.maxdateview)
        theDate.text = outputFormat.format(Maxdate)
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

class MinDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = android.icu.util.Calendar.getInstance()
        val year = c.get(android.icu.util.Calendar.YEAR)
        val month = c.get(android.icu.util.Calendar.MONTH)
        val day = c.get(android.icu.util.Calendar.DAY_OF_MONTH)
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val selectedDate = "$year-${month + 1}-$day"
        (requireActivity() as Home).addMiniDateTo(selectedDate)
    }
}

class MaxDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = android.icu.util.Calendar.getInstance()
        val year = c.get(android.icu.util.Calendar.YEAR)
        val month = c.get(android.icu.util.Calendar.MONTH)
        val day = c.get(android.icu.util.Calendar.DAY_OF_MONTH)
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val selectedDate = "$year-${month + 1}-$day"
        (requireActivity() as Home).addMaxDateTo(selectedDate)
    }
}