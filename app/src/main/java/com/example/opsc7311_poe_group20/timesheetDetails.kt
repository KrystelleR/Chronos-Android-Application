package com.example.opsc7311_poe_group20

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class timesheetDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet_details)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val date = Timesheetobj.timesheetlist[Home.clickedItemPosition].date
        val startTime = Timesheetobj.timesheetlist[Home.clickedItemPosition].startTime
        val endTime = Timesheetobj.timesheetlist[Home.clickedItemPosition].EndTime

        val formattedDate = dateFormat.format(date)
        val formattedStartTime = timeFormat.format(startTime)
        val formattedEndTime = timeFormat.format(endTime)

        val start = findViewById<TextView>(R.id.startview)
        start.text = formattedStartTime

        val end = findViewById<TextView>(R.id.endview)
        end.text = formattedEndTime

        val theDate = findViewById<TextView>(R.id.dateview)
        theDate.text = formattedDate

        val project = findViewById<TextView>(R.id.selecedProjecttxt)
        project.text = Timesheetobj.timesheetlist[Home.clickedItemPosition].projectName

        val description = findViewById<TextView>(R.id.descriptiontxt)

         if(Timesheetobj.timesheetlist[Home.clickedItemPosition].decsrp == ""){
             description.text = "No Description"
         }
        else{
             description.text =Timesheetobj.timesheetlist[Home.clickedItemPosition].decsrp
         }


        var theImage = findViewById<ImageView>(R.id.projimage)
        var image = Timesheetobj.timesheetlist[Home.clickedItemPosition].images

        if(image != ""){
            if (!image.isNullOrEmpty()) {
                val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
                val decodedBitmap: Bitmap? = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                if (decodedBitmap != null) {
                    theImage.setImageBitmap(decodedBitmap)
                } else {
                    // Failed to decode bitmap
                    theImage.setImageResource(R.drawable.avatar)
                }
            } else {
                theImage.setImageResource(R.drawable.avatar)
            }
        }
        else{
            theImage.visibility = View.GONE
        }




    }
}