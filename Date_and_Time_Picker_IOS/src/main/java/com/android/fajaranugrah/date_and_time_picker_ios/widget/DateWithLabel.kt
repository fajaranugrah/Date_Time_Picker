package com.android.fajaranugrah.date_and_time_picker_ios.widget

import com.android.fajaranugrah.date_and_time_picker_ios.DateHelper.Companion.compareDateIgnoreTime
import java.util.*

class DateWithLabel(val label: String, val date: Date?) {
    init {
        requireNotNull(date) { "null value provided. label=[$label], date=[$date]" }
    }

    override fun toString(): String {
        return label
    }

    override fun hashCode(): Int {
        throw IllegalStateException("Not implemented")
    }

    override fun equals(o: Any?): Boolean {
        if (o is DateWithLabel) {
            val newDate = o
            return label == newDate.label && compareDateIgnoreTime(date!!, newDate.date!!) == 0
        }
        return false
    }
}