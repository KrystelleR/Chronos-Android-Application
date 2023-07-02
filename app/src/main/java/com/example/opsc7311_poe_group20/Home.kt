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
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var items : MutableList<TimesheetItems>
    lateinit var adptr :ListViewTimesheetApdt

    private lateinit var mindate: Date
    private lateinit var Maxdate:Date

    var minHours : Int = 0
    var maxHours : Int = 0
    var totalTime : Int = 0
    var badgeCount: Int = 1

    val tipList: MutableList<String> = mutableListOf()

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

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val userName = sharedPreferences.getString("name", "")
        if (userEmail != null) {
            checkEarnBadge(userEmail)
        }

        if(AllBadgesObj.AllBadgeslist.size !=5) {
            populateBadge()
        }

//tip of the day
        tipList.add("Start each day by identifying the most important tasks you need to accomplish and focus on those first.")
        tipList.add("Large tasks can be overwhelming, so break them down into smaller, more manageable steps to make progress easier.")
        tipList.add("Clearly define what you want to achieve and set specific, measurable goals to track your progress.")
        tipList.add("Minimize interruptions by turning off notifications, closing unnecessary tabs, and creating a quiet work environment.")
        tipList.add("Allocate specific time blocks for different tasks or activities to ensure better time management and prevent multitasking.")
        tipList.add("Give yourself short breaks throughout the day to recharge and maintain your focus and productivity.")
        tipList.add("If possible, delegate tasks or outsource certain activities to others, allowing you to focus on high-priority responsibilities.")
        tipList.add("Work in focused, 25-minute intervals (called Pomodoros) followed by short breaks to enhance concentration and productivity.")
        tipList.add("Explore task management apps, note-taking tools, or project management software to streamline your workflow and stay organized.")
        tipList.add("Prioritize self-care, get enough sleep, eat well, and exercise regularly. A healthy mind and body contribute to increased productivity.")

        val tipTextView = findViewById<TextView>(R.id.tiptxt)
        val randomTip = tipList.random()
        tipTextView.text = randomTip

        val tipIndex = tipList.indexOf(randomTip) + 1
        val tipNum = findViewById<TextView>(R.id.tipNumbertxt)
        tipNum.text = "Productivity Tip #"+ tipIndex


        var maxdatebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.maxiumdate_fab)
        var mindatebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.mindate_fab)

        val myTip = findViewById<TextView>(R.id.tiptxt)
        myTip.isSelected = true

        updateCardVisibility()

        mindatebtn.setOnClickListener {
            showDateMinPickerDialog(mindatebtn)
        }
        maxdatebtn.setOnClickListener {
            showDateMaxPickerDialog(maxdatebtn)
        }


        val greeting = findViewById<TextView>(R.id.greetingtxt)
        greeting.text = "Welcome back, " + userName

        val user = UserManager.userList.find { it.email == userEmail }
        if (user != null) {
            minHours= user.min
            maxHours = user.max

            val mintxt = findViewById<TextView>(R.id.mintxt)
            mintxt.text = "Min: " + minHours + " hrs"

            val maxtxt = findViewById<TextView>(R.id.maxtxt)
            maxtxt.text = "Max: " + maxHours + " hrs"
        }

        val timesheetEntries = Timesheetobj.timesheetlist



        for (entry in timesheetEntries) {
            // Inside your function or method
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            val formattedDate = dateFormat.format(entry.date)
            Log.d("tag", formattedDate)

            val currentDate = Date()
            val formattedCurrentDate = dateFormat.format(currentDate)
            Log.d("tag", formattedCurrentDate)

            if(entry.email ==userEmail) {
                if(formattedDate == formattedCurrentDate) {
                    totalTime += entry.duration
                }
            }
        }

        val myHours = totalTime / 60
        val myMinutes = totalTime % 60

        val total = findViewById<TextView>(R.id.totaltxt)
        total.text = "$myHours Hour/s and $myMinutes Minute/s"

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
        updateCardVisibility()
        if (userEmail != null) {
            checkEarnBadge(userEmail)
        }


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

    private fun updateCardVisibility() {
        val card = findViewById<CardView>(R.id.new_userID)
        val scroll = findViewById<ScrollView>(R.id.myScrollView)
        if (Timesheetobj.timesheetlist.isEmpty()) {
            card.visibility = View.VISIBLE
            scroll.visibility = View.GONE

            card.setOnClickListener {
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
            }
        } else {
            card.visibility = View.GONE
            scroll.visibility = View.VISIBLE
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










    private fun checkEarnBadge(email: String) {
        val myBadge = OwnBadgesObj.ownBadgeslist.find { it.email == email }
        val entries = Timesheetobj.timesheetlist.find { it.email == email }

        // badge1 (at least 1 entry)
        if (myBadge != null && entries != null && !myBadge.badge1) {
            myBadge.badge1 = true
            val badge1 = AllBadgesObj.AllBadgeslist.firstOrNull { it.number == 1 }
            if (badge1 != null) {
                viewMyBadge(badge1.badgeTitle, badge1.image, badge1.desc)
            }
        }

        // badge2 (an entry with 2+ hours duration)
        if (myBadge != null && entries != null && !myBadge.badge2) {
            var found : Boolean = false
            val AllEntries: List<TimesheetItems> = Timesheetobj.timesheetlist.filter { it.email == email }

            for(entry in AllEntries){
                if (entry.duration >= 120) {
                    found = true
                    break
                }
            }
            if (found == true) {
                myBadge.badge2 = true
                val badge2 = AllBadgesObj.AllBadgeslist.firstOrNull { it.number == 2 }
                if (badge2 != null) {
                    viewMyBadge(badge2.badgeTitle, badge2.image, badge2.desc)
                }
            }
        }

        // badge3 ( 5 or more entries in a single day)
        if (myBadge != null && !myBadge.badge3) {
            val entryGroups = Timesheetobj.timesheetlist
                .filter { it.email == email }
                .groupBy { "group" } // Grouping by a constant value

            val dateCounts = entryGroups.filter { it.value.size >= 5 }

            if (dateCounts.isNotEmpty()) {
                myBadge.badge3 = true
                val badge3 = AllBadgesObj.AllBadgeslist.firstOrNull { it.number == 3 }
                if (badge3 != null) {
                    viewMyBadge(badge3.badgeTitle, badge3.image, badge3.desc)
                }
            }
        }

        // badge4 (Meet the minimum daily goal for 10 consecutive days)
        if (myBadge != null && !myBadge.badge4) {
            val durationThreshold = 250 // Total duration threshold in minutes
            val consecutiveDays = 10 // Number of consecutive days to check

            val allDates = Timesheetobj.timesheetlist
                .filter { it.email == email }
                .map { it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }
                .distinct()
                .sorted()

            var count = 0
            var previousDate: LocalDate? = null

            for (date in allDates) {
                if (previousDate != null && date == previousDate.plusDays(1)) {
                    val totalDuration = Timesheetobj.timesheetlist
                        .filter {
                            it.email == email && it.date.toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate() == date
                        }
                        .sumOf { it.duration }

                    if (totalDuration >= durationThreshold) {
                        count++
                        if (count == consecutiveDays) {
                            myBadge.badge4 = true
                            val badge4 = AllBadgesObj.AllBadgeslist.firstOrNull { it.number == 4 }
                            if (badge4 != null) {
                                badgeCount++
                                viewMyBadge(
                                    badge4.badgeTitle,
                                    badge4.image,
                                    badge4.desc
                                )
                                break
                            }
                        }
                    } else {
                        count = 0
                    }
                } else {
                    count = 1
                }

                previousDate = date
            }
        }

        // badge5 (Create and manage 5 projects simultaneously)
        if (myBadge != null && !myBadge.badge5) {
            val AllProjects: List<Project> = ProjectManager.projectList.filter { it.email == email }
            if(AllProjects.size >= 5){
                myBadge.badge5 = true
                val badge5 = AllBadgesObj.AllBadgeslist.firstOrNull { it.number == 5 }
                if (badge5 != null) {
                    viewMyBadge(badge5.badgeTitle, badge5.image, badge5.desc)
                }
            }
        }

        badgeCount = 1
    }


    private fun viewMyBadge(name : String, theImage : Int, description : String) {
        val viewBadge = Dialog(this)
        viewBadge.setContentView(R.layout.activity_badge)

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val userName = sharedPreferences.getString("name", "")

        val userEarned = viewBadge.findViewById<TextView>(R.id.usernametxt)
        userEarned.text = userName + " earned the"

        val title = viewBadge.findViewById<TextView>(R.id.badgenametxt)
        title.text = name + " Badge"

        val image =  viewBadge.findViewById<ImageView>(R.id.badgeimg)
        image.setImageResource(theImage)

        val details = viewBadge.findViewById<TextView>(R.id.desctxt)
        details.text = description

        var Count = BadgesObj.Badgeslist.size +1
        val myCount = viewBadge.findViewById<TextView>(R.id.collectedtxt)
        myCount.text = "Collected: "  + Count + "/5 Badges"

        val Badge = userEmail?.let {
            myBadges(
                number = (BadgesObj.Badgeslist.size+1),
                badgeTitle = name,
                desc = description,
                image = theImage,
                email = it
            )
        }
        if (Badge != null) {
            BadgesObj.Badgeslist.add(Badge)
        }

        viewBadge.show()

        val okay = viewBadge.findViewById<Button>(R.id.okayButton)
        okay.setOnClickListener(){
            viewBadge.hide()
        }
    }

    private fun populateBadge() {

        val Badge1 =AllBadges(
            number = (AllBadgesObj.AllBadgeslist.size+1),
            badgeTitle = "Time Apprentice",
            desc = "Add your 1st time entry",
            image = R.drawable.badge1
        )
        AllBadgesObj.AllBadgeslist.add(Badge1)

        val Badge2 =AllBadges(
            number = (AllBadgesObj.AllBadgeslist.size+1),
            badgeTitle = "Focused Workaholic",
            desc = "Work for more than 2 hours in a single session",
            image = R.drawable.badge2
        )
        AllBadgesObj.AllBadgeslist.add(Badge2)

        val Badge3 =AllBadges(
            number = (AllBadgesObj.AllBadgeslist.size+1),
            badgeTitle = "Productivity Guru",
            desc = "Add 5 or more entries in a single day",
            image = R.drawable.badge3
        )
        AllBadgesObj.AllBadgeslist.add(Badge3)

        val Badge4 =AllBadges(
            number = (AllBadgesObj.AllBadgeslist.size+1),
            badgeTitle = "Goal Crusher",
            desc = "Meet the minimum daily goal for 10 consecutive days",
            image = R.drawable.badge4
        )
        AllBadgesObj.AllBadgeslist.add(Badge4)

        val Badge5 =AllBadges(
            number = (AllBadgesObj.AllBadgeslist.size+1),
            badgeTitle = "Master Planner",
            desc = "Create and manage 5 projects simultaneously",
            image = R.drawable.badge5
        )
        AllBadgesObj.AllBadgeslist.add(Badge5)
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
                if(ProjectManager.projectList.size == 0){
                    // Handle item1 click
                    val intent1 = Intent(this, AddProject::class.java)
                    startActivity(intent1)
                    Toast.makeText(this, "Add a project first" , Toast.LENGTH_SHORT).show()
                }
                else{
                    val intent3 = Intent(this, Stopwatch::class.java)
                    startActivity(intent3)
                }
                true
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

                if(ProjectManager.projectList.size == 0){
                    val intent1 = Intent(this, AddProject::class.java)
                    startActivity(intent1)
                    Toast.makeText(this, "Add a project first" , Toast.LENGTH_SHORT).show()
                }
                else{
                    // Handle item6 click
                    val intent6 = Intent(this, Pomodoro::class.java)
                    startActivity(intent6)
                }
                true

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