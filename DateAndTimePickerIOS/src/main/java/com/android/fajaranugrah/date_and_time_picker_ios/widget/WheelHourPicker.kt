package com.android.fajaranugrah.date_and_time_picker_ios.widget

import android.content.Context
import android.util.AttributeSet
import com.android.fajaranugrah.date_and_time_picker_ios.widget.DateAndTimeIOSConstants.MAX_HOUR_AM_PM
import com.android.fajaranugrah.date_and_time_picker_ios.widget.DateAndTimeIOSConstants.MAX_HOUR_DEFAULT
import com.android.fajaranugrah.date_and_time_picker_ios.widget.DateAndTimeIOSConstants.MIN_HOUR_DEFAULT
import com.android.fajaranugrah.date_and_time_picker_ios.widget.DateAndTimeIOSConstants.STEP_HOURS_DEFAULT
import java.util.*

class WheelHourPicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<String?>(context, attrs) {
    private var minHour = 0
    private var maxHour = 0
    private var hoursStep = 0
    @JvmField
    var isAmPm = false
    private var finishedLoopListener: FinishedLoopListener? = null
    private var hourChangedListener: OnHourChangedListener? = null

    override fun init() {
        isAmPm = false
        minHour = MIN_HOUR_DEFAULT
        maxHour = MAX_HOUR_DEFAULT
        hoursStep = STEP_HOURS_DEFAULT
    }

    override fun initDefault(): String {
        return dateHelper.getHour(dateHelper.today(), isAmPm).toString()
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<String> {
        val hours: MutableList<String> = ArrayList()
        if (isAmPm) {
            hours.add(getFormattedValue(12))
            var hour = hoursStep
            while (hour < maxHour) {
                hours.add(getFormattedValue(hour))
                hour += hoursStep
            }
        } else {
            var hour = minHour
            while (hour <= maxHour) {
                hours.add(getFormattedValue(hour))
                hour += hoursStep
            }
        }
        return hours
    }

    override fun findIndexOfDate(date: Date): Int {
        if (isAmPm) {
            val hours = date.hours
            if (hours >= MAX_HOUR_AM_PM) {
                val copy = Date(date.time)
                copy.hours = hours % MAX_HOUR_AM_PM
                return super.findIndexOfDate(copy)
            }
        }
        return super.findIndexOfDate(date)
    }

    override fun getFormattedValue(value: Any): String {
        var valueItem = value
        if (value is Date) {
            val instance = Calendar.getInstance()
            instance.timeZone = dateHelper.getTimeZone()
            instance.time = value
            valueItem = instance[Calendar.HOUR_OF_DAY]
        }
        return String.format(currentLocale, FORMAT, valueItem)
    }

    override fun setDefault(defaultValue: String?) {
        try {
            var hour = defaultValue!!.toInt()
            if (isAmPm && hour >= MAX_HOUR_AM_PM) {
                hour -= MAX_HOUR_AM_PM
            }
            super.setDefault(getFormattedValue(hour))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setIsAmPm(isAmPm: Boolean) {
        this.isAmPm = isAmPm
        if (isAmPm) {
            setMaxHour(MAX_HOUR_AM_PM)
        } else {
            setMaxHour(MAX_HOUR_DEFAULT)
        }
        updateAdapter()
    }

    fun setMaxHour(maxHour: Int) {
        if (maxHour >= MIN_HOUR_DEFAULT && maxHour <= MAX_HOUR_DEFAULT) {
            this.maxHour = maxHour
        }
        notifyDatasetChanged()
    }

    fun setMinHour(minHour: Int) {
        if (minHour >= MIN_HOUR_DEFAULT && minHour <= MAX_HOUR_DEFAULT) {
            this.minHour = minHour
        }
        notifyDatasetChanged()
    }

    fun setStepSizeHours(hoursStep: Int) {
        if (hoursStep >= MIN_HOUR_DEFAULT && hoursStep <= MAX_HOUR_DEFAULT) {
            this.hoursStep = hoursStep
        }
        notifyDatasetChanged()
    }

    private fun convertItemToHour(item: Any): Int {
        var hour = Integer.valueOf(item.toString())
        if (!isAmPm) {
            return hour
        }
        if (hour == 12) {
            hour = 0
        }
        return hour
    }

    val currentHour: Int
        get() = convertItemToHour(adapter!!.getItem(currentItemPosition)!!)

    override fun onItemSelected(position: Int, item: String?) {
        super.onItemSelected(position, item)
        if (hourChangedListener != null) {
            hourChangedListener!!.onHourChanged(this, convertItemToHour(item!!))
        }
    }

    fun setOnFinishedLoopListener(finishedLoopListener: FinishedLoopListener?): WheelHourPicker {
        this.finishedLoopListener = finishedLoopListener
        return this
    }

    fun setHourChangedListener(hourChangedListener: OnHourChangedListener?): WheelHourPicker {
        this.hourChangedListener = hourChangedListener
        return this
    }

    override fun onFinishedLoop() {
        super.onFinishedLoop()
        if (finishedLoopListener != null) {
            finishedLoopListener!!.onFinishedLoop(this)
        }
    }

    interface FinishedLoopListener {
        fun onFinishedLoop(picker: WheelHourPicker?)
    }

    interface OnHourChangedListener {
        fun onHourChanged(picker: WheelHourPicker?, hour: Int)
    }
}