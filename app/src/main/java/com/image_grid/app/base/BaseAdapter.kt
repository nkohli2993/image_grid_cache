package com.image_grid.app.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<DB : ViewDataBinding> : RecyclerView.Adapter<BaseViewHolder>() {
    open lateinit var binding: DB

    private var itemClickListener: OnItemClick? = null

    @LayoutRes
    abstract fun getLayoutRes(): Int


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutRes(),
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    }


    interface OnItemClick {
        fun onItemClick(vararg items: Any)
    }

    fun onItemClick(vararg items: Any) {
        itemClickListener?.onItemClick(*items)
    }

    fun setOnItemClickListener(onItemClick: OnItemClick) {
        itemClickListener = onItemClick
    }

}