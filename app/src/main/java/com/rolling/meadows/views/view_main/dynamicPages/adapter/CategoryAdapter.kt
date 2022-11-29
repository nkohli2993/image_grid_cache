package com.rolling.meadows.views.view_main.dynamicPages.adapter

import android.annotation.SuppressLint
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterCategoryBinding

class CategoryAdapter(val array: ArrayList<String>, val baseActivity: BaseActivity) :
    BaseAdapter<AdapterCategoryBinding>(), BaseAdapter.OnItemClick {

    override fun getLayoutRes(): Int = R.layout.adapter_category

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCategoryBinding
        binding.nameTV.text = array[position]
        binding.root.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return array.size
    }

}

