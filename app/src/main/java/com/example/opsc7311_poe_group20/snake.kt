 package com.example.opsc7311_poe_group20

 import android.app.Activity
 import android.graphics.Color
 import android.graphics.drawable.ColorDrawable
 import android.os.Bundle
 import android.os.Handler
 import android.view.*
 import android.widget.Button
 import android.widget.ImageView
 import android.widget.LinearLayout
 import android.widget.RelativeLayout
 import androidx.appcompat.app.AppCompatActivity
 import java.util.*
 import kotlin.math.pow
 import kotlin.math.sqrt

 class snake : AppCompatActivity() {

     private val Int.dp: Int
         get() = (this / resources.displayMetrics.density).toInt()

     override fun onCreate(savedInstanceState: Bundle?) {
         requestWindowFeature(Window.FEATURE_NO_TITLE)
         window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_snake)
         supportActionBar?.hide()
         val board = findViewById<RelativeLayout>(R.id.board)
         val border = findViewById<RelativeLayout>(R.id.relativeLayout)
         val lilu = findViewById<LinearLayout>(R.id.lilu)
         val pauseButton = findViewById<Button>(R.id.pause)
         val newgame = findViewById<Button>(R.id.new_game)
         val resume = findViewById<Button>(R.id.resume)
         val playagain = findViewById<Button>(R.id.playagain)
         val score2 = findViewById<Button>(R.id.score2)
         val meat = ImageView(this)
         val snake = ImageView(this)
         val snakeSegments =
             mutableListOf(snake) // Keep track of the position of each snake segment
         val handler = Handler()
         var delayMillis = 30L // Update snake position every 100 milliseconds
         var currentDirection = "right" // Start moving right by default
         var scorex = 0

         val foodResources = intArrayOf(
             R.drawable.food1,
             R.drawable.food2,
             R.drawable.food3
         )

         var currentFoodIndex = 0


         val body1ResId = R.drawable.body1
         val body2ResId = R.drawable.body2
         var currentBodyResId = body1ResId

         // Variables to track touch events
         var startX = 0f
         var startY = 0f

        val stopbtn = findViewById<Button>(R.id.stopBtn)
         stopbtn.setOnClickListener {
             finish()
         }
// Variables to track swipe direction
         var deltaX = 0f
         var deltaY = 0f


         board.setOnTouchListener { view, event ->
             when (event.action) {
                 MotionEvent.ACTION_DOWN -> {
                     startX = event.x
                     startY = event.y
                 }
                 MotionEvent.ACTION_MOVE -> {
                     deltaX = event.x - startX
                     deltaY = event.y - startY

                     // Determine the dominant direction of the swipe
                     if (Math.abs(deltaX) > Math.abs(deltaY)) {
                         // Horizontal swipe
                         if (deltaX > 0) {
                             currentDirection = "right"
                         } else {
                             currentDirection = "left"
                         }
                     } else {
                         // Vertical swipe
                         if (deltaY > 0) {
                             currentDirection = "down"
                         } else {
                             currentDirection = "up"
                         }
                     }
                 }
                 MotionEvent.ACTION_UP -> {
                     deltaX = 0f
                     deltaY = 0f
                 }
             }
             true
         }




         board.visibility = View.INVISIBLE
         playagain.visibility = View.INVISIBLE
         score2.visibility = View.INVISIBLE

         newgame.setOnClickListener {


             board.visibility = View.VISIBLE
             newgame.visibility = View.INVISIBLE
             resume.visibility = View.INVISIBLE
             score2.visibility = View.VISIBLE


             snake.setImageResource(R.drawable.head) //this is the head
             snake.layoutParams = LinearLayout.LayoutParams(200.dp, 200.dp)
             board.addView(snake)
             snake.bringToFront() // Bring the snake's head to the front
             snakeSegments.add(snake) // Add the new snake segment to the list


             var snakeX = snake.x
             var snakeY = snake.y

             val foodResource = foodResources[currentFoodIndex]
             meat.setImageResource(foodResource)
             meat.layoutParams = LinearLayout.LayoutParams(250.dp, 250.dp)
             board.addView(meat)

             val random = Random() // create a Random object
             val randomX =
                 random.nextInt(801) - 400 // generate a random x-coordinate between -400 and 400
             val randomY =
                 random.nextInt(801) - 400 // generate a random y-coordinate between -400 and 400


             meat.x = randomX.toFloat()
             meat.y = randomY.toFloat()



             fun checkFoodCollision() {

                 val distanceThreshold = 50

                 val distance = sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))

                 if (distance < distanceThreshold) { // Check if the distance between the snake head and the meat is less than the threshold

                     val newSnake = ImageView(this)
                     newSnake.visibility = View.INVISIBLE
                     newSnake.setImageResource(currentBodyResId)
                     newSnake.layoutParams = LinearLayout.LayoutParams(200.dp, 200.dp)
                     board.addView(newSnake)


                     snake.bringToFront()

                     snakeSegments.add(newSnake) // Add the new snake segment to the list
                     currentBodyResId = if (currentBodyResId == body1ResId) body2ResId else body1ResId


                     val random = Random() // create a Random object
                     val randomX = random.nextInt(board.width - meat.width).coerceAtLeast(0) // generate random X coordinate within the visible area
                     val randomY = random.nextInt(board.height - meat.height).coerceAtLeast(0) // generate random Y coordinate within the visible area

                     meat.x = randomX.toFloat()
                     meat.y = randomY.toFloat()


                     delayMillis-- // Reduce delay value by 1
                     scorex++
                     currentFoodIndex = (currentFoodIndex + 1) % foodResources.size
                     if(currentFoodIndex == 3){
                         currentFoodIndex ==0
                     }
                     val foodResource = foodResources[currentFoodIndex]
                     meat.setImageResource(foodResource)

                     score2.text =   "score : " + scorex.toString() // Update delay text view
                     newSnake.visibility = View.VISIBLE
                 }
             }

             val runnable = object : Runnable {
                 override fun run() {
                     for (i in snakeSegments.size - 1 downTo 1) {
                         snakeSegments[i].x = snakeSegments[i - 1].x
                         snakeSegments[i].y = snakeSegments[i - 1].y
                     }

                     when (currentDirection) {
                         "up" -> {
                             snakeY -= 10
                             val maxY = ((board.height/2) *-1) + snake.height
                             if (snakeY < maxY) {
                                 snakeY = maxY.toFloat()
                                 border.setBackgroundColor(getResources().getColor(R.color.red))
                                 playagain.visibility = View.VISIBLE
                                 currentDirection = "pause"
                                 lilu.visibility = View.INVISIBLE
                                 score2.visibility = View.INVISIBLE
                             }

                             snake.translationY = snakeY - 50.dp
                             snake.rotation = 180f // Rotate the head 180 degrees
                         }
                         "down" -> {
                             snakeY += 10
                             val maxY = board.height / 2
                             if (snakeY > maxY) {
                                 snakeY = maxY.toFloat()
                                 border.setBackgroundColor(getResources().getColor(R.color.red))
                                 playagain.visibility = View.VISIBLE
                                 currentDirection = "pause"
                                 lilu.visibility = View.INVISIBLE
                                 score2.visibility = View.INVISIBLE
                             }
                             snake.translationY = snakeY - 50.dp
                             snake.rotation = 0f // Reset head rotation to 0 degrees
                         }
                         "left" -> {
                             snakeX -= 10
                             val maxX = ((board.width / 2) - 50) * -1
                             if (snakeX < maxX) {
                                 snakeX = maxX.toFloat()
                                 border.setBackgroundColor(getResources().getColor(R.color.red))
                                 playagain.visibility = View.VISIBLE
                                 currentDirection = "pause"
                                 lilu.visibility = View.INVISIBLE
                                 score2.visibility = View.INVISIBLE
                             }
                             snake.translationX = snakeX - 50.dp
                             snake.rotation = 90f // Rotate the head 90 degrees to the left
                         }
                         "right" -> {
                             snakeX += 10
                             val maxX = (board.width / 2) - 25
                             if (snakeX > maxX) {
                                 snakeX = maxX.toFloat()
                                 border.setBackgroundColor(getResources().getColor(R.color.red))
                                 playagain.visibility = View.VISIBLE
                                 currentDirection = "pause"
                                 lilu.visibility = View.INVISIBLE
                                 score2.visibility = View.INVISIBLE
                             }
                             snake.translationX = snakeX - 50.dp
                             snake.rotation = -90f // Rotate the head 90 degrees to the right
                         }
                         "pause" -> {
                             snakeX += 0
                             snake.translationX = snakeX
                         }
                     }

                     checkFoodCollision()
                     handler.postDelayed(this, delayMillis)
                 }
             }


             handler.postDelayed(runnable, delayMillis)

// Set button onClickListeners to update the currentDirection variable when pressed
             pauseButton.setOnClickListener {
                 currentDirection = "pause"
                 board.visibility = View.INVISIBLE
                 newgame.visibility = View.VISIBLE
                 resume.visibility = View.VISIBLE

             }
             resume.setOnClickListener {
                 currentDirection = "right"
                 board.visibility = View.VISIBLE
                 newgame.visibility = View.INVISIBLE
                 resume.visibility = View.INVISIBLE

             }
             playagain.setOnClickListener {

                 recreate()
             }

         }


     }

 }