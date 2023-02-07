package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.CategoryData
import com.rolling.meadows.databinding.AdapterFilterBinding

class FilterAdapter(
    val baseActivity: BaseActivity,
    var categoryData: ArrayList<CategoryData>
) : BaseAdapter<AdapterFilterBinding>(),
    BaseAdapter.OnItemClick {
    override fun getLayoutRes(): Int = R.layout.adapter_filter

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterFilterBinding
        val data = categoryData[position]
        binding.nameTV.text = data.name
        binding.root.setOnClickListener { onItemClick(data, "filter") }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int {
        return categoryData.size
    }

}

