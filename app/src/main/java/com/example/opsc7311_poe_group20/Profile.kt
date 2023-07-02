package com.example.opsc7311_poe_group20

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.*


class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("email", "")
        val name = sharedPreferences.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")
        val company = sharedPreferences.getString("company", "")
        val mobile = sharedPreferences.getString("mobile", "")
        val min = sharedPreferences.getInt("userMin", 0)
        val max = sharedPreferences.getInt("userMax", 0)
        val profilePicture = sharedPreferences.getString("profilePicture", null)

        val dateFormat = sharedPreferences.getString("dateFormat", "")
        val clock = sharedPreferences.getBoolean("Is24HoursClock", true)
        val dayoftheweek = sharedPreferences.getString("firstDayOfTheWeek", "")
        val notification = sharedPreferences.getBoolean("IsNotification", true)

        // Set the user details in the profile XML layout
        val nameTextView = findViewById<TextView>(R.id.nametxt)
        val surnameTextView = findViewById<TextView>(R.id.surnametxt)
        val companyTextView = findViewById<TextView>(R.id.companytxt)
        val emailTextView = findViewById<TextView>(R.id.emailtxt)
        val mobileTextView = findViewById<TextView>(R.id.mobiletxt)
        val profileImageView = findViewById<ImageView>(R.id.profileimg)
        val minTextView = findViewById<TextView>(R.id.mintxt)
        val maxTextView = findViewById<TextView>(R.id.maxtxt)

        nameTextView.text = name
        surnameTextView.text = surname
        companyTextView.text = company
        emailTextView.text = userEmail
        mobileTextView.text = mobile
        minTextView.text = min.toString()
        maxTextView.text = max.toString()

        //setting user settings
        val dateFormatSpin = findViewById<Spinner>(R.id.DateSpinner)
        val Is24HoursClockcb = findViewById<SwitchCompat>(R.id.hourswitch)
        val firstDayOfTheWeekSpin = findViewById<Spinner>(R.id.weekdaysSpinner)
        val IsNotificationcb = findViewById<SwitchCompat>(R.id.notificationswitch)

        dateFormatSpin.setSelection(getSelectedIndex(dateFormatSpin, dateFormat))
        Is24HoursClockcb.isChecked = clock
        firstDayOfTheWeekSpin.setSelection(getSelectedIndex(firstDayOfTheWeekSpin, dayoftheweek))
        IsNotificationcb.isChecked = notification

// Load and set the profile picture
        if (!profilePicture.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(profilePicture, Base64.DEFAULT)
            val decodedBitmap: Bitmap? = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            if (decodedBitmap != null) {
                profileImageView.setImageBitmap(decodedBitmap)
            } else {
                // Failed to decode bitmap
                profileImageView.setImageResource(R.drawable.avatar)
            }
        } else {
            profileImageView.setImageResource(R.drawable.avatar)
        }

        // Set the hint based on the user details
        nameTextView.hint = createItalicHint(if (name.isNullOrEmpty()) "Add Name" else name)
        surnameTextView.hint = createItalicHint(if (surname.isNullOrEmpty()) "Add Surname" else surname)
        companyTextView.hint = createItalicHint(if (company.isNullOrEmpty()) "Add Company" else company)
        mobileTextView.hint = createItalicHint(if (mobile.isNullOrEmpty()) "Add Mobile Number" else mobile)

        // Set the hint based on the user details
        nameTextView.hint = createItalicHint((if (name != "") name else "Add Name").toString())
        surnameTextView.hint = createItalicHint((if (surname != "") name else "Add Surname").toString())
        companyTextView.hint = createItalicHint((if (company != "") company else "Add Company").toString())
        mobileTextView.hint = createItalicHint((if (mobile != "") mobile else "Add Mobile Number").toString())


        //ADAPTER
        val rvBadges: RecyclerView = findViewById(R.id.rvBadges)
        val items = BadgesObj.Badgeslist
        val adapter = BadgeAdapter(items)
        rvBadges.adapter = adapter
        // Inside your activity or fragment
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvBadges.layoutManager = layoutManager

        var discover = findViewById<TextView>(R.id.discovertxt)
        var left = 5 - BadgesObj.Badgeslist.size
        discover.text = "You have " + left + " more badges left to discover"


        val addBtn: Button = findViewById(R.id.editProfilebtn)
        addBtn.setOnClickListener {
            editProfilePic()
        }

        val savebtn: Button = findViewById(R.id.savebtn)
        savebtn.setOnClickListener {
            // Find the user with the matching email
            val userToEdit = UserManager.userList.find { it.email == userEmail }
            val userSettings = UserSettingsManager.userSettingsList.find { it.email == userEmail}

            // Check if the user exists
            if (userToEdit != null) {
                // Modify the desired fields
                userToEdit.name = (findViewById<TextView>(R.id.nametxt)).text.toString()
                userToEdit.surname = (findViewById<TextView>(R.id.surnametxt)).text.toString()
                userToEdit.company = (findViewById<TextView>(R.id.companytxt)).text.toString()
                userToEdit.mobile = (findViewById<TextView>(R.id.mobiletxt)).text.toString()

                val defaultAvatarResourceId = R.drawable.avatar
                val defaultAvatarUriString = Uri.parse("android.resource://$packageName/$defaultAvatarResourceId").toString()
                userToEdit?.profilePicture = defaultAvatarUriString


                //user settings
                if (userSettings != null) {
                    userSettings.dateFormat = (findViewById<Spinner>(R.id.DateSpinner)).selectedItem.toString()
                    userSettings.Is24HoursClock = (findViewById<SwitchCompat>(R.id.hourswitch)).isChecked
                    userSettings.firstDayOfTheWeek = (findViewById<Spinner>(R.id.weekdaysSpinner)).selectedItem.toString()
                    userSettings.IsNotification = (findViewById<SwitchCompat>(R.id.notificationswitch)).isChecked
                }

                // Save the changes to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("name", userToEdit.name)
                editor.putString("surname", userToEdit.surname)
                editor.putString("company", userToEdit.company)
                editor.putString("mobile", userToEdit.mobile)

                if (userSettings != null) {
                    editor.putString("dateFormat", userSettings.dateFormat)
                    editor.putBoolean("Is24HoursClock", userSettings.Is24HoursClock)
                    editor.putString("firstDayOfTheWeek", userSettings.firstDayOfTheWeek)
                    editor.putBoolean("IsNotification", userSettings.IsNotification)
                }

                editor.apply()

                Toast.makeText(this, "Changes Successfully Saved!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
        }

        val logoutbtn: Button = findViewById(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Get SharedPreferences instance
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val profileImageView = findViewById<ImageView>(R.id.profileimg)
        val userEmail = sharedPreferences.getString("email", "")

        // For capture
        if (requestCode == 123 && resultCode == RESULT_OK) {
            val bmp: Bitmap = data?.extras?.get("data") as Bitmap
            profileImageView.setImageBitmap(bmp)

            // Convert the bitmap to a Base64-encoded string
            val imageString = bitmapToBase64String(bmp)

            // Update the profile picture in the user object
            val userToEdit = UserManager.userList.find { it.email == userEmail }
            userToEdit?.profilePicture = imageString

            // Save the updated profile picture string to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("profilePicture", imageString)
            editor.apply()

            Toast.makeText(this, "Profile successfully changed!", Toast.LENGTH_SHORT).show()
        }
        // For gallery
        else if (requestCode == 456 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            profileImageView.setImageURI(selectedImageUri)

            // Convert the selected image URI to a Base64-encoded string
            val imageString = uriToBase64String(selectedImageUri)

            // Update the profile picture in the user object
            val userToEdit = UserManager.userList.find { it.email == userEmail }
            userToEdit?.profilePicture = imageString

            // Save the updated profile picture string to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("profilePicture", imageString)
            editor.apply()

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


    // Helper function to get the index of the selected item in the spinner
    private fun getSelectedIndex(spinner: Spinner, value: String?): Int {
        val adapter = spinner.adapter
        if (value != null && adapter != null) {
            for (i in 0 until adapter.count) {
                if (value == adapter.getItem(i).toString()) {
                    return i
                }
            }
        }
        return -1 // Return -1 if the value is not found
    }

    class CustomSpinnerAdapter(context: Context, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            (view as TextView).setTextColor(Color.WHITE)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            (view as TextView).setTextColor(Color.WHITE)
            return view
        }
    }


    private fun createItalicHint(hintText: String): SpannableString {
        val spannableString = SpannableString(hintText)
        val italicSpan = StyleSpan(Typeface.ITALIC)
        spannableString.setSpan(italicSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}