package com.android.fajaranugrah.dateandtimepickerios.widget

import android.content.Context
import android.util.AttributeSet
import com.android.fajaranugrah.dateandtimepickerios.R
import java.text.SimpleDateFormat
import java.util.*

class WheelYearPicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<String?>(context, attrs) {
    private var simpleDateFormat: SimpleDateFormat? = null
    @JvmField
    internal var minYear = 0
    @JvmField
    internal var maxYear = 0
    private var onYearSelectedListener: OnYearSelectedListener? = null

    override fun init() {
        simpleDateFormat = SimpleDateFormat("yyyy", currentLocale)
        val instance = Calendar.getInstance()
        instance.timeZone = dateHelper.getTimeZone()
        val currentYear = instance[Calendar.YEAR]
        minYear = currentYear - DateAndTimeIOSConstants.MIN_YEAR_DIFF
        maxYear = currentYear + DateAndTimeIOSConstants.MAX_YEAR_DIFF
    }

    override fun initDefault(): String {
        return todayText
    }

    private val todayText: String
        private get() = getLocalizedString(R.string.picker_today)

    protected override fun onItemSelected(position: Int, item: String?) {
        if (onYearSelectedListener != null) {
            val year = convertItemToYear(position)
            onYearSelectedListener!!.onYearSelected(this, position, year)
        }
    }

    fun setMaxYear(maxYear: Int) {
        this.maxYear = maxYear
        notifyDatasetChanged()
    }

    fun setMinYear(minYear: Int) {
        this.minYear = minYear
        notifyDatasetChanged()
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<String>? {
        val years: MutableList<String> = ArrayList()
        val instance = Calendar.getInstance()
        instance.timeZone = dateHelper.getTimeZone()
        instance[Calendar.YEAR] = minYear - 1
        for (i in minYear..maxYear) {
            instance.add(Calendar.YEAR, 1)
            years.add(getFormattedValue(instance.time))
        }
        return years
    }

    override fun getFormattedValue(value: Any): String {
        return simpleDateFormat!!.format(value)
    }

    fun setOnYearSelectedListener(onYearSelectedListener: OnYearSelectedListener?) {
        this.onYearSelectedListener = onYearSelectedListener
    }

    val currentYear: Int
        get() = convertItemToYear(super.currentItemPosition)

    private fun convertItemToYear(itemPosition: Int): Int {
        return minYear + itemPosition
    }

    interface OnYearSelectedListener {
        fun onYearSelected(picker: WheelYearPicker?, position: Int, year: Int)
    }
}