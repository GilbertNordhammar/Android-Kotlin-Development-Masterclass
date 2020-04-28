package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    var lastNumeric = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View) {
        tvInput.append((view as Button).text )
        lastNumeric = true
    }

    fun onClear(view: View) {
        tvInput.text = ""

        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if(lastNumeric && !lastDot) {
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        val input = tvInput.text.toString()
        if(lastNumeric && !containsOperator(input)){
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun containsOperator(value: String) : Boolean {
        var value = value
        if(value.startsWith("-"))
            value = value.substring(1)

        return value.contains("/") || value.contains("*") || value.contains("-")
    }

    fun onEqual(view: View) {
        if(lastNumeric) {
            var tvValue = tvInput.text.toString()
            var prefix = ""
            var result = 0.0;

            if(tvValue.startsWith("-")) {
                prefix = "-"
                tvValue = tvValue.substring(1)
            }

            if(tvValue.contains("-")) {
                val splitValues = tvValue.split("-")

                val leftNumb = (prefix + splitValues[0]).toDouble()
                val rightNumb = splitValues[1].toDouble()

                result = leftNumb - rightNumb
            } else if(tvValue.contains("+")) {
                val splitValues = tvValue.split("+")

                val leftNumb = (prefix + splitValues[0]).toDouble()
                val rightNumb = splitValues[1].toDouble()

                result = leftNumb + rightNumb
            } else if(tvValue.contains("/")) {
                val splitValues = tvValue.split("/")

                val leftNumb = (prefix + splitValues[0]).toDouble()
                val rightNumb = splitValues[1].toDouble()

                result = leftNumb / rightNumb
            } else if(tvValue.contains("*")) {
                val splitValues = tvValue.split("*")

                val leftNumb = (prefix + splitValues[0]).toDouble()
                val rightNumb = splitValues[1].toDouble()

                result = leftNumb * rightNumb
            }
            tvInput.text = removeZeroAfterDot(result.toString())
        }
    }

    private fun removeZeroAfterDot(result: String) : String {
        var value = result
        if (result.endsWith(".0"))
            value = result.substring(0, result.length - 2)
        return value
    }

}
