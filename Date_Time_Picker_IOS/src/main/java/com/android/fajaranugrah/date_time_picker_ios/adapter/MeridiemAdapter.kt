package com.android.fajaranugrah.date_time_picker_ios.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.fajaranugrah.date_time_picker_ios.databinding.RecyclerDateSelectionBinding
import com.android.fajaranugrah.date_time_picker_ios.utils.DateViewHolder
import com.android.fajaranugrah.date_time_picker_ios.utils.dpToPx
import com.android.fajaranugrah.date_time_picker_ios.utils.spToPx

/**
 * Created by Fajar Anugrah Ramadhan on 09-03-2023.
 * Contact email - fajarconan@gmail.com
 */
class MeridiemAdapter(
    val meridiem: ArrayList<String>,
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

        if (meridiem[position].isNotEmpty())
            viewHolder.binding.tv.text = meridiem[position]
        else
            viewHolder.binding.tv.text = ""
    }


    override fun getItemCount() = meridiem.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}