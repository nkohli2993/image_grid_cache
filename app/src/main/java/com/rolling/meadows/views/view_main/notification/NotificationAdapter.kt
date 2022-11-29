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
      /*  val data = notification[position]
        binding.descriptionTV.text = data.message
        binding.timeTV.text = data.timeAgo
        binding.notificationIV.setImageResource(R.drawable.ic_read_notificatyion)
        binding.descriptionTV.setTextColor(ContextCompat.getColor(baseActivity, R.color._787F84))
        if (data.read == "UN_READ") {
            binding.notificationIV.setImageResource(R.drawable.ic_un_read_notification)
            binding.descriptionTV.setTextColor(
                ContextCompat.getColor(
                    baseActivity,
                    R.color._1D1A2F
                )
            )
        }

        binding.root.setOnClickListener {
            onItemClick(data)
        }*/
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
        return 5
    }

}

