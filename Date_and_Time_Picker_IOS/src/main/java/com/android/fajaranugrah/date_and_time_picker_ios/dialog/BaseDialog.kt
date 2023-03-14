package com.android.fajaranugrah.date_and_time_picker_ios.dialog

import android.graphics.Color
import androidx.annotation.ColorInt
import com.android.fajaranugrah.date_and_time_picker_ios.widget.DateAndTimeIOSConstants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Fajar Anugrah Ramadhan on 14/Mar/2023.
 */
abstract class BaseDialog {
    var isDisplaying = false
        private set

    @JvmField
    @ColorInt
    protected var backgroundColor: Int? = Color.WHITE

    @JvmField
    @ColorInt
    protected var mainColor: Int? = Color.BLUE

    @JvmField
    @ColorInt
    protected var titleTextColor: Int? = null
    @JvmField
    protected var okClicked = false
    @JvmField
    protected var curved = false
    @JvmField
    protected var mustBeOnFuture = false
    @JvmField
    protected var minutesStep = DateAndTimeIOSConstants.STEP_MINUTES_DEFAULT
    @JvmField
    protected var minDate: Date? = null
    @JvmField
    protected var maxDate: Date? = null
    @JvmField
    protected var defaultDate: Date? = null
    @JvmField
    protected var displayDays = false
    @JvmField
    protected var displayMinutes = false
    @JvmField
    protected var displayHours = false
    @JvmField
    protected var displayDaysOfMonth = false
    @JvmField
    protected var displayMonth = false
    @JvmField
    protected var displayYears = false
    @JvmField
    protected var displayMonthNumbers = false
    @JvmField
    protected var isAmPm: Boolean? = null
    @JvmField
    protected var dayFormatter: SimpleDateFormat? = null
    @JvmField
    protected var customLocale: Locale? = null
    open fun display() {
        isDisplaying = true
    }

    open fun close() {
        isDisplaying = false
    }

    open fun dismiss() {
        isDisplaying = false
    }

    fun setBackgroundColor(@ColorInt backgroundColor: Int?) {
        this.backgroundColor = backgroundColor
    }

    fun setMainColor(@ColorInt mainColor: Int?) {
        this.mainColor = mainColor
    }

    fun setTitleTextColor(@ColorInt titleTextColor: Int) {
        this.titleTextColor = titleTextColor
    }

    protected open fun onClose() {
        isDisplaying = false
    }

    companion object {
        const val DEFAULT_ITEM_COUNT_MODE_CURVED = 7
        const val DEFAULT_ITEM_COUNT_MODE_NORMAL = 5
    }
}