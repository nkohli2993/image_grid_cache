package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterRollingEventDateWiseBinding
import com.rolling.meadows.utils.extensions.visibleView

class EventDateWiseAdapter  (
    val baseActivity: BaseActivity
) : BaseAdapter<AdapterRollingEventDateWiseBinding>(),
    BaseAdapter.OnItemClick {

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event_date_wise

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventDateWiseBinding
        binding.dateTV.visibleView(true)
       /* if(position == 0){
            binding.dateTV.visibleView(false)
        }*/
        val adapter = EventsAdapter(baseActivity)
        binding.rollingRV.adapter = adapter
    }

    override fun getItemCount(): Int {
        return 5
    }

}

