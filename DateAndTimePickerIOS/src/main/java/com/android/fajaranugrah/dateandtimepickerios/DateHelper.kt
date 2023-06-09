package com.android.fajaranugrah.dateandtimepickerios

import java.util.*

class DateHelper {
    // Don't use static, as timezone may change while app is alive
    @JvmField internal var timeZone: TimeZone? = TimeZone.getDefault()

    constructor() {
        timeZone = TimeZone.getDefault()
    }

    constructor(timeZone: TimeZone) {
        this.timeZone = timeZone
    }

    fun setTimeZone(timeZoneValue: TimeZone) {
        timeZone = timeZoneValue
    }

    fun getTimeZone(): TimeZone {
        return timeZone ?: TimeZone.getDefault()
    }

    fun getCalendarOfDate(date: Date?): Calendar {
        val calendar = Calendar.getInstance(getTimeZone())
        calendar.time = date!!
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0
        return calendar
    }

    fun getHour(date: Date?): Int {
        return getCalendarOfDate(date)[Calendar.HOUR]
    }

    fun getHourOfDay(date: Date?): Int {
        return getCalendarOfDate(date)[Calendar.HOUR]
    }

    fun getHour(date: Date?, isAmPm: Boolean): Int {
        return if (isAmPm) {
            getHourOfDay(date)
        } else {
            getHour(date)
        }
    }

    fun getMinuteOf(date: Date?): Int {
        return getCalendarOfDate(date)[Calendar.MINUTE]
    }

    fun today(): Date {
        val now = Calendar.getInstance(getTimeZone())
        return now.time
    }

    fun getMonth(date: Date?): Int {
        return getCalendarOfDate(date)[Calendar.MONTH]
    }

    fun getDay(date: Date?): Int {
        return getCalendarOfDate(date)[Calendar.DAY_OF_MONTH]
    }

    companion object {
        @JvmStatic
        fun compareDateIgnoreTime(first: Date, second: Date): Int {
            val firstZeroTime = getZeroTimeDateWithoutTimeZone(first)
            val secondZeroTime = getZeroTimeDateWithoutTimeZone(second)
            return firstZeroTime.compareTo(secondZeroTime)
        }

        private fun getZeroTimeDateWithoutTimeZone(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }
    }
}