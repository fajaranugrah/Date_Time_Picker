package com.android.fajaranugrah.dateandtimepickerios.widget

import android.content.Context
import android.util.AttributeSet
import com.android.fajaranugrah.dateandtimepickerios.R
import com.android.fajaranugrah.dateandtimepickerios.widget.DateAndTimeIOSConstants.DAYS_PADDING
import java.text.SimpleDateFormat
import java.util.*

class WheelDayPicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<DateWithLabel?>(context, attrs)  {
    private var simpleDateFormat: SimpleDateFormat? = null
    private var customDateFormat: SimpleDateFormat? = null
    private var dayCount: Int = DAYS_PADDING
    private var onDaySelectedListener: OnDaySelectedListener? = null

    override fun init() {
        simpleDateFormat = SimpleDateFormat(DAY_FORMAT_PATTERN, currentLocale)
        simpleDateFormat!!.timeZone = dateHelper.getTimeZone()
    }

    override fun setCustomLocale(customLocale: Locale?) {
        super.setCustomLocale(customLocale)
        simpleDateFormat = SimpleDateFormat(DAY_FORMAT_PATTERN, currentLocale)
        simpleDateFormat!!.timeZone = dateHelper.getTimeZone()
    }

    override fun initDefault(): DateWithLabel {
        return DateWithLabel(todayText, Date())
    }

    private val todayText: String
        private get() = getLocalizedString(R.string.picker_today)

    protected override fun onItemSelected(position: Int, item: DateWithLabel?) {
        if (onDaySelectedListener != null) {
            onDaySelectedListener!!.onDaySelected(this, position, item?.label, item?.date)
        }
    }

    fun setDayCount(dayCount: Int) {
        this.dayCount = dayCount
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<DateWithLabel> {
        val days: MutableList<DateWithLabel> = ArrayList()
        var instance = Calendar.getInstance()
        instance.timeZone = dateHelper.getTimeZone()
        val startDayOffset = if (showOnlyFutureDates) 0 else -1 * dayCount
        instance.add(Calendar.DATE, startDayOffset - 1)
        for (i in startDayOffset..-1) {
            instance.add(Calendar.DAY_OF_MONTH, 1)
            val date = instance.time
            days.add(DateWithLabel(getFormattedValue(date), date))
        }

        //today
        days.add(DateWithLabel(todayText, Date()))
        instance = Calendar.getInstance()
        instance.timeZone = dateHelper.getTimeZone()
        for (i in 0 until dayCount) {
            instance.add(Calendar.DATE, 1)
            val date = instance.time
            days.add(DateWithLabel(getFormattedValue(date), date))
        }
        return days
    }

    override fun getFormattedValue(value: Any): String {
        return dateFormat!!.format(value)
    }

    fun setDayFormatter(simpleDateFormat: SimpleDateFormat): WheelDayPicker {
        simpleDateFormat.timeZone = dateHelper.getTimeZone()
        customDateFormat = simpleDateFormat
        updateAdapter()
        return this
    }

    fun setOnDaySelectedListener(onDaySelectedListener: OnDaySelectedListener?) {
        this.onDaySelectedListener = onDaySelectedListener
    }

    val currentDate: Date
        get() = convertItemToDate(super.currentItemPosition)
    private val dateFormat: SimpleDateFormat?
        private get() = if (customDateFormat != null) {
            customDateFormat
        } else simpleDateFormat

    private fun convertItemToDate(itemPosition: Int): Date {
        val date: Date
        val itemText = adapter!!.getItemText(itemPosition)
        val todayCalendar = Calendar.getInstance()
        todayCalendar.timeZone = dateHelper.getTimeZone()
        var todayPosition = -1
        val data: MutableList<DateWithLabel?>? = adapter!!.data
        for (i in data!!.indices) {
            if (data[i]?.label == todayText) {
                todayPosition = i
                break
            }
        }
        date = if (todayText == itemText) {
            todayCalendar.time
        } else {
            todayCalendar.add(Calendar.DAY_OF_YEAR, itemPosition - todayPosition)
            todayCalendar.time
        }
        return date
    }

    fun setTodayText(today: DateWithLabel?) {
        val data: MutableList<DateWithLabel?>? = adapter!!.data
        for (i in data!!.indices) {
            if (data[i]?.label == todayText) {
                adapter!!.data?.set(i, today)
                notifyDatasetChanged()
            }
        }
    }

    interface OnDaySelectedListener {
        fun onDaySelected(picker: WheelDayPicker?, position: Int, name: String?, date: Date?)
    }

    companion object {
        private const val DAY_FORMAT_PATTERN = "EEE d MMM"
    }
}