package com.android.fajaranugrah.date_time_picker_ios.utils

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.fajaranugrah.date_time_picker_ios.R

/**
 * Created by Fajar Anugrah Ramadhan on 09-03-2023.
 * Contact email - fajarconan@gmail.com
 */
class CustomSnapHelper(
    private val rv: RecyclerView,
    private val snapListener: SnapListener
) : LinearSnapHelper() {

    init {
        this.attachToRecyclerView(rv)
    }

    private var selectedPosition = -1

    private var lastSelectedView: TextView? = null

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        val view = super.findSnapView(layoutManager)

        if (view != null) {
            val newPosition = layoutManager!!.getPosition(view)
            if (newPosition != selectedPosition && rv.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                selectedPosition = newPosition
                snapListener.onViewSnapped(newPosition)

                setSelectedFont(newPosition)
            }
        }

        return view
    }

    private fun setSelectedFont(newPosition: Int) {
        if (lastSelectedView != null) {
            lastSelectedView?.typeface = Typeface.DEFAULT
            lastSelectedView?.setTextColor(ContextCompat.getColor(rv.context, R.color.grey_chateau))
        }

        lastSelectedView = (rv.findViewHolderForAdapterPosition(newPosition) as DateViewHolder).binding.tv
        lastSelectedView?.typeface = Typeface.DEFAULT_BOLD
        lastSelectedView?.setTextColor(ContextCompat.getColor(rv.context, R.color.raisin_black))
    }

    interface SnapListener {
        fun onViewSnapped(position: Int)
    }
}