package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
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
import com.rolling.meadows.utils.DateFunctions
import com.rolling.meadows.utils.extensions.visibleView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CategoryWiseAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>,
    var eventList: ArrayList<ListEventData>,
    var type: Int
) : BaseAdapter<AdapterRollingEventDateWiseBinding>(),
    BaseAdapter.OnItemClick {
    lateinit var context: Context

    init {
        setHasStableIds(true)
    }

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event_date_wise

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventDateWiseBinding
        binding.dateTV.visibleView(true)
        val data = eventList[position]
        binding.dateTV.typeface = ResourcesCompat.getFont(baseActivity, R.font.poppins_semibold)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.black))
        binding.dateTV.text = data.name
        val adapter = DateWiseEvents(baseActivity, background, data.childList!!, 0)
        binding.rollingRV.adapter = adapter
        binding.rollingRV.layoutManager =
            LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.dateTV.visibleView(false)
        when (type) {
            Constants.EVENT_FILTER.ALL.value -> {
                binding.dateTV.visibleView(true)

            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int {
        return eventList.size
    }

}

