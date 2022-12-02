package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterMonthBinding
import com.rolling.meadows.databinding.AdapterNotificationBinding
import kotlin.collections.ArrayList

class MonthAdapter(
    val baseActivity: BaseActivity, val monthList: ArrayList<String>, var position: Int
) : BaseAdapter<AdapterMonthBinding>(),
    BaseAdapter.OnItemClick {
    lateinit var context: Context
    var selectedPosition = position
    override fun getLayoutRes(): Int = R.layout.adapter_month

    private fun getScreenWidth(): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x - 110
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.adapter_month,
            parent,
            false
        )
        binding.root.layoutParams.width = (getScreenWidth() / 3)
        return BaseViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterMonthBinding
        binding.monthTV.text = monthList[position]
        binding.root.setBackgroundResource(R.drawable.background_transparent)
        binding.monthTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.white))
        if (selectedPosition == position) {
            binding.root.setBackgroundResource(R.drawable.background_white_round)
            binding.monthTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._083000))
        }

        binding.root.setOnClickListener {
            selectedPosition = position
            onItemClick(position,"month")
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return monthList.size
    }

}

