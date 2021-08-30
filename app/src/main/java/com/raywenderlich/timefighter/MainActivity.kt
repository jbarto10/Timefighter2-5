package com.raywenderlich.timefighter

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import android.widget.Toast
import android.util.Log
import android.view.animation.AnimationUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

  private val TAG = MainActivity::class.java.simpleName
  //this is for logging and makes it easier to see where message is coming from

  private lateinit var gameScoreTextView: TextView
  private lateinit var timeLeftTextView: TextView
  private lateinit var tapMeButton: Button

  private var score = 0

  private var gameStarted = false

  private lateinit var countDownTimer: CountDownTimer
  private var initialCountDown: Long = 60000
  private var countDownInterval: Long = 1000
  private var timeLeft = 60

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    // connect views to variables
    Log.d(TAG, "onCreate called. Score is: $score")
    //used for logging
    // 1
    gameScoreTextView = findViewById(R.id.game_score_text_view)
    timeLeftTextView = findViewById(R.id.time_left_text_view)
    tapMeButton = findViewById(R.id.tap_me_button)
// 2
    tapMeButton.setOnClickListener { view ->
      val bounceAnimation = AnimationUtils.loadAnimation(this,
        R.anim.bounce)
      view.startAnimation(bounceAnimation)
      incrementScore()
    }

    if (savedInstanceState != null) {
      score = savedInstanceState.getInt(SCORE_KEY)
      timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
      restoreGame()
    } else {
      resetGame()
    }
  }
  // 2
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(SCORE_KEY, score)
    outState.putInt(TIME_LEFT_KEY, timeLeft)
    countDownTimer.cancel()
    Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeft")
  }
  // 3
  override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy called.")
  }
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
      // Inflate the menu; this adds items to the action bar if it
      super.onCreateOptionsMenu(menu)
      menuInflater.inflate(R.menu.menu, menu)
      return true
    }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about_item) {
          showInfo()
        }
        return true
      }



  private fun incrementScore() {
    // increment score logic
    if (!gameStarted) {
      startGame()
    }

    score++

    val newScore = getString(R.string.your_score, score)
    gameScoreTextView.text = newScore

  }
  private fun resetGame() {
    // reset game logic
    // 1
    score = 0
    // sets the score to "0"
    val initialScore = getString(R.string.your_score, score)
    // creates the initial score variable and gets the string from the string.xml
    gameScoreTextView.text = initialScore
    //stores and sets the initial score within the game score text view
    val initialTimeLeft = getString(R.string.time_left, 60)
    // creates the initial variable for initial time left and gets the string from the string.xml
    timeLeftTextView.text = initialTimeLeft
    //stores and sets the initial time left within the time left text view
    // 2
    countDownTimer = object : CountDownTimer(initialCountDown,
      countDownInterval) {
      //creates the count down time object and assigns the count down and the interval
      // 3
      override fun onTick(millisUntilFinished: Long) {
        timeLeft = millisUntilFinished.toInt() / 1000
        val timeLeftString = getString(R.string.time_left,
          timeLeft)
        timeLeftTextView.text = timeLeftString
      }
      // ths onTick is used to update the time left property by calling the onTick once a second to pass into the timer
      override fun onFinish() {
        //used for with the counting down is finished
        endGame()
      }

    }
    // 4
    gameStarted = false
  }
  private fun restoreGame() {
    val restoredScore = getString(R.string.your_score, score)
    gameScoreTextView.text = restoredScore
    val restoredTime = getString(R.string.time_left, timeLeft)
    timeLeftTextView.text = restoredTime
    countDownTimer = object : CountDownTimer((timeLeft *
            1000).toLong(), countDownInterval) {
      override fun onTick(millisUntilFinished: Long) {
        timeLeft = millisUntilFinished.toInt() / 1000
        val timeLeftString = getString(R.string.time_left,
          timeLeft)
        timeLeftTextView.text = timeLeftString
      }
      override fun onFinish() {
        endGame()
      }
    }
    countDownTimer.start()
    gameStarted = true
  }

  private fun startGame() {
    // start game logic
    countDownTimer.start()
    gameStarted = true
    // starts the count down timer
  }
  private fun endGame() {
    // end game logic
    Toast.makeText(this, getString(R.string.game_over_message,
      score), Toast.LENGTH_LONG).show()
    //toast message with score
    resetGame()
    //restart the game
  }
  // 1
  companion object {
    private const val SCORE_KEY = "SCORE_KEY"
    private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    //use these variables for when the device changes orientation
  }
    private fun showInfo() {
      val dialogTitle = getString(R.string.about_title,
        BuildConfig.VERSION_NAME)
      val dialogMessage = getString(R.string.about_message)
      val builder = AlertDialog.Builder(this)
      builder.setTitle(dialogTitle)
      builder.setMessage(dialogMessage)
      builder.create().show()
    }

}