package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.util.Log
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.events.EventDetailData
import com.rolling.meadows.databinding.AdapterRollingEventDayBinding
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.DateFunctions.getFormattedDate

class EventsAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<EventDetailData>
) : BaseAdapter<AdapterRollingEventDayBinding>(),
    BaseAdapter.OnItemClick {
    private var i: Int = 0

    init {
        setHasStableIds(true)
    }

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event_day

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventDayBinding
        if (eventList.size > 0) {
            val data = eventList[position]
            binding.titleTV.text = data.eventType
            binding.descriptionTV.text = data.description
           /* binding.timeTV.text = getFormattedDate(
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm a", data.date.plus(" ${data.time}")
            )*/
            when (data.event_category_id) {
                Constants.EVENT_FILTER.EVENTS.value -> {
                    binding.imageIV.setImageResource(R.drawable.ic_event_icon)
                }
                Constants.EVENT_FILTER.ANNOUNCEMENTS.value -> {
                    binding.imageIV.setImageResource(R.drawable.ic_announcement_icon)
                }
                else -> {
                    binding.imageIV.setImageResource(R.drawable.ic_menu_icon)
                }
            }
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
        Log.e("catch_exception", "size: ${eventList.size}")
        return eventList.size
    }

}

