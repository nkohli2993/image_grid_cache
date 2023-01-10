package com.rolling.meadows.views.view_main.notification

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.data.NotificationData
import com.rolling.meadows.data.events.EventDetailData
import com.rolling.meadows.databinding.FragmentNotificationBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.observeEvent
import com.rolling.meadows.utils.extensions.showError
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.view_model.NotificationViewModel
import com.rolling.meadows.views.ViewTypeOpenViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(),
    BaseAdapter.OnItemClick {
    override fun getLayoutRes() = R.layout.fragment_notification
    private var selectedPosition: Int? = null
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private var total: Int = 0
    private var pageNumber: Int = 1
    private val viewModel: NotificationViewModel by viewModels()
    private var adapter: NotificationAdapter? = null
    private var notificationList: ArrayList<NotificationData> = arrayListOf()
    private val viewModelClick: ViewTypeOpenViewModel by viewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        isBinded = false
    }

    override fun onResume() {
        super.onResume()
        isBinded = true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observeViewModel() {
        viewModel.notificationResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    showError(baseActivity!!, result.message.toString())
                }
                is DataResult.Loading -> {
                    showLoading("")
                }
                is DataResult.Success -> {
                    hideLoading()
                    if (viewModel.page.value == 1) {
                        adapter = null
                        notificationList.clear()
                        setAdapter()
                    }
                    var list: ArrayList<NotificationData> = arrayListOf()
                    result.data?.data?.let {
                        list = it.list
                        total = it.total_pages!!
                        currentPage = it.current_page!!
                        totalPage = it.total_pages!!
                    }
                    notificationList.addAll(list)
                    adapter!!.notifyDataSetChanged()
                    binding.nofoundTV.visibleView(false)
                    if (notificationList.size <= 0) {
                        binding.nofoundTV.visibleView(true)
                        viewModelClick.selectItem(0)
                    }
                    if (notificationList.size > 0) {
                        binding.nofoundTV.visibleView(false)
                        binding.notificationRV.visibleView(true)
                    } else {
                        binding.nofoundTV.visibleView(true)
                        binding.notificationRV.visibleView(false)
                    }
                }
                else -> {}
            }
        }
        viewModel.notificationReadResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    showError(baseActivity!!, result.message.toString())
                }
                is DataResult.Loading -> {
                    showLoading("")
                }
                is DataResult.Success -> {
                    hideLoading()
                    binding.nofoundTV.visibleView(false)
                    if (notificationList.size <= 0) {
                        binding.nofoundTV.visibleView(true)
                        viewModelClick.selectItem(0)
                    }
                    val data = result.data?.data
                    val eventData = EventDetailData(
                        data!!.date,
                        data.description,
                        data.eventType,
                        data.id,
                        data.time
                    )
                    val bundle = Bundle()
                    bundle.putParcelable("detail", eventData)
                    findNavController().navigate(R.id.notification_to_rolling_detail, bundle)
                }
                else -> {}
            }
        }
        viewModel.notificationDeleteResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    showError(baseActivity!!, result.message.toString())
                }
                is DataResult.Loading -> {
                    showLoading("")
                }
                is DataResult.Success -> {
                    hideLoading()
                    notificationList.removeAt(selectedPosition!!)
                    adapter!!.notifyItemRangeRemoved(selectedPosition!!, notificationList.size)
                    adapter!!.notifyDataSetChanged()
                    binding.nofoundTV.visibleView(false)
                    if (notificationList.size <= 0) {
                        binding.nofoundTV.visibleView(true)
                    }
                }
                else -> {}
            }
        }

    }

    override fun onItemClick(vararg items: Any) {
        selectedPosition = items[1] as Int
        val data = items[0] as NotificationData
        if (items[2] as String == "delete") {
            viewModel.deleteNotification(data.id)
        } else {
            if (data.read == "UN_READ") {
                viewModel.notificationRead(data.id)
            } else {
                val bundle = Bundle()
                bundle.putInt("event_id", data.eventId)
                findNavController().navigate(R.id.notification_to_rolling_detail, bundle)
            }
        }


    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

    override fun initViewBinding() {
        binding.listener = this
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))
        changeStatusBarIconColor(true)
        viewModel.page.value = 1
        viewModel.limit.value = 20
        viewModel.hitNotificationApi()
        setAdapter()
    }

    private fun handleNotificationPagination() {
        var isScrolling: Boolean
        var visibleItemCount: Int
        var totalItemCount: Int
        var pastVisiblesItems: Int


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val view = binding.scrollView.getChildAt(binding.scrollView.childCount - 1) as View
            val diff: Int = view.bottom - (binding.scrollView.height + binding.scrollView
                .scrollY)
            if (diff == 0) {
                isScrolling = true
                visibleItemCount =
                    binding.notificationRV.layoutManager!!.childCount
                totalItemCount =
                    binding.notificationRV.layoutManager!!.itemCount
                pastVisiblesItems =
                    (binding.notificationRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (isScrolling && visibleItemCount + pastVisiblesItems >= totalItemCount && (currentPage < totalPage)) {
                    isScrolling = false
                    currentPage++
                    pageNumber++
                    viewModel.page.value = pageNumber
                    viewModel.hitNotificationApi()
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = NotificationAdapter(baseActivity!!, notificationList)
        binding.notificationRV.adapter = adapter
        adapter!!.setOnItemClickListener(this)
        handleNotificationPagination()
    }


}