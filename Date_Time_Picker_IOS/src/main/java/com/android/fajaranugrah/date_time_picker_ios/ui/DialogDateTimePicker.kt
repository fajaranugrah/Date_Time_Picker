package com.android.fajaranugrah.date_time_picker_ios.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.Window
import android.widget.Toast
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.fajaranugrah.date_time_picker_ios.R
import com.android.fajaranugrah.date_time_picker_ios.adapter.DateAdapter
import com.android.fajaranugrah.date_time_picker_ios.adapter.HourAdapter
import com.android.fajaranugrah.date_time_picker_ios.adapter.MeridiemAdapter
import com.android.fajaranugrah.date_time_picker_ios.adapter.MinuteAdapter
import com.android.fajaranugrah.date_time_picker_ios.databinding.DialogDateTimePickerBinding
import com.android.fajaranugrah.date_time_picker_ios.utils.*
import java.util.*

/**
 * Created by Fajar Anugrah Ramadhan on 5/16/2019.
 */
@Keep
class DialogDateTimePicker(
    context: Context,
    private val startDate: Calendar,
    private val maxMonthToDisplay: Int,
    private val dateTimeSelectedListener: OnDateTimeSelectedListener,
    private val title: String
) : Dialog(context, R.style.Theme_Custom_Dialog) {

    private lateinit var utils: DatePickerUtils
    private lateinit var dialogBinding: DialogDateTimePickerBinding

    private val selectedDateUnvalidated: Calendar = startDate
    val selectedDateTime: Calendar = startDate

    private var titleTextColor = R.color.colorPrimaryDark
    private var titleVisible = VISIBLE

    private var centerViewBgColor = R.color.colorPrimaryDark

    private var cancelTextColor = android.R.color.black
    private var cancelBgColor = android.R.color.white

    private var submitTextColor = android.R.color.black
    private var submitBgColor = android.R.color.white

    private var cancelText = context.resources.getString(R.string.cancel)
    private var submitText = context.resources.getString(R.string.submit)

    private var fontSize: Int = 14

    private var dividerHeight: Int = 38

    private var endDate: Calendar = Calendar.getInstance().also {
        it.timeInMillis = startDate.timeInMillis
        it.add(Calendar.MONTH, maxMonthToDisplay)
    }

    init {
        setOnShowListener { initDates(FAST_SPEED) }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialogBinding = DialogDateTimePickerBinding.inflate(LayoutInflater.from(context))
        window?.setGravity(Gravity.BOTTOM)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(dialogBinding.root)

        window?.setTransparentEdges()
        window?.setFullWidth()

        utils = DatePickerUtils(startDate, endDate)

        dialogBinding.title.text = title
        dialogBinding.title.visibility = titleVisible

        dialogBinding.title.setTextColor(ContextCompat.getColor(context, titleTextColor))
        dialogBinding.btnSubmit.setTextColor(ContextCompat.getColor(context, submitTextColor))
        dialogBinding.btnCancel.setTextColor(ContextCompat.getColor(context, cancelTextColor))

        dialogBinding.viewCenter.setBackgroundColor(
            ContextCompat.getColor(
                context,
                centerViewBgColor
            )
        )

        dialogBinding.btnSubmit.setBackgroundColor(ContextCompat.getColor(context, submitBgColor))
        dialogBinding.btnCancel.setBackgroundColor(ContextCompat.getColor(context, cancelBgColor))

        dialogBinding.btnSubmit.text = submitText
        dialogBinding.btnCancel.text = cancelText

        dialogBinding.viewCenter.layoutParams.height = dividerHeight.dpToPx(context).toInt()

        val dateAdapter = DateAdapter(utils.getAllDates(), fontSize, dividerHeight)
        val hourAdapter = HourAdapter(
            utils.addEmptyValue(
                utils.getHours(true)
            ),
            fontSize,
            dividerHeight
        )
        val minuteAdapter =
            MinuteAdapter(
                utils.addEmptyValue(utils.getMinutes()),
                fontSize,
                dividerHeight
            )
        val meridiemAdapter =
            MeridiemAdapter(
                utils.addEmptyValueInString(utils.getMeridiem()),
                fontSize,
                dividerHeight
            )

        dialogBinding.dateRv.initVerticalAdapter(dateAdapter, true)
        dialogBinding.hourRv.initVerticalAdapter(hourAdapter, true)
        //dialogBinding.meridiemRv.initVerticalAdapter(meridiemAdapter, true)
        dialogBinding.minuteRv.initVerticalAdapter(minuteAdapter, true)

        val dateSnapListener = object : CustomSnapHelper.SnapListener {
            override fun onViewSnapped(position: Int) {
                setSelectedDate(
                    dateAdapter.dates[position].get(Calendar.DAY_OF_YEAR),
                    dateAdapter.dates[position].get(Calendar.YEAR)
                )

                validateDateTime()
            }
        }

        val hourSnapListener = object : CustomSnapHelper.SnapListener {
            override fun onViewSnapped(position: Int) {
                /*if (position >= 3) {
                    //utils.currentSelectedHour = hourAdapter.hour[position]
                    setSelectedHour(
                        getFormattedHour(
                            utils.isPmSelectedUnvalidated,
                            hourAdapter.hour[position]
                        )
                    )
                } else {
                    setMinimumHour(dialogBinding.hourRv)
                }*/

                Log.e("checkIndexH", "${hourAdapter.hour[position]}")
                setSelectedHour(hourAdapter.hour[position])
                validateDateTime()
            }
        }

        val minuteSnapListener = object : CustomSnapHelper.SnapListener {
            override fun onViewSnapped(position: Int) {
                if (position >= 3) {
                    setSelectedMinute(minuteAdapter.minute[position])
                    validateDateTime()
                } else
                    setMinimumMinutes(dialogBinding.minuteRv)
            }
        }

        val meridiemSnapListener = object : CustomSnapHelper.SnapListener {
            override fun onViewSnapped(position: Int) {
                utils.isPmSelectedUnvalidated = meridiemAdapter.meridiem[position] == PM
                //setSelectedAmPm(position)
                validateDateTime()
            }
        }

        CustomSnapHelper(dialogBinding.dateRv, dateSnapListener)
        CustomSnapHelper(dialogBinding.hourRv, hourSnapListener)
        CustomSnapHelper(dialogBinding.minuteRv, minuteSnapListener)
        //CustomSnapHelper(dialogBinding.meridiemRv, meridiemSnapListener)

        dialogBinding.btnSubmit.setOnClickListener {
            dateTimeSelectedListener.onDateTimeSelected(selectedDateTime)
            dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun initDates(scrollSpeed: Float) {
        resetDate(dialogBinding.dateRv, scrollSpeed)
        resetHour(dialogBinding.hourRv, scrollSpeed)
        //resetMeridiem(dialogBinding.meridiemRv, scrollSpeed)
        resetMinute(dialogBinding.minuteRv, scrollSpeed)
    }


    private fun isStoppedScrolling(): Boolean {
        return dialogBinding.dateRv.scrollState == RecyclerView.SCROLL_STATE_IDLE &&
                dialogBinding.hourRv.scrollState == RecyclerView.SCROLL_STATE_IDLE &&
                dialogBinding.minuteRv.scrollState == RecyclerView.SCROLL_STATE_IDLE
    }

    private fun validateDateTime() {
        if (isStoppedScrolling()) {
            if (isValidDate()) {
                Log.e("checkValid", "1 ${isValidDate()}")
                setSelectedDateTime()
            } else {
                Log.e("checkValid", "2 ${isValidDate()}")
                initDates(SLOW_SPEED)
            }
        }
    }

    private fun setSelectedDateTime() {
        selectedDateTime.timeInMillis = selectedDateUnvalidated.timeInMillis
    }

    private fun setSelectedDate(dayOfYear: Int, year: Int) {
        selectedDateUnvalidated.set(Calendar.DAY_OF_YEAR, dayOfYear)
        selectedDateUnvalidated.set(Calendar.YEAR, year)
    }

    private fun setSelectedHour(hour: Int) {
        selectedDateUnvalidated.set(Calendar.HOUR_OF_DAY, hour)
    }

    private fun setSelectedMinute(minute: Int) {
        selectedDateUnvalidated.set(Calendar.MINUTE, minute)
    }

    /*private fun setSelectedAmPm(ampm: Int) {
        if (startDate.get(Calendar.AM_PM) == ampm) {
            selectedDateUnvalidated.set(Calendar.AM_PM, Calendar.AM)
        } else {
            selectedDateUnvalidated.set(Calendar.AM_PM, Calendar.PM)
        }
    }*/

    private fun getInitDateIndex(): Int {
        return startDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1
    }

    private fun getInitHourIndex(): Int {
        /*return if (startDate.get(Calendar.HOUR_OF_DAY) > 12) {
            (startDate.get(Calendar.HOUR_OF_DAY) - 12) + 1
        } else {
            startDate.get(Calendar.HOUR_OF_DAY) + 1
        }*/
        return startDate.get(Calendar.HOUR_OF_DAY) + 2
    }

    private fun getInitMinuteIndex(): Int {
        return startDate.get(Calendar.MINUTE) + 2
    }

    /*private fun getInitMeridiemIndex(): Int {
        return startDate.get(Calendar.AM_PM) + 1
    }*/


    private fun resetDate(rv: RecyclerView, scrollSpeed: Float) {
        val index = getInitDateIndex()
        smoothScrollToTop(rv, index, scrollSpeed)
    }

    private fun resetHour(rv: RecyclerView, scrollSpeed: Float) {
        val index = getInitHourIndex()
        Log.e("checkIndexH", "reset $index")
        smoothScrollToTop(rv, index, scrollSpeed)
    }

    private fun resetMinute(rv: RecyclerView, scrollSpeed: Float) {
        val index = getInitMinuteIndex()
        smoothScrollToTop(rv, index, scrollSpeed)
    }

    /*private fun resetMeridiem(rv: RecyclerView, scrollSpeed: Float) {
        val index = getInitMeridiemIndex()
        smoothScrollToTop(rv, index, scrollSpeed)
    }*/

    private fun setMinimumHour(rv: RecyclerView) {
        smoothScrollToTop(rv, 1, SLOW_SPEED)
    }

    private fun setMinimumMinutes(rv: RecyclerView) {
        smoothScrollToTop(rv, 1, SLOW_SPEED)
    }

    private fun isValidDate(): Boolean {
        Log.e( "initValid","${startDate.time} START_DATE")
        Log.e( "initValid","${selectedDateUnvalidated.time} SELECTED_DATE_BEFORE_VALIDATION ")
        return selectedDateUnvalidated.timeInMillis >= startDate.timeInMillis
    }

    /*-----*/

    fun setTitleTextColor(color: Int) {
        titleTextColor = color
    }

    fun setTitleView(visibility: Int) {
        titleVisible = visibility
    }

    fun setDividerBgColor(color: Int) {
        centerViewBgColor = color
    }

    fun setSubmitBtnColor(color: Int) {
        submitBgColor = color
    }

    fun setSubmitBtnTextColor(color: Int) {
        submitTextColor = color
    }

    fun setCancelBtnColor(color: Int) {
        cancelBgColor = color
    }

    fun setCancelBtnTextColor(color: Int) {
        cancelTextColor = color
    }

    fun setSubmitBtnText(submitTxt: String) {
        submitText = submitTxt
    }

    fun setCancelBtnText(cancelTxt: String) {
        cancelText = cancelTxt
    }

    fun setFontSize(sizeInSp: Int) {
        fontSize = sizeInSp
    }

    fun setCenterDividerHeight(sizeInDp: Int) {
        dividerHeight = sizeInDp
    }

}