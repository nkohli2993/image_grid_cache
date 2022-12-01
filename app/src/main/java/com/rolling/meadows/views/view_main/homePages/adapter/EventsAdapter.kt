package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.util.Log
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterRollingEventBinding

class EventsAdapter (
    val baseActivity: BaseActivity) : BaseAdapter<AdapterRollingEventBinding>(),
    BaseAdapter.OnItemClick {

    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventBinding

    }

    override fun getItemCount(): Int {
        return 5
    }

}

