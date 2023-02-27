package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.events.EventData
import com.rolling.meadows.databinding.AdapterRollingEventDateWiseBinding
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.DateFunctions
import com.rolling.meadows.utils.extensions.visibleView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventDateWiseAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<EventData>,
    var type: Int
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
        binding.dateTV.typeface = ResourcesCompat.getFont(baseActivity, R.font.poppins_medium)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._B7B7B7))
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

        val adapter =
            CategoryWiseAdapter(baseActivity, background, data.data?.listData!!, type)
        binding.rollingRV.adapter = adapter
        binding.rollingRV.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int {
        return eventList.size
    }

}

