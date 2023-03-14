package com.android.fajaranugrah.date_time_picker_ios.utils

import androidx.annotation.Keep
import java.util.*

/**
 * Created by Fajar Anugrah Ramadhan on 09-03-2023.
 * Contact email - fajarconan@gmail.com
 */
@Keep
interface OnDateTimeSelectedListener {
    fun onDateTimeSelected(selectedDateTime: Calendar)
}