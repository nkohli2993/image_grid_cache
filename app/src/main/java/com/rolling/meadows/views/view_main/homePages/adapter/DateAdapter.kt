package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.DateData
import com.rolling.meadows.databinding.AdapterDateBinding
import com.rolling.meadows.utils.Constants

class DateAdapter(
    val baseActivity: BaseActivity,
    val dateList: ArrayList<DateData>,
    val type: Int
) : BaseAdapter<AdapterDateBinding>(),
    BaseAdapter.OnItemClick {
    override fun getLayoutRes(): Int = R.layout.adapter_date

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterDateBinding
        val data = dateList[position]
        binding.dateTV.text = data.date
        binding.dayTV.text = data.day
        binding.dateTV.setBackgroundResource(R.drawable.background_transparent)
        binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._B7B7B7))
        binding.lineIV.visibility = View.INVISIBLE
        if (data.isSelected) {
            binding.lineIV.visibility = View.VISIBLE
            binding.dateTV.setBackgroundResource(R.drawable.background_date_green)
            binding.dateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.white))
        }

        binding.root.setOnClickListener {
          /*  when (type) {
                Constants.EVENT_FILTER_TYPE.DAY.value -> {
                    dateList.forEachIndexed { index, video ->
                        video.takeIf { it.isSelected }?.let {
                            dateList[index] = it.copy(isSelected = false)
                        }
                    }
                    data.isSelected = true
                    notifyDataSetChanged()
                }
            }*/
            onItemClick(position, "date", data.date)
        }
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

}

