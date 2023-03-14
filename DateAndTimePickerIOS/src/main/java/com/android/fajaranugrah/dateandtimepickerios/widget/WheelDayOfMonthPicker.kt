package com.android.fajaranugrah.dateandtimepickerios.widget

import android.content.Context
import android.util.AttributeSet


class WheelDayOfMonthPicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<String?>(context, attrs) {
    var daysInMonth = 0
    private var dayOfMonthSelectedListener: DayOfMonthSelectedListener? = null
    private var finishedLoopListener: FinishedLoopListener? = null
    override fun init() {
        // no-op here
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<String> {
        val dayList: MutableList<String> = ArrayList()
        for (i in 1..daysInMonth) {
            dayList.add(String.format("%02d", i))
        }
        return dayList
    }

    override fun initDefault(): String {
        return dateHelper.getDay(dateHelper.today()).toString()
    }

    fun setOnFinishedLoopListener(finishedLoopListener: FinishedLoopListener?) {
        this.finishedLoopListener = finishedLoopListener
    }

    override fun onFinishedLoop() {
        super.onFinishedLoop()
        if (finishedLoopListener != null) {
            finishedLoopListener!!.onFinishedLoop(this)
        }
    }

    fun setDayOfMonthSelectedListener(listener: DayOfMonthSelectedListener?) {
        this.dayOfMonthSelectedListener = listener
    }

    protected override fun onItemSelected(position: Int, item: String?) {
        dayOfMonthSelectedListener?.onDayOfMonthSelected(this, position)
    }

    val currentDay: Int
        get() = currentItemPosition

    interface FinishedLoopListener {
        fun onFinishedLoop(picker: WheelDayOfMonthPicker?)
    }

    interface DayOfMonthSelectedListener {
        fun onDayOfMonthSelected(picker: WheelDayOfMonthPicker?, dayIndex: Int)
    }
}