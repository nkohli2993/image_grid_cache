package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
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
        val data = eventList[position]
        binding.dateTV.text = data.name
        binding.dateTV.typeface = ResourcesCompat.getFont(baseActivity, R.font.poppins_semibold)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.black))
        background.clear()
        when {
            data.name.lowercase().contains("announcements") -> {
                background.addAll(baseActivity.announcementBackgroundList())
            }
            data.name.lowercase().contains("event") -> {
                background.addAll(baseActivity.eventBackgroundList())
            }
            else -> {
                background.addAll(baseActivity.menuBackgroundList())
            }
        }
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
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return eventList.size
    }

    private fun setBackgroundList(list: TypedArray) {
        for (i in 0 until 4) {
            background.add(list.getResourceId(i, -1))
        }
        list.recycle()
    }

}

