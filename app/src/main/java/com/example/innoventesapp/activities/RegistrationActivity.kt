package com.example.innoventesapp.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.innoventesapp.R
import com.example.innoventesapp.singletons.Constants
import com.example.innoventesapp.viewmodels.RegistrationActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private val registrationActivityViewModel by viewModels<RegistrationActivityViewModel>()
    private lateinit var panNumberEditText: TextInputEditText
    private lateinit var panNumberLayoutEditText: TextInputLayout
    private lateinit var birthDayEditText: TextInputEditText
    private lateinit var birthMonthEditText: TextInputEditText
    private lateinit var birthYearEditText: TextInputEditText
    private lateinit var nextButton: MaterialButton
    private lateinit var noPanTextView: TextView
    private lateinit var dobWarningTextView: TextView
    private var isPanNumberValid=false
    private var isDateFormatValid=false
    private var isAgeValid=false
    private val dateFormatBuilder=StringBuilder()
    private var birthDayString=""
    private var birthMonthString=""
    private var birthYearString=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
        setUpListeners()
    }

    private fun setUpViews(){
        panNumberEditText=findViewById(R.id.panNumberEditText)
        panNumberLayoutEditText=findViewById(R.id.panNumberLayoutEditText)
        birthDayEditText=findViewById(R.id.dayEditText)
        birthMonthEditText=findViewById(R.id.monthEditText)
        birthYearEditText=findViewById(R.id.yearEditText)
        nextButton=findViewById(R.id.nextButton)
        noPanTextView=findViewById(R.id.noPanText)
        dobWarningTextView=findViewById(R.id.dobWarningTextView)
    }

    private fun setUpListeners(){
        nextButton.setOnClickListener {
        Toast.makeText(this, getString(R.string.submitted_string), Toast.LENGTH_LONG).show()
            finish()
        }

        noPanTextView.setOnClickListener {
            finish()
        }

        panNumberEditText.addTextChangedListener {

            isPanNumberValid=registrationActivityViewModel.validatePanNumber(it.toString())

            if (isPanNumberValid) {
                panNumberLayoutEditText.error = null
                hideTheKeyboard(panNumberEditText)
                birthDayEditText.requestFocus()
            }
            else
                panNumberLayoutEditText.error=getString(R.string.pan_number_error)

            allValidationDone()
        }

        birthDayEditText.addTextChangedListener {
            dateFormatBuilder.clear()
            birthDayString=it.toString()

            when(birthDayString.length){
                0, 1 -> {
                    dateFormatNotValidAlert()
                }

                2 -> {
                    dobValidationProcess(Constants.DOBValidationConstants.BIRTH_DATE_VALIDATION)
                }
            }

        }

        birthMonthEditText.addTextChangedListener {
            dateFormatBuilder.clear()
            birthMonthString=it.toString()

            when(birthMonthString.length){
                0, 1 -> {
                    dateFormatNotValidAlert()
                }

                2 -> {
                    dobValidationProcess(Constants.DOBValidationConstants.BIRTH_MONTH_VALIDATION)
                }
            }
        }

        birthYearEditText.addTextChangedListener {
            dateFormatBuilder.clear()
            birthYearString=it.toString()

            when(birthYearString.length){
                0, 1, 2, 3 -> {
                    dateFormatNotValidAlert()
                }

                4 -> {
                    dobValidationProcess(Constants.DOBValidationConstants.BIRTH_YEAR_VALIDATION)
                }
            }
        }
    }

    private fun dateFormatNotValidAlert(){
        dobWarningTextView.text=getString(R.string.date_format_warning)
        dobWarningTextView.visibility = View.VISIBLE
        isDateFormatValid = false
        allValidationDone()
    }

    private fun allValidationDone(){
        nextButton.isEnabled=isPanNumberValid && isDateFormatValid && isAgeValid
    }

    private fun hideTheKeyboard(view: View){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun dobValidationProcess(dobValidationConstants: Constants.DOBValidationConstants){
        when(dobValidationConstants){
            Constants.DOBValidationConstants.BIRTH_DATE_VALIDATION -> {
                if (birthMonthString.length == 2 && birthYearString.length == 4) {
                    dateFormatValidationUsingRegEx()
                } else {
                    dobWarningTextView.visibility = View.VISIBLE
                    birthMonthEditText.requestFocus()
                }

            }

            Constants.DOBValidationConstants.BIRTH_MONTH_VALIDATION -> {
                if (birthDayString.length == 2 && birthYearString.length == 4) {
                    dateFormatValidationUsingRegEx()
                } else {
                    dobWarningTextView.visibility = View.VISIBLE
                    birthYearEditText.requestFocus()
                }

            }

            Constants.DOBValidationConstants.BIRTH_YEAR_VALIDATION -> {
                if (birthDayString.length == 2 && birthMonthString.length == 2) {
                    dateFormatValidationUsingRegEx()
                } else {
                    dobWarningTextView.visibility = View.VISIBLE
                }

            }
        }



    }

    private fun dateFormatValidationUsingRegEx(){
        dateFormatBuilder.append("$birthDayString-$birthMonthString-$birthYearString")
        isDateFormatValid=registrationActivityViewModel.validateDateFormat(dateFormatBuilder.toString())

        if (isDateFormatValid) {

            dobWarningTextView.visibility = View.GONE
            isAgeValid=registrationActivityViewModel.validateAge(birthDayString.toInt(),
            birthMonthString.toInt(),birthYearString.toInt())

            if (isAgeValid){
                hideTheKeyboard(birthDayEditText)
            }
            else{
                dobWarningTextView.text=getString(R.string.age_warning)
                dobWarningTextView.visibility = View.VISIBLE
            }

            allValidationDone()
        }
    }



}