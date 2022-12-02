package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.DateData
import com.rolling.meadows.databinding.AdapterDateBinding

class DateAdapter (
    val baseActivity: BaseActivity, val dateList:ArrayList<DateData>, var position:Int) : BaseAdapter<AdapterDateBinding>(),
    BaseAdapter.OnItemClick {
    var selectedPosition = position
    override fun getLayoutRes(): Int = R.layout.adapter_date

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterDateBinding
        binding.dateTV.text = dateList[position].date
        binding.dayTV.text = dateList[position].day
        binding.dateTV.setBackgroundResource(R.drawable.background_transparent)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._B7B7B7))
        binding.lineIV.visibility = View.INVISIBLE
        if(selectedPosition == position){
            binding.lineIV.visibility = View.VISIBLE
            binding.dateTV.setBackgroundResource(R.drawable.background_date_green)
            binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.white))
        }

        binding.root.setOnClickListener {
            selectedPosition = position
            onItemClick(position,"date")
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

}

