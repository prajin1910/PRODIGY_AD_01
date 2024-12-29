package com.example.calcuator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentInput = ""
    private var lastOperator = ""
    private var result = 0.0
    private var isOperatorClicked = false
    private var hasDecimal = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)


        display = findViewById(R.id.display)

        // Number buttons
        val buttons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )
        for (buttonId in buttons) {
            findViewById<Button>(buttonId).setOnClickListener { onNumberClick(it as Button) }
        }

        // Operator buttons
        findViewById<Button>(R.id.btn_plus).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btn_minus).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btn_multiply).setOnClickListener { onOperatorClick("*") }
        findViewById<Button>(R.id.btn_divide).setOnClickListener { onOperatorClick("/") }

        // Function buttons
        findViewById<Button>(R.id.btn_equals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.btn_clear).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.btn_delete).setOnClickListener { onDeleteClick() }
        findViewById<Button>(R.id.btn_dot).setOnClickListener { onDotClick() }

    }
    private fun onNumberClick(button: Button) {
        if (isOperatorClicked) {
            currentInput = ""
            isOperatorClicked = false
            hasDecimal = false // Reset decimal flag after operator
        }
        currentInput += button.text
        display.text = currentInput
    }

    private fun onOperatorClick(operator: String) {
        if (currentInput.isNotEmpty() && currentInput != "." && currentInput != "-") {
            try {
                if (lastOperator.isNotEmpty()) {
                    calculate()
                } else {
                    result = currentInput.toDouble()
                }
                lastOperator = operator
                isOperatorClicked = true
                hasDecimal = false // Reset decimal flag after operator
            } catch (e: NumberFormatException) {
                display.text = "Error: Invalid Input"
                currentInput = ""
            }
        }
    }

    private fun onEqualsClick() {
        if (lastOperator.isNotEmpty() && currentInput.isNotEmpty() && currentInput != "." && currentInput != "-") {
            try {
                calculate()
                display.text = result.toString()
                lastOperator = ""
                hasDecimal = false
            } catch (e: NumberFormatException) {
                display.text = "Error: Invalid Input"
                currentInput = ""
            }
        }
    }

    private fun calculate() {
        try {
            val inputValue = currentInput.toDouble()
            when (lastOperator) {
                "+" -> result += inputValue
                "-" -> result -= inputValue
                "*" -> result *= inputValue
                "/" -> {
                    if (inputValue == 0.0) {
                        display.text = "Error: Division by zero"
                        return // Exit to prevent crash
                    } else {
                        result /= inputValue
                    }
                }
            }
            currentInput = result.toString()
        } catch (e: NumberFormatException) {
            display.text = "Error: Invalid Input"
            currentInput = ""
        }
    }

    private fun onClearClick() {
        currentInput = ""
        lastOperator = ""
        result = 0.0
        display.text = "0"
        hasDecimal=false
    }

    private fun onDeleteClick() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            display.text = if (currentInput.isEmpty()) "0" else currentInput
            if(!currentInput.contains(".")) hasDecimal=false;
        }
    }

    private fun onDotClick() {
        if (!hasDecimal) {
            if(currentInput.isEmpty()) currentInput="0"
            currentInput += "."
            display.text = currentInput
            hasDecimal = true
        }
    }
}