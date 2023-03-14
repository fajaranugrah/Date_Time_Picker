package com.example.datetimepicker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.fajaranugrah.dateandtimepickerios.DateAndTimePickerIOS
import com.android.fajaranugrah.dateandtimepickerios.dialog.SingleDateAndTimePickerIOSDialog
import com.example.datetimepicker.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showDateAndTimePicker.setOnClickListener {
            val minDate: Calendar = Calendar.getInstance()
            SingleDateAndTimePickerIOSDialog.Builder(
                this@MainActivity
            ) .bottomSheet()
                .curved()
                .minDateRange(minDate.time)
                .minutesStep(1)
                //.displayHours(false)
                //.displayMinutes(false)
                //.todayText("aujourd'hui")
                .displayListener(object :
                    SingleDateAndTimePickerIOSDialog.DisplayListener {
                    override fun onDisplayed(picker: DateAndTimePickerIOS?) {
                        // Retrieve the DateAndTimePickerIOS
                    }

                    override fun onClosed(picker: DateAndTimePickerIOS?) {
                        // On dialog closed
                    }
                })
                .listener(object : SingleDateAndTimePickerIOSDialog.Listener {
                    override fun onDateSelected(date: Date?) {
                        Toast.makeText(
                            this@MainActivity,
                            "Selected date $date",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }).display()
        }

    }
}