package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.events.EventData
import com.rolling.meadows.databinding.AdapterRollingEventDateWiseBinding
import com.rolling.meadows.utils.DateFunctions
import com.rolling.meadows.utils.extensions.visibleView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventDateWiseAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<EventData>
) : BaseAdapter<AdapterRollingEventDateWiseBinding>(),
    BaseAdapter.OnItemClick {
    init {
        setHasStableIds(true)
    }

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event_date_wise

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventDateWiseBinding
        binding.dateTV.visibleView(true)
        val data = eventList[position]
        val selectedDate = SimpleDateFormat("yyyy-MM-dd").parse(data.date)
        val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        if (selectedDate!! == SimpleDateFormat("yyyy-MM-dd").parse(currentDate)) {
            binding.dateTV.text = "Today"
        } else {
            binding.dateTV.text = DateFunctions.getFormattedDate(
                "yyyy-MM-dd",
                "dd MMM, yyyy", data.date
            )
        }
        val adapter = DateWiseEvents(baseActivity, background, data.data)
        binding.rollingRV.adapter = adapter
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int {
        return eventList.size
    }

}

