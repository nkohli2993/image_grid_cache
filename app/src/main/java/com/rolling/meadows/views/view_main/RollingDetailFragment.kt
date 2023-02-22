package com.rolling.meadows.views.view_main

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.data.events.EventDetailData
import com.rolling.meadows.databinding.FragmentRollingDetailBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.utils.DateFunctions
import com.rolling.meadows.view_model.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RollingDetailFragment : BaseFragment<FragmentRollingDetailBinding>() {

    override fun getLayoutRes() = R.layout.fragment_rolling_detail
    private val viewModel: EventsViewModel by viewModels()

    override fun observeViewModel() {
        viewModel.eventDetailResponseLiveData.observe(this) { resultEvent ->
            resultEvent.getContentIfNotHandled()?.let {
                when (it) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        val data = it.data?.data!!
                        binding.dateTV.text = data.date
                        binding.titleTV.text = data.eventType
                        binding.descriptionTV.text = data.description
                        binding.typeTV.text = data.event_category_name
                        binding.dateTV.text = DateFunctions.getFormattedDate(
                            "yyyy-MM-dd",
                            "dd MMM, yyyy", data.date
                        )
                     /*   binding.timeTV.text = DateFunctions.getFormattedDate(
                            "yyyy-MM-dd hh:mm:ss",
                            "hh:mm a", data.date.plus(" ${data.time}")
                        )*/
                    }
                    is DataResult.Failure -> {
                        handleFailure(it.message, it.exception, it.errorCode)
                    }
                    else -> {}
                }
            }

        }

    }

    override fun initViewBinding() {
        binding.listener = this
        baseActivity!!.updateStatusBarColor(R.drawable.detail_bd_new, true, R.color._24A872)
        setdata()
    }

    private fun setdata() {
        if (arguments != null && requireArguments().containsKey("event_id")) {
            viewModel.eventId.value = arguments?.getInt("event_id")!!
            viewModel.hitEventDetailApi()
        } else if (arguments != null && requireArguments().containsKey("detail")) {
            val data = arguments?.getParcelable<EventDetailData>("detail")!!
            binding.dateTV.text = data.date
            binding.titleTV.text = data.eventType
            binding.typeTV.text = data.event_category_name
            binding.descriptionTV.text = data.description
            binding.dateTV.text = DateFunctions.getFormattedDate(
                "yyyy-MM-dd",
                "dd MMM, yyyy", data.date
            )
            binding.timeTV.text = DateFunctions.getFormattedDate(
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm a", data.date.plus(" ${data.time}")
            )
        }


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