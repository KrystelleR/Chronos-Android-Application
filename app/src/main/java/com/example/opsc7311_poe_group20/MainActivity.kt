package com.example.opsc7311_poe_group20


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2

private lateinit var iv1: CardView
private lateinit var iv2: CardView
private lateinit var iv3: CardView
private lateinit var iv4: CardView
private lateinit var iv5: CardView
private lateinit var iv6: CardView

private lateinit var imagetxt: TextView

private lateinit var viewPager2:ViewPager2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signUp = findViewById<Button>(R.id.signUpbtn)

        signUp.setOnClickListener(){
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        val signIn = findViewById<Button>(R.id.signInbtn)

        signIn.setOnClickListener(){
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        viewPager2= findViewById(R.id.viewPager2)
        iv1 = findViewById(R.id.im1)
        iv2 = findViewById(R.id.im2)
        iv3 = findViewById(R.id.im3)
        iv4 = findViewById(R.id.im4)
        iv5 = findViewById(R.id.im5)
        iv6 = findViewById(R.id.im6)

        imagetxt = findViewById(R.id.imagetxt)

        val images = listOf(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6)
        val adapter = ViewPagerAdapter(images)
        viewPager2.adapter = adapter

        //chnaging color of dot indicater when image is on current image
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changeColor()
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }

        })
    }

    fun changeColor(){
        when (viewPager2.currentItem)
        {
            0->
            {
                imagetxt.setText("Manage your time more efficiently")
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            1->
            {
                imagetxt.setText("View reports of where you spend most of your time")
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            2->
            {
                imagetxt.setText("Set reminders for projects and tasks")
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            3->
            {
                imagetxt.setText("Use our stopwatch feature to more accurately record your hours")
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            4->
            {
                imagetxt.setText("View past time-sheets and plan for future sessions with our calendar")
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            5->
            {
                imagetxt.setText("Collaborate on team tasks more easily")
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }
        }
    }
}