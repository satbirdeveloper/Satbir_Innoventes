package com.example.innoventesapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*
import java.util.regex.Pattern

class RegistrationActivityViewModel : ViewModel() {

   private val panCardRegExp="[A-Z]{5}[0-9]{4}[A-Z]{1}"
   private val dobRegExp="^(0[1-9]|[12][0-9]|3[01])[- .](0[1-9]|1[012])[- .](19|20)\\d\\d\$"

    fun validatePanNumber(panNumberText:String):Boolean{
       return Pattern.compile(panCardRegExp).matcher(panNumberText).matches()
    }

    fun validateDateFormat(dobText:String):Boolean{
        return Pattern.compile(dobRegExp).matcher(dobText).matches()
    }

     fun validateAge(birthDay:Int,birthMonth:Int,birthYear:Int):Boolean{
        val currentDate: Calendar = Calendar.getInstance()
         val cDay=currentDate.get(Calendar.DAY_OF_MONTH)
        val cMonth=currentDate.get(Calendar.MONTH)+1
        val cYear=currentDate.get(Calendar.YEAR)

        var restMonth:Int
        var restDay:Int

        var restYear = cYear - birthYear;

         if (cMonth >= birthMonth) {
            restMonth = cMonth - birthMonth;
        } else {
            restMonth = cMonth - birthMonth;
            restMonth += 12;
            restYear--;
        }


        if (cDay >= birthDay) {
            restDay = cDay - birthDay;
        } else {
            restDay = cDay - birthDay;
            restDay += 31;
            if (restMonth == 0) {
                restMonth = 11;
                restYear--;
            } else {
                restMonth--;
            }
        }


        return restYear>18
    }
}