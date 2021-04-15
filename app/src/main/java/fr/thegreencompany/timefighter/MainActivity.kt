package fr.thegreencompany.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal var score = 0
    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal var timeLeftOnTimer: Long = 60000
    internal val contDownInterval: Long = 1000

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLefTextView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLefTextView = findViewById(R.id.timeLefTextView)

        tapMeButton = findViewById(R.id.tapMebutton)
        tapMeButton.setOnClickListener { view ->
            incrementScore()
        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        Log.d(TAG, "onCreate score is: $score")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)

        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState, Saving score: $score ;Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy")
    }

    private fun resetGame() {

        score = 0
        gameScoreTextView.text =  getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        timeLefTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object  : CountDownTimer(initialCountDown, contDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = timeLeftOnTimer / 1000
                timeLefTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, score)

        val restoreTime = timeLeftOnTimer / 1000
        timeLefTextView.text = getString(R.string.timeLeft, restoreTime)

        countDownTimer = object  : CountDownTimer(timeLeftOnTimer, contDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = timeLeftOnTimer / 1000
                timeLefTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }


    private fun incrementScore() {

        if(!gameStarted) {
            startGame()
        }

        score += 1
        gameScoreTextView.text = getString(R.string.yourScore, score)
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}