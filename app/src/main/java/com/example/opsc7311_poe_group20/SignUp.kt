package com.example.opsc7311_poe_group20

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import java.util.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.opsc7311_poe_group20.OwnBadgesObj.ownBadgeslist
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val database = Firebase.database
        val addUsers = database.getReference("Users")
        val addUserSettings = database.getReference("Users_Settings")
        val addUserBadges = database.getReference("Users_Badges")

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

        val signInLink = findViewById<Button>(R.id.SignInbtn)
        val passwordEditText = findViewById<EditText>(R.id.passwordtxt)
        val confirmPassText = findViewById<EditText>(R.id.confirmpasswordtxt)
        val emailEditText = findViewById<EditText>(R.id.emailtxt)
        val termsCheckBox = findViewById<CheckBox>(R.id.checkBox)
        val signUpButton = findViewById<Button>(R.id.SignIn)
        val name = findViewById<EditText>(R.id.nametxt)
        val min = findViewById<TextView>(R.id.mintxt)
        val max = findViewById<TextView>(R.id.maxtxt)

        val linkTextView = findViewById<TextView>(R.id.tcstext)
        linkTextView.setOnClickListener {
            // Create and show the dialog box
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Terms and Conditions")
                .setMessage("Terms and Conditions for Chronos Time Tracker:\n\nBy using the Chronos Time Tracker application, you agree to the following terms and conditions:\n\n1. Account Registration:\n - You must create an account to access and use the App.\n - You are responsible for maintaining the confidentiality of your account login credentials.\n - You are solely responsible for all activities that occur under your account.\n\n2. User Responsibilities:\n - You must provide accurate and complete information when using the App.\n - You are responsible for the content and accuracy of the data you enter into the App.\n - You must not misuse, interfere with, or disrupt the App or its associated systems.\n\n3. Data Privacy and Security:\n - The App stores your data in an online database for retrieval and analysis.\n - While we take reasonable measures to protect your data, we cannot guarantee absolute security.\n - We may collect and use your data in accordance with our Privacy Policy.\n\n4. Availability and Updates:\n - We strive to ensure that the App is available and functional, but we do not guarantee uninterrupted access.\n - We may release updates to enhance the App's features and address any issues or bugs.\n\n5. Third-Party Content and Links:\n - The App may contain links to third-party websites or resources.\n - We are not responsible for the content, accuracy, or availability of these external resources.\n\n6. Limitation of Liability:\n - To the maximum extent permitted by law, we shall not be liable for any damages arising from the use of the App.\n\nBy using the Chronos Time Tracker App, you acknowledge that you have read, understood, and agreed to these terms and conditions. If you do not agree to these terms, you should refrain from using the App.")
                .setPositiveButton("OK") { dialog, _ ->
                    // Handle positive button click
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Handle negative button click
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }


        signUpButton.setOnClickListener {
            val myEmail = emailEditText.text.toString().toLowerCase()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPassText.text.toString()
            val myName = name.text.toString()
            val myMin = min.text.toString()
            val myMax = max.text.toString()
            val termsAccepted = termsCheckBox.isChecked
            val passwordRegex = "(?=.*[A-Z])(?=.*[0-9]).{6,}".toRegex()

            if (!Patterns.EMAIL_ADDRESS.matcher(myEmail).matches()) {
                showToast("Please enter a valid email address")
            } else if  (isEmailTaken(myEmail)) {
                showToast("Email is already taken. Sign-In instead")
            } else if (myEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || myName.isEmpty()|| myMin.isEmpty() || myMax.isEmpty()) {
                showToast("Please fill in all fields.")
            } else if (!termsAccepted) {
                showToast("Please accept the terms and conditions")
            } else if (!passwordRegex.matches(password)) {
                showToast("Password must be at least 6 characters long, contain a capital letter, and a numerical value")
            } else if (password != confirmPassword) {
                showToast("Passwords do not match")
            } else {

                val theMin = myMin.toInt()
                val theMax = myMax.toInt()

                if (theMin < theMax){
                    val salt = generateSalt()
                    val hashedPassword = hashPassword(password, salt)

                    val defaultAvatarResourceId = R.drawable.avatar
                    val defaultAvatarUriString = Uri.parse("android.resource://$packageName/$defaultAvatarResourceId").toString()

                    val newUser = Users(
                        email = myEmail,
                        passwordHash = hashedPassword,
                        passwordSalt = Base64.getEncoder().encodeToString(salt),
                        name =myName,
                        surname = "",
                        company="",
                        mobile="",
                        min = theMin,
                        max = theMax,
                        profilePicture =  defaultAvatarUriString
                    )

                    addUsers.push().setValue(newUser)

                    val newUserSettings = UserSettings(
                        ID = 0,
                        dateFormat = "DD/MM/YYYY",
                        Is24HoursClock = true,
                        firstDayOfTheWeek = "Monday",
                        IsNotification = true,
                        email = myEmail
                    )
                    addUserSettings.push().setValue(newUserSettings)


                    val newBadges =OwnBadges(
                        badge1 = false,
                        badge2 = false,
                        badge3 = false,
                        badge4 = false,
                        badge5 = false,
                        email = myEmail
                    )
                    addUserBadges.push().setValue(newBadges)

                    val user = UserManager.userList.find { it.email == myEmail }
                    if (user != null) {
                        showToast("Successfully Signed-Up!")
                    }

                    val intent = Intent(this, SignIn::class.java)
                    startActivity(intent)
                }
                else{
                    showToast("Minimum goal cannot be more than maximum goal")
                }
            }
        }

        signInLink.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.error =("Please enter a valid email address")
                }  else {
                    emailEditText.error = null // Clear the error
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                val passwordRegex = "(?=.*[A-Z])(?=.*[0-9]).{6,}".toRegex()

                if (!passwordRegex.matches(password)) {
                    passwordEditText.error = "Password must be at least 6 characters long, contain a capital letter, and a numerical value"
                } else {
                    passwordEditText.error = null // Clear the error
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        confirmPassText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val confirmPassword = s.toString()
                val password = passwordEditText.text.toString() // Retrieve the password from passwordEditText

                if (confirmPassword != password) { // Compare the passwords
                    confirmPassText.error = "Passwords do not match" // Set error on confirmPassText
                } else {
                    confirmPassText.error = null // Clear the error
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isEmailTaken(email: String): Boolean {
        val user = UserManager.userList.find { it.email == email }
        return user != null
    }
}