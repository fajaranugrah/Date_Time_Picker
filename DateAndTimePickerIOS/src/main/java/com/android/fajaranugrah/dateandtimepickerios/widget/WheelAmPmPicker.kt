package com.android.fajaranugrah.dateandtimepickerios.widget

import android.content.Context
import android.util.AttributeSet
import com.android.fajaranugrah.dateandtimepickerios.R
import java.util.*

class WheelAmPmPicker @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
) : WheelPicker<String?>(context, attrs) {
    private var amPmListener: AmPmListener? = null

    override fun init() {}
    override fun initDefault(): String {
        return if (dateHelper.getHour(
                dateHelper.today(),
                true
            ) >= DateAndTimeIOSConstants.MAX_HOUR_AM_PM
        ) {
            getLocalizedString(R.string.picker_pm)
        } else {
            getLocalizedString(R.string.picker_am)
        }
    }

    override fun generateAdapterValues(showOnlyFutureDates: Boolean): List<String> {
        return Arrays.asList(
            getLocalizedString(R.string.picker_am),
            getLocalizedString(R.string.picker_pm)
        )
    }

    override fun findIndexOfDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.timeZone = dateHelper.getTimeZone()
        calendar.time = date
        val hours = calendar[Calendar.HOUR_OF_DAY]
        return if (hours >= DateAndTimeIOSConstants.MAX_HOUR_AM_PM) {
            1
        } else {
            0
        }
    }

    fun setAmPmListener(amPmListener: AmPmListener?) {
        this.amPmListener = amPmListener
    }

    override fun onItemSelected(position: Int, item: String?) {
        super.onItemSelected(position, item)
        if (amPmListener != null) {
            amPmListener!!.onAmPmChanged(this, isAm)
        }
    }

    override fun setCyclic(isCyclic: Boolean) {
        super.setCyclic(false)
    }

    fun isAmPosition(position: Int): Boolean {
        return position == INDEX_AM
    }

    override fun getFormattedValue(value: Any): String {
        if (value is Date) {
            val instance = Calendar.getInstance()
            instance.timeZone = dateHelper.getTimeZone()
            instance.time = value
            return getLocalizedString(if (instance[Calendar.AM_PM] == Calendar.PM) R.string.picker_pm else R.string.picker_am)
        }
        return value.toString()
    }

    val isAm: Boolean
        get() = currentItemPosition == INDEX_AM
    val isPm: Boolean
        get() = currentItemPosition == INDEX_PM

    interface AmPmListener {
        fun onAmPmChanged(pmPicker: WheelAmPmPicker?, isAm: Boolean)
    }

    companion object {
        const val INDEX_AM = 0
        const val INDEX_PM = 1
    }
}