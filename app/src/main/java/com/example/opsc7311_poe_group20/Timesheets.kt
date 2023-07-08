package com.example.opsc7311_poe_group20

import android.app.DatePickerDialog
import java.util.Calendar
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.format.DateFormat
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7311_poe_group20.Timesheetobj.timesheetlist
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class Timesheets : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var descr : String;
    private lateinit var selectedItem: String
    private var imageString: String = ""

    private lateinit var Mdate: Date
    private lateinit var strTime: Time
    private lateinit var endTime: Time
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheets)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val editText = findViewById<EditText>(R.id.desc)
        val Spinner = findViewById<Spinner>(R.id.projectspinner)
        val savebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.save)
        val datebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.date_fab)
        val startTimebtn: AppCompatButton = findViewById<AppCompatButton>(R.id.startTime_fab)
        val endTimebtb: AppCompatButton = findViewById<AppCompatButton>(R.id.endTime_fab)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
        val formattedTime = timeFormat.format(currentDate)

        val start = findViewById<TextView>(R.id.startview)
        start.text = formattedTime

        val end = findViewById<TextView>(R.id.endview)
        end.text = formattedTime

        val theDate = findViewById<TextView>(R.id.dateview)
        theDate.text = formattedDate

        Mdate = currentDate // Set Mdate to the current date
        strTime = Time(currentDate.time) // Set strTime to the current time
        endTime = Time(currentDate.time) // Set endTime to the current time

        // Filter the projectList based on the email
        val filteredProjects = ProjectManager.projectList.filter { it.email == userEmail }

        // Extract the project names from the filtered projects
        val projectNames = filteredProjects.map { it.projectName }

        // Set up ArrayAdapter to populate the spinner with the project names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, projectNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spinner.adapter = adapter

        var image = findViewById<ImageView>(R.id.projimage)
        image.visibility = View.GONE

        val addBtn: Button = findViewById(R.id.add_image)
        addBtn.setOnClickListener {
            editProfilePic()
            var image = findViewById<ImageView>(R.id.projimage)
            image.visibility = View.VISIBLE
        }

        datebtn.setOnClickListener {
            showDatePickerDialog(datebtn)
        }

        startTimebtn.setOnClickListener {
            showTimePickerDialog()
        }
        endTimebtb.setOnClickListener {
            showEndTimePickerDialog()
        }

        savebtn.setOnClickListener {

            val startCalendar = Calendar.getInstance()
            startCalendar.time = strTime

            val endCalendar = Calendar.getInstance()
            endCalendar.time = endTime

            val durationInMillis = endCalendar.timeInMillis - startCalendar.timeInMillis

            val totalMinutes: Int = (durationInMillis / (1000 * 60)).toInt()

            descr = editText.text.toString()
            selectedItem = Spinner.selectedItem.toString()
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("email", "")

            if(strTime < endTime){
                if (userEmail != null) {
                    var project = ProjectManager.projectList.find { it.projectName == selectedItem && it.email == userEmail}
                    project?.totalHours = project?.totalHours?.plus(totalMinutes)!!

                    val newEntry = TimesheetItems(
                        timesheetID = timesheetlist.size,
                        date = Mdate,
                        startTime = strTime,
                        endTime = endTime,
                        duration = totalMinutes,
                        decsrp = descr,
                        images = imageString ,
                        projectName = selectedItem, //fk project
                        email = userEmail//fk user
                    )
                    Timesheetobj.timesheetlist.add(newEntry)
                    Toast.makeText(this, "Time entry Added Successfully" , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,Home::class.java)
                    startActivity(intent)
                }
            }
            else{
                Toast.makeText(this, "Start time can not be later than end time" , Toast.LENGTH_SHORT).show()
            }
        }

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
    private fun editProfilePic() {
        val edit = Dialog(this)
        edit.setContentView(R.layout.edit_profile)

        val captureBtn = edit.findViewById<Button>(R.id.capturebtn)
        val galleryBtn = edit.findViewById<Button>(R.id.gallerybtn)

        captureBtn.setOnClickListener {
            // capture action is to capture an image from media store
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // request code allows the app to know which intent is being requested
            startActivityForResult(intent, 123)
        }

        galleryBtn.setOnClickListener {
            // gallery action is to pick an image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 456)
        }

        edit.show()
    }
    // open
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val profileImageView = findViewById<ImageView>(R.id.projimage)

        // For capture
        if (requestCode == 123 && resultCode == RESULT_OK) {
            val bmp: Bitmap = data?.extras?.get("data") as Bitmap
            profileImageView.setImageBitmap(bmp)

            // Convert the bitmap to a Base64-encoded string
            imageString = bitmapToBase64String(bmp)

            Toast.makeText(this, "Profile successfully changed!", Toast.LENGTH_SHORT).show()
        }
        // For gallery
        else if (requestCode == 456 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            profileImageView.setImageURI(selectedImageUri)

            // Convert the selected image URI to a Base64-encoded string
            imageString = uriToBase64String(selectedImageUri)
            Toast.makeText(this, "Profile successfully changed!", Toast.LENGTH_SHORT).show()
        }

        Log.d("Profile", "requestCode: $requestCode")
        Log.d("Profile", "resultCode: $resultCode")
        Log.d("Profile", "data: $data")
    }


    private fun bitmapToBase64String(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun uriToBase64String(uri: Uri?): String {
        val inputStream = contentResolver.openInputStream(uri!!)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream?.read(buffer).also { length = it!! } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun addDateTo(date: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        val formattedDateStr = outputFormat.format(parsedDate)

        Mdate = outputFormat.parse(formattedDateStr)

        val theDate = findViewById<TextView>(R.id.dateview)
        theDate.text = outputFormat.format(Mdate)
    }

    fun addTime(time: String) {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val parsedTime = inputFormat.parse(time)
        val formattedTimeStr = outputFormat.format(parsedTime)

        val formattedTime = outputFormat.parse(formattedTimeStr)
        strTime = Time(formattedTime.time)

        val start = findViewById<TextView>(R.id.startview)
        start.text = outputFormat.format(formattedTime)
    }
    fun addEndTime(time: String) {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val parsedTime = inputFormat.parse(time)
        val formattedTimeStr = outputFormat.format(parsedTime)

        val formattedTime = outputFormat.parse(formattedTimeStr)
        endTime = Time(formattedTime.time)

        val end = findViewById<TextView>(R.id.endview)
        end.text = outputFormat.format(formattedTime)
    }

    fun showTimePickerDialog() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")
    }
    fun showEndTimePickerDialog() {
        val timePickerFragment = EndTimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "timePicker")
    }
    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }
}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        (requireActivity() as Timesheets).addTime(selectedTime)
    }
}
class EndTimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        (requireActivity() as Timesheets).addEndTime(selectedTime)
    }
}
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
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
        (requireActivity() as Timesheets).addDateTo(selectedDate)
    }
}