package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.events.ListEventData
import com.rolling.meadows.databinding.AdapterRollingEventDateWiseBinding
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.extensions.visibleView


class EventsAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<ListEventData>,
    var type: Int
) : BaseAdapter<AdapterRollingEventDateWiseBinding>(),
    BaseAdapter.OnItemClick {
    private var i: Int = 0

    init {
        setHasStableIds(true)
    }

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event_date_wise

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventDateWiseBinding
        /*   if (eventList.size > 0) {
               val data = eventList[position]
               binding.titleTV.text = data.eventType
               binding.descriptionTV.text = data.description
               *//* binding.timeTV.text = getFormattedDate(
                 "yyyy-MM-dd hh:mm:ss",
                 "hh:mm a", data.date.plus(" ${data.time}")
             )*//*
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
*/

        val data = eventList[position]
        binding.dateTV.text = data.name
        binding.dateTV.typeface = ResourcesCompat.getFont(baseActivity, R.font.poppins_semibold)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.black))
        val adapter = DateWiseEvents(baseActivity, background, data.childList!!,type)
        binding.rollingRV.adapter = adapter
        binding.dateTV.visibleView(false)
        binding.rollingRV.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
        when (type) {
            Constants.EVENT_FILTER.ALL.value -> {
                binding.dateTV.visibleView(true)
                binding.rollingRV.layoutManager =
                    LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        // binding.root.setOnClickListener { onItemClick(position, "event") }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return eventList.size
    }

}

