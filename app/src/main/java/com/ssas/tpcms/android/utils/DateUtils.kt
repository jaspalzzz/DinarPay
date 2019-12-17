package com.ssas.tpcms.android.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {
    fun onDateClick(context: Context,callBack: DateCallBack) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd")
                //val formatapi = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val formatapi = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val strDate = format.format(calendar.time)
                val formatDate=formatapi.format(calendar.time)
                callBack.onDateSelected(strDate,formatDate)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun onDOBPicker(context: Context,callBack: DateCallBack) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatapi = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val strDate = format.format(calendar.time)
            val formatDate=formatapi.format(calendar.time)
            callBack.onDateSelected(strDate,formatDate)
        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate=c.timeInMillis
        datePickerDialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat("yyyy-MM-dd")
        return df.format(c)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val c = df.parse(date)
        println("Current time => $c")

        val df1 = SimpleDateFormat("dd MMM")
        return df1.format(c)
    }

    interface DateCallBack{
        fun onDateSelected(date: String, formatDate: String)
    }

    fun getLastTime(applicationDate:String):String{
        var currentTime=Date().time
        var sdf=SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ")
        var applicationTime=sdf.parse(applicationDate)
        var diff=currentTime-applicationTime.time

        val diffSeconds = diff / 1000
        val diffMinutes = diff / (60 * 1000) % 60
        val diffHours = diff / (60 * 60 * 1000) % 24
        val diffDays = diff / (24 * 60 * 60 * 1000)

        var time: String = ""

        if (diffDays > 0) {
            if (diffDays == 1L) {
                time = diffDays.toString() + " day ago "
            } else {
                time = diffDays.toString() + " days ago "
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1L) {
                    time = diffHours.toString() + " hours ago"
                } else {
                    time = diffHours.toString() + " hours ago"
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1L) {
                        time = diffMinutes.toString() + " minutes ago"
                    } else {
                        time = diffMinutes.toString() + " minutes ago"
                    }
                }
            }

        }
        return time
    }
}