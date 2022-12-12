package com.rolling.meadows.views.view_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.data.events.EventDetailData
import com.rolling.meadows.databinding.FragmentHomeBinding
import com.rolling.meadows.databinding.FragmentRollingDetailBinding
import com.rolling.meadows.utils.DateFunctions
import java.util.*


class RollingDetailFragment : BaseFragment<FragmentRollingDetailBinding>() {

    override fun getLayoutRes() = R.layout.fragment_rolling_detail
    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding.listener = this
        baseActivity!!.updateStatusBarColor(R.drawable.detail_bd_new, true, R.color._24A872)
        setdata()
    }

    private fun setdata() {
        val data = arguments?.getParcelable<EventDetailData>("detail")!!
        binding.dateTV.text = data.date
        binding.titleTV.text = data.eventType
        binding.descriptionTV.text = data.description
        binding.dateTV.text = DateFunctions.getFormattedDate(
            "yyyy-MM-dd",
            "dd MMM, yyyy", data.date
        )
        binding.timeTV.text = DateFunctions.convertDateFormatFromUTC(
            "yyyy-MM-dd hh:mm:ss",
            "hh:mm a", data.date.plus(" ${data.time}")
        )

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV -> {
                findNavController().popBackStack()
            }
        }
    }


}