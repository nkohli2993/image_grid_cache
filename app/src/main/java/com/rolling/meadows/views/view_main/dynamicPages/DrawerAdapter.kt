package com.rolling.meadows.views.view_main.dynamicPages
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterDrawerBinding
import com.rolling.meadows.views.view_main.DrawerData
import java.util.*


class DrawerAdapter(var drawerList: ArrayList<DrawerData>) : BaseAdapter<AdapterDrawerBinding>() {

    override fun getLayoutRes(): Int = R.layout.adapter_drawer

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterDrawerBinding
        val data = drawerList[position]
        binding.drawerIV.setImageResource(data.icon)
        binding.titleTV.text = data.name
        binding.root.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return drawerList.size
    }


}
