package com.rolling.meadows.views.view_main.notification

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseViewHolder
import com.rolling.meadows.data.NotificationData
import com.rolling.meadows.databinding.AdapterNotificationBinding

class NotificationAdapter(
    val baseActivity: BaseActivity,
    var notification: MutableList<NotificationData> = ArrayList()
) : BaseAdapter<AdapterNotificationBinding>(),
    BaseAdapter.OnItemClick {

    override fun getLayoutRes(): Int = R.layout.adapter_notification

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterNotificationBinding
        val data = notification[position]
        binding.descriptionTV.text = data.message
        binding.descriptionTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._787F84))
        binding.card.setBackgroundColor(ContextCompat.getColor(baseActivity, R.color._808F8F8F))
        if (data.read == "UN_READ") {
            binding.descriptionTV.setTextColor(
                ContextCompat.getColor(
                    baseActivity,
                    R.color._1D1A2F
                )
            )
            binding.card.setBackgroundColor(ContextCompat.getColor(baseActivity, R.color.white))
        }
        binding.timeTV.text = data.timeAgo
        binding.root.setOnClickListener {
            onItemClick(data, position, "read")
        }
        binding.deleteIV.setOnClickListener {
            onItemClick(data, position, "delete")
        }
    }

    fun addNotification(notificationList: ArrayList<NotificationData>) {
        this.notification.addAll(notificationList)
        notifyDataSetChanged()
    }

    fun addNotificationONTop(notificationList: NotificationData) {
        this.notification.add(0, notificationList)
        notifyDataSetChanged()
    }

    fun addAll(notificationList: ArrayList<NotificationData>) {
        this.notification = notificationList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notification.size
    }

}

