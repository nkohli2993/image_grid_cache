package com.rolling.meadows.views.view_main.homePages.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.databinding.AdapterRollingEventBinding

class DateWiseEvents(
    val baseActivity: BaseActivity, val background: ArrayList<Int>
) : BaseAdapter<AdapterRollingEventBinding>(),
    BaseAdapter.OnItemClick {
    lateinit var context: Context
    override fun getLayoutRes(): Int = R.layout.adapter_rolling_event
    private var i = 0
    private fun getScreenWidth(): Float {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x - 110.0f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.adapter_rolling_event,
            parent,
            false
        )
        binding.root.layoutParams.width = (getScreenWidth() / 1.2).toInt()
        return BaseViewHolder(binding)
    }


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterRollingEventBinding
        binding.viewCL.setBackgroundResource(background[i])
        i++
        if (i > 4) {
            i = 0
        }

    }

    override fun getItemCount(): Int {
        return 6
    }

}

