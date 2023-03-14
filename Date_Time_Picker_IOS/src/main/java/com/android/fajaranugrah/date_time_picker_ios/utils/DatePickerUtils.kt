package com.android.fajaranugrah.date_time_picker_ios.utils

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by Fajar Anugrah Ramadhan on 09-03-2023.
 * Contact email - fajarconan@gmail.com
 */
const val INVISIBLE_VIEWS = 2
const val AM = "AM"
const val PM = "PM"

class DatePickerUtils(private val startDate: Calendar, private val endDate: Calendar) {

    var currentSelectedHour = 0
    var isPmSelectedUnvalidated = false

    init {
        isPmSelectedUnvalidated = startDate.get(Calendar.HOUR_OF_DAY) > 12
    }

    fun getAllDates(): ArrayList<Calendar> {

        val calendarStartDate = Calendar.getInstance().also { it.time = startDate.time }
        val calendarEndDate = Calendar.getInstance().also { it.time = endDate.time }
        val dates = ArrayList<Calendar>()

        calendarStartDate.add(Calendar.DAY_OF_YEAR, -(2 * INVISIBLE_VIEWS))
        calendarEndDate.add(Calendar.DAY_OF_YEAR, INVISIBLE_VIEWS)

        val totalDaysBetweenEnds =
            TimeUnit.MILLISECONDS.toDays(calendarEndDate.timeInMillis - calendarStartDate.timeInMillis)
                .toInt()

        for (count in 0..totalDaysBetweenEnds) {
            val date =
                Calendar.getInstance().also { it.timeInMillis = calendarStartDate.timeInMillis }
            dates.add(date.also { it.add(Calendar.DAY_OF_YEAR, count) })
        }

        return dates
    }

    fun getHours(is24Hour: Boolean): ArrayList<Int> {

        val hours = ArrayList<Int>()
        if (is24Hour) {
            for (hour in 0..23) hours.add(hour)
        } else {
            for (hour in 1..12) hours.add(hour)
        }

        return hours
    }

    fun getMinutes(): ArrayList<Int> {
        val minutes = ArrayList<Int>()
        for (hour in 0..59) minutes.add(hour)
        return minutes
    }

    fun getMeridiem(): ArrayList<String> {
        val meridiem = ArrayList<String>()
        meridiem.add(AM)
        meridiem.add(PM)
        return meridiem
    }


    fun addEmptyValue(list: ArrayList<Int>): ArrayList<Int> {
        list.add(0, -1)
        list.add(1, -1)
        list.add(2, -1)
        list.add(-1)
        list.add(-1)
        return list
    }

    fun addEmptyValueInString(list: ArrayList<String>): ArrayList<String> {
        list.add(0, "")
        list.add(1, "")
        list.add("")
        list.add("")
        return list
    }
}