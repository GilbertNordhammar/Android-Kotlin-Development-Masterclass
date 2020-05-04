package com.example.a7minuteworkout

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity() {

    companion object {
        private const val MAX_EXERCISE_TIME_IN_MILLISECONDS: Long = 30000
        private const val MAX_REST_TIME_IN_MILLISECONDS: Long = 10000
        private const val COUNTDOWN_INTERVAL_IN_MILLISECONDS: Long = 1000
    }

    private var _countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _countdownTimer?.cancel()
    }

    private fun startCountdown(maxTime: Long, onFinish: () -> Unit, onTick: (secondsUntilFinished: Int) -> Unit) {
        _countdownTimer?.cancel()
        _countdownTimer = object: CountDownTimer(maxTime, COUNTDOWN_INTERVAL_IN_MILLISECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = (millisUntilFinished / 1000).toInt()
                onTick(secondsUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    private fun setupRestView() {
        progressbar_rest_view.max = (MAX_REST_TIME_IN_MILLISECONDS / 1000).toInt()
        progressbar_rest_view.progress = 0

        val onFinish = {
            Toast.makeText(
                this@ExerciseActivity,
                "We will now start the exercise",
                Toast.LENGTH_SHORT
            ).show()
            linearlayout_rest_view.visibility = View.GONE
            setupExerciseView()
        }

        val onTick = { secondsUntilFinished: Int ->
            progressbar_rest_view.progress = secondsUntilFinished
            textView_timer_rest_view.text = secondsUntilFinished.toString()
        }

        startCountdown(MAX_REST_TIME_IN_MILLISECONDS, onFinish, onTick)
    }

    private fun setupExerciseView() {
        progressbar_exercise_view.max = (MAX_EXERCISE_TIME_IN_MILLISECONDS / 1000).toInt()
        progressbar_exercise_view.progress = 0
        linearlayout_excercise_view.visibility = View.VISIBLE

        val onFinish = {
            Toast.makeText(
                this@ExerciseActivity,
                "Here we will start the next rest screen",
                Toast.LENGTH_SHORT
            ).show()
        }

        val onTick = { secondsUntilFinished: Int ->
            progressbar_exercise_view.progress = secondsUntilFinished
            textView_timer_exercise_view.text = secondsUntilFinished.toString()
        }

        startCountdown(MAX_EXERCISE_TIME_IN_MILLISECONDS, onFinish, onTick)
    }
}
