package com.example.opsc7311_poe_group20

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.Base64

class SignIn : AppCompatActivity() {
    private var isPasswordVisible = false

    val database = Firebase.database
    val getUserDetails = database.getReference("Users")
    val getUserSettings = database.getReference("Users_Settings")

    var userList = mutableListOf<Users>()
    val userSettingsList = mutableListOf<UserSettings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //read from database users
        getUserDetails.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle the data change event
                //iterate over children in the list
                for(pulledUser in snapshot.children){
                    val user: Users? = pulledUser.getValue(Users::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation event for user retrieval
                Toast.makeText(this@SignIn, "Error retrieving user", Toast.LENGTH_SHORT).show()
            }
        })

        //read from database userSettings
        getUserSettings.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Handle the data change event
                //iterate over children in the list
                for(pulledUserSettings in snapshot.children){
                    val userSettings: UserSettings? = pulledUserSettings.getValue(UserSettings::class.java)
                    if (userSettings != null) {
                        userSettingsList.add(userSettings)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation event for user retrieval
                Toast.makeText(this@SignIn, "Error retrieving userSettings", Toast.LENGTH_SHORT).show()
            }
        })


        val passwordEditText: EditText = findViewById(R.id.passwordtxt)
        val passwordToggleImage: ImageView = findViewById(R.id.imageViewTogglePassword)

        passwordToggleImage.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Show the password
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggleImage.setImageResource(R.drawable.ic_visibility_on)
            } else {
                // Hide the password
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggleImage.setImageResource(R.drawable.ic_visibility_off)
            }

            // Move the cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.text.length)
        }


        val signUpLink = findViewById<Button>(R.id.Signupbtn)

        signUpLink.setOnClickListener() {
            val intent1 = Intent(this, SignUp::class.java)
            startActivity(intent1)
        }

        val signInButton = findViewById<Button>(R.id.SignInbtn)

        signInButton.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.emailtxt)
            val passwordEditText = findViewById<EditText>(R.id.passwordtxt)

            val email = emailEditText.text.toString().toLowerCase()
            val password = passwordEditText.text.toString()

            val user = userList.find { it.email == email }
            val userSettings = userSettingsList.find { it.email == email }

            if (user != null) {
                val saltString = user.passwordSalt
                val saltByteArray = Base64.getDecoder().decode(saltString)

                val myHash = hashPassword(password, saltByteArray)

                if (myHash == user.passwordHash) {
                    // Storing user details in SharedPreferences
                    val sharedPreferences = getSharedPreferences   ("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("email", user.email)
                    editor.putString("passwordHash", user.passwordHash)
                    editor.putString("passwordSalt", user.passwordSalt)
                    editor.putString("name", user.name)
                    editor.putInt("userMin", user.min)
                    editor.putInt("userMax", user.max)
                    editor.putString("surname", user.surname)
                    editor.putString("company", user.company)
                    editor.putString("mobile", user.mobile)
                    editor.putString("profilePicture", user.profilePicture)
                    editor.apply()

                    if (userSettings != null) {
                        editor.putInt("ID", userSettings.ID)
                        editor.putString("dateFormat", userSettings.dateFormat)
                        editor.putBoolean("Is24HoursClock", userSettings.Is24HoursClock)
                        editor.putString("firstDayOfTheWeek", userSettings.firstDayOfTheWeek)
                        editor.putBoolean("IsNotification", userSettings.IsNotification)
                        editor.apply()
                    }

                    // Passwords match
                    Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                    // Redirect to the home page
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                } else {
                    // Passwords don't match
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                // User not found
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}