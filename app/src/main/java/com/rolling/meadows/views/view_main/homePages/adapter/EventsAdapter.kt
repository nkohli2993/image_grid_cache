package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.util.Log
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterRollingEventBinding

class EventsAdapter(
    val baseActivity: BaseActivity,
    val background: ArrayList<Int>
) : BaseAdapter<AdapterRollingEventBinding>(),
    BaseAdapter.OnItemClick {
    private var i: Int = 0
    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventBinding
        binding.viewCL.setBackgroundResource(background[i])
        i++
        if (i  > 4) {
            i = 0
        }

        binding.root.setOnClickListener { onItemClick(position,"event") }
    }

    override fun getItemCount(): Int {
        return 6
    }

}

