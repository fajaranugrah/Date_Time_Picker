package com.android.fajaranugrah.date_time_picker_ios.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.fajaranugrah.date_time_picker_ios.databinding.RecyclerDateSelectionBinding
import com.android.fajaranugrah.date_time_picker_ios.utils.*


/**
 * Created by Fajar Anugrah Ramadhan on 09-03-2023.
 * Contact email - fajarconan@gmail.com
 */
class HourAdapter(
    val hour: ArrayList<Int>,
    private val fontSize: Int,
    private val dividerHeight: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = DateViewHolder(
            RecyclerDateSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        view.binding.parent.layoutParams.height =
            dividerHeight.dpToPx(view.binding.tv.context).toInt()
        view.binding.tv.textSize = fontSize.spToPx(view.binding.tv.context)
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as DateViewHolder

        if (hour[position] != -1) {
            viewHolder.binding.tv.text = getZeroPrefix(hour[position])
        } else {
            viewHolder.binding.tv.text = ""
        }
    }


    override fun getItemCount() = hour.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}