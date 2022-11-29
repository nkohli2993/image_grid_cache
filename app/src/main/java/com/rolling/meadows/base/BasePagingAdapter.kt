package com.rolling.meadows.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<DB : ViewDataBinding> :
    RecyclerView.Adapter<BaseViewHolder>() {
    open lateinit var binding: DB
    private var onPageEndListener: OnPageEndListener? = null


    @LayoutRes
    abstract fun getLayoutRes(): Int


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutRes(),
            parent,
            false
        )
        return BaseViewHolder(binding)

    }

    private var itemClickListener: OnItemClick? = null

    interface OnItemClick {
        fun onItemClick(vararg items: Any)
    }

    fun onItemClick(vararg items: Any) {
        itemClickListener?.onItemClick(*items)
    }

    fun setOnItemClickListener(onItemClick: OnItemClick) {
        itemClickListener = onItemClick
    }
    fun setOnPageEndListener(onPageEndListener: OnPageEndListener) {
        this.onPageEndListener = onPageEndListener
    }
    fun onPageEnd(vararg itemData: Any) {
        if (onPageEndListener != null) {
            onPageEndListener!!.onPageEnd(*itemData)
        }
    }
    interface OnPageEndListener {
        fun onPageEnd(vararg itemData: Any)
    }

}
