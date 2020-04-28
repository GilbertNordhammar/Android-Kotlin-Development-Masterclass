package com.example.ageinminutes

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDatePicker.setOnClickListener {view ->
            clickDatePicker(view)
        }

    }

    private fun clickDatePicker(view: View) {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener {
                    view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month+1}/$year"
                tvSelectedDate.text = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                val theDate = sdf.parse("$dayOfMonth/${month+1}/$year")
                val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

                val selectedDateInMinutes = theDate!!.time / 60000
                val currentDateInMinutes = currentDate!!.time / 60000
                val ageInMinutes = currentDateInMinutes - selectedDateInMinutes;
                tvAgeInMinutes.text = ageInMinutes.toString()

                val selectedDateInDays = theDate!!.time / (60000 * 60 * 24)
                val currentDateInDays = currentDate!!.time / (60000 * 60 * 24)
                val ageInDays = currentDateInDays - selectedDateInDays;
                tvAgeInDays.text = ageInDays.toString()
            },
            year,
            month,
            day)

        dpd.datePicker.maxDate = Date().time - 86_400_000
        dpd.show()
    }
}
