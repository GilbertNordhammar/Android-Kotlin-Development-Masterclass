package com.example.a7minuteworkout

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    companion object {
        private const val EXERCISE_TIME_IN_MILLISECONDS: Long = 5000
        private const val REST_TIME_IN_MILLISECONDS: Long = 5000
        private const val COUNTDOWN_INTERVAL_IN_MILLISECONDS: Long = 1000
    }

    private var _countdownTimer: CountDownTimer? = null
    private var _excerciseList: ArrayList<ExerciseModel>? = null
    private var _currentExcercisePosition = -1

    private lateinit var _textToSpeech: TextToSpeech
    private val _mediaPlayer by lazy {MediaPlayer.create(applicationContext, R.raw.press_start)}
    private var _exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        _textToSpeech = TextToSpeech(this, this)
        _excerciseList = Exercises.defaultExcerciseList()
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _countdownTimer?.cancel()

        _textToSpeech.stop()
        _textToSpeech.shutdown()

        _mediaPlayer.stop()
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
        _mediaPlayer.isLooping = false
        _mediaPlayer.start()

        linearlayout_rest_view.visibility = View.VISIBLE
        progressbar_rest_view.max = (REST_TIME_IN_MILLISECONDS / 1000).toInt()
        progressbar_rest_view.progress = 0

        val upcomingExerciseName = _excerciseList!![_currentExcercisePosition + 1].getName()
        textView_upcoming_exercise_name.text = upcomingExerciseName

        val onFinish = {
            linearlayout_rest_view.visibility = View.GONE
            _currentExcercisePosition++
            setupExerciseView()
        }

        val onTick = { secondsUntilFinished: Int ->
            progressbar_rest_view.progress = secondsUntilFinished
            textView_timer_rest_view.text = secondsUntilFinished.toString()
        }

        startCountdown(REST_TIME_IN_MILLISECONDS, onFinish, onTick)
    }

    private fun setupExerciseView() {
        progressbar_exercise_view.max = (EXERCISE_TIME_IN_MILLISECONDS / 1000).toInt()
        progressbar_exercise_view.progress = 0
        linearlayout_excercise_view.visibility = View.VISIBLE

        val currentExercise = _excerciseList!![_currentExcercisePosition]
        imageView_exercise_image.setImageResource(currentExercise.getImage())
        textView_exercise_name.text = currentExercise.getName()
        _textToSpeech.speak(currentExercise.getName(), TextToSpeech.QUEUE_FLUSH, null, "")

        val onFinish = {
            linearlayout_excercise_view.visibility = View.GONE

            if(_currentExcercisePosition < _excerciseList!!.size - 1)
                setupRestView()
            else
                Toast.makeText(
                    this@ExerciseActivity,
                    "Congratulations! You have completed the 7 minutes workout",
                    Toast.LENGTH_SHORT
                ).show()
        }

        val onTick = { secondsUntilFinished: Int ->
            progressbar_exercise_view.progress = secondsUntilFinished
            textView_timer_exercise_view.text = secondsUntilFinished.toString()
        }

        startCountdown(EXERCISE_TIME_IN_MILLISECONDS, onFinish, onTick)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS) {
            val result = _textToSpeech.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("Text To Speech", "The specified language is not supported!")
            }
        } else {
            Log.e("Text To Speach", "Initialization failed!")
        }
    }

    private fun setupExerciseStatusRecyclerView() {
        recyclerView_exerciseStatus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        _exerciseAdapter = ExerciseStatusAdapter(_excerciseList!!, this)
        recyclerView_exerciseStatus.adapter = _exerciseAdapter
    }
}
