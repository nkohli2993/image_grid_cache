package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.events.EventData
import com.rolling.meadows.data.events.EventDetailData
import com.rolling.meadows.databinding.AdapterRollingEventBinding
import com.rolling.meadows.utils.DateFunctions.convertDateFormatFromUTC

class EventsAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<EventData>
) : BaseAdapter<AdapterRollingEventBinding>(),
    BaseAdapter.OnItemClick {
    private var i: Int = 0
    init {
        setHasStableIds(true)
    }

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventBinding
        if(eventList.size>0){
            val data = eventList[position].data[0]
            binding.titleTV.text  = data.eventType
            binding.descriptionTV.text = data.description
            binding.timeTV.text = convertDateFormatFromUTC(
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm a",data.date.plus(" ${data.time}")
            )
        }

        binding.viewCL.setBackgroundResource(background[i])
        i++
        if (i > 4) {
            i = 0
        }

        binding.root.setOnClickListener { onItemClick(position, "event") }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return eventList.size
    }

}

