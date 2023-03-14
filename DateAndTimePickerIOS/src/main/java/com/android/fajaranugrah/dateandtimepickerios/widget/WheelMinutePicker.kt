package com.android.fajaranugrah.dateandtimepickerios.widget

import android.content.Context
import android.util.AttributeSet
import com.android.fajaranugrah.dateandtimepickerios.widget.DateAndTimeIOSConstants.MAX_MINUTES
import com.android.fajaranugrah.dateandtimepickerios.widget.DateAndTimeIOSConstants.MIN_MINUTES
import com.android.fajaranugrah.dateandtimepickerios.widget.DateAndTimeIOSConstants.STEP_MINUTES_DEFAULT
import java.util.*

class WheelMinutePicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<String?>(context, attrs) {
    private var stepMinutes = 0
    private var onMinuteChangedListener: OnMinuteChangedListener? = null
    private var onFinishedLoopListener: OnFinishedLoopListener? = null

    override fun init() {
        stepMinutes = STEP_MINUTES_DEFAULT
    }

    override fun initDefault(): String {
        val now = Calendar.getInstance()
        now.timeZone = dateHelper.getTimeZone()
        return getFormattedValue(now[Calendar.MINUTE])
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<String> {
        val minutes: MutableList<String> = ArrayList()
        var min: Int = MIN_MINUTES
        while (min <= MAX_MINUTES) {
            minutes.add(getFormattedValue(min))
            min += stepMinutes
        }
        return minutes
    }

    private fun findIndexOfMinute(minute: Int): Int {
        val itemCount = adapter!!.itemCount
        for (i in 0 until itemCount) {
            val `object` = adapter!!.getItemText(i)
            val value = Integer.valueOf(`object`)
            if (minute == value) {
                return i
            }
            if (minute < value) {
                return i - 1
            }
        }
        return itemCount - 1
    }

    override fun findIndexOfDate(date: Date): Int {
        return findIndexOfMinute(dateHelper.getMinuteOf(date))
    }

    override fun getFormattedValue(value: Any): String {
        var valueItem = value
        if (value is Date) {
            val instance = Calendar.getInstance()
            instance.timeZone = dateHelper.getTimeZone()
            instance.time = value
            valueItem = instance[Calendar.MINUTE]
        }
        return String.format(currentLocale, FORMAT, valueItem)
    }

    fun setStepSizeMinutes(stepMinutes: Int) {
        if (stepMinutes < 60 && stepMinutes > 0) {
            this.stepMinutes = stepMinutes
            updateAdapter()
        }
    }

    private fun convertItemToMinute(item: Any): Int {
        return Integer.valueOf(item.toString())
    }

    val currentMinute: Int
        get() = convertItemToMinute(adapter!!.getItem(currentItemPosition)!!)

    fun setOnMinuteChangedListener(onMinuteChangedListener: OnMinuteChangedListener?): WheelMinutePicker {
        this.onMinuteChangedListener = onMinuteChangedListener
        return this
    }

    fun setOnFinishedLoopListener(onFinishedLoopListener: OnFinishedLoopListener?): WheelMinutePicker {
        this.onFinishedLoopListener = onFinishedLoopListener
        return this
    }

    protected override fun onItemSelected(position: Int, item: String?) {
        super.onItemSelected(position, item)
        if (onMinuteChangedListener != null) {
            onMinuteChangedListener!!.onMinuteChanged(this, convertItemToMinute(item!!))
        }
    }

    override fun onFinishedLoop() {
        super.onFinishedLoop()
        if (onFinishedLoopListener != null) {
            onFinishedLoopListener!!.onFinishedLoop(this)
        }
    }

    interface OnMinuteChangedListener {
        fun onMinuteChanged(picker: WheelMinutePicker?, minutes: Int)
    }

    interface OnFinishedLoopListener {
        fun onFinishedLoop(picker: WheelMinutePicker?)
    }
}