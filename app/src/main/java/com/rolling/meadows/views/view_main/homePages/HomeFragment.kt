package com.rolling.meadows.views.view_main.homePages

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rolling.meadows.BuildConfig
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.data.DateData
import com.rolling.meadows.data.MonthCalendarData
import com.rolling.meadows.data.events.EventData
import com.rolling.meadows.databinding.FragmentHomeBinding
import com.rolling.meadows.network.retrofit.DataResult
import com.rolling.meadows.network.retrofit.observeEvent
import com.rolling.meadows.utils.Constants
import com.rolling.meadows.utils.DateFunctions
import com.rolling.meadows.utils.extensions.showException
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.view_model.EventsViewModel
import com.rolling.meadows.views.view_main.homePages.adapter.DateAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.EventDateWiseAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.EventsAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.MonthAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
@SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    BaseAdapter.OnItemClick {
    override fun getLayoutRes() = R.layout.fragment_home
    private var dateList: ArrayList<DateData> = arrayListOf()
    private var dateAdapter: DateAdapter? = null
    private var total: Int? = null
    private var dayEventList: ArrayList<EventData> = arrayListOf()
    private var eventList: ArrayList<EventData> = arrayListOf()
    private var dialog: Dialog? = null
    private var multiDayAdapter: EventDateWiseAdapter? = null
    private var dayEventAdapter: EventsAdapter? = null
    private var background: ArrayList<Int> = arrayListOf()
    private val lastDayInCalendar = Calendar.getInstance()
    private val viewModel: EventsViewModel by viewModels()
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private var pageNumber: Int = 1
    private var monthSelected = -1
    private var dateelected = -1
    private var startDate = ""
    private var endDate = ""
    private var year = ""
    private var filterType = Constants.EVENT_FILTER_TYPE.DAY.value
    private var monthList: ArrayList<MonthCalendarData> = arrayListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isBinded = false
    }

    override fun onResume() {
        super.onResume()
        isBinded = true
    }

    override fun initViewBinding() {
        binding.listener = this
        baseActivity!!.updateStatusBarColor(R.color.transparent, true, R.color._24A872)
        binding.titleTV.text = viewModel.getSavedUser()?.fullName
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))
        changeStatusBarIconColor(true)
        background.clear()
        background.add(R.drawable.ic_background_pink)
        background.add(R.drawable.ic_background_yellow)
        background.add(R.drawable.ic_background_blue)
        background.add(R.drawable.ic_background_green)
        background.add(R.drawable.ic_background_pruple)
        startDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        endDate = startDate
        year = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
        binding.yearTV.text = year
        callEventApi()
        setMonthAdapter()
        setDateAdapter()
        setEventAdapter()
        setEventDateAdapter()
        lastDayInCalendar.add(Calendar.MONTH, 3)
    }

    override fun observeViewModel() {
        viewModel.logoutResponseLiveData.observe(this) { resultEvent ->
            resultEvent.getContentIfNotHandled()?.let {
                when (it) {
                    is DataResult.Loading -> {
                        showLoading()
                    }
                    is DataResult.Success -> {
                        hideLoading()
                        viewModel.saveToken("")
                        viewModel.clearAllData()
                        baseActivity!!.goToInitialActivity()
                    }
                    is DataResult.Failure -> {
                        handleFailure(it.message, it.exception, it.errorCode)
                    }
                    else -> {}
                }
            }

        }
        viewModel.eventResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    hideLoading()
                    handleFailure(result.message, result.exception, result.errorCode)
                }
                is DataResult.Loading -> {
                    showLoading("")
                }
                is DataResult.Success -> {
                    hideLoading()
                    var list: ArrayList<EventData> = arrayListOf()
                    result.data?.data?.let {
                        list = it.list
                        total = it.totalPages
                        currentPage = it.currentPage
                        totalPage = it.totalPages
                    }


                    when (filterType) {
                        Constants.EVENT_FILTER_TYPE.DAY.value -> {
                            if (viewModel.page.value == 1) {
                                dayEventAdapter = null
                                dayEventList.clear()
                                setEventAdapter()
                            }
                            dayEventList.addAll(list)
                            if (dayEventAdapter == null) {
                                setEventAdapter()
                            } else {
                                dayEventAdapter!!.notifyDataSetChanged()
                            }

                            setList(dayEventList)
                        }
                        else -> {
                            if (viewModel.page.value == 1) {
                                multiDayAdapter = null
                                eventList.clear()
                                setEventDateAdapter()
                            }

                            eventList.addAll(list)
                            multiDayAdapter!!.notifyDataSetChanged()
                            setList(eventList)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun setList(list: ArrayList<EventData>) {
        if (list.size > 0) {
            binding.nofoundTV.visibleView(false)
            binding.rollingRV.visibleView(true)
        } else {
            binding.nofoundTV.visibleView(true)
            binding.rollingRV.visibleView(false)
        }

    }

    private fun setEventAdapter() {
        if (dayEventList.size > 0) {
            binding.dateTV.visibleView(true)
            dayEventAdapter = EventsAdapter(baseActivity!!, background, dayEventList[0].data)
            binding.rollingRV.adapter = dayEventAdapter
            dayEventAdapter!!.setOnItemClickListener(this)
            handleNotificationPagination()
        }

    }

    private fun setEventDateAdapter() {
        binding.dateTV.visibleView(false)
        multiDayAdapter = EventDateWiseAdapter(baseActivity!!, background, eventList)
        binding.rollingRV.adapter = multiDayAdapter
        multiDayAdapter!!.setOnItemClickListener(this)
        handleNotificationPagination()
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClick(vararg items: Any) {
        when {
            items[1] as String == "month" -> {
                year = monthList[items[0] as Int].year
                monthSelected = monthList[items[0] as Int].monthId
                onMonthSelectStartDate()
                if ((monthSelected + 1) == SimpleDateFormat("MM").format(Calendar.getInstance().time)
                        .toInt()
                ) {
                    startDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                }
                setDateAdapter()
                calculateEndDate()
                highLightedDaysWeek()
                callEventApi()
            }
            items[1] as String == "date" -> {
                dateelected = items[0] as Int
                val date = if (dateelected < 10) {
                    "0${dateelected + 1}"
                } else {
                    (dateelected + 1).toString()
                }
                startDate = year.plus("-${(monthSelected+1)}-$date")

                val selectedDate = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
                val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
                if (selectedDate!! == SimpleDateFormat("yyyy-MM-dd").parse(currentDate)) {
                    binding.dateTV.text = "Today"
                } else {
                    binding.dateTV.text = DateFunctions.convertDateFormatFromUTC(
                        "yyyy-MM-dd",
                        "dd MMM, yyyy", startDate
                    )
                }
                dateList.forEachIndexed { index, video ->
                    video.takeIf { it.isSelected }?.let {
                        dateList[index] = it.copy(isSelected = false)
                    }
                }

                highLightedDaysWeek()
                calculateEndDate()
                callEventApi()
            }
            else -> {
                val bundle = Bundle()
                bundle.putParcelable("detail", dayEventList[0].data[items[0] as Int])
                findNavController().navigate(R.id.home_to_rolling_detail, bundle)
            }
        }

    }

    private fun highLightedDaysWeek() {
        dateList.forEachIndexed { index, video ->
            video.takeIf { it.isSelected }?.let {
                dateList[index] = it.copy(isSelected = false)
            }
        }
        when (filterType) {
            Constants.EVENT_FILTER_TYPE.DAY.value -> {
                dateList[dateelected].isSelected = true
            }
            else -> {
                try {
                    for (i in dateelected until (dateelected + 7)) {
                        dateList[i].isSelected = true
                    }

                } catch (e: Exception) {
                    showException(e)
                }

            }
        }
        dateAdapter?.notifyDataSetChanged()
    }

    private fun calculateEndDate() {
        val calendar = Calendar.getInstance()

        when (filterType) {
            Constants.EVENT_FILTER_TYPE.DAY.value -> {
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(startDate)!!
                calendar.add(Calendar.DATE, 0)
            }
            Constants.EVENT_FILTER_TYPE.WEEK.value -> {
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(startDate)!!

                val (dateFormat: DateFormat, date, yearMonthObject: YearMonth) = getDaysOfMonth()
                val daysInMonth: Int = yearMonthObject.lengthOfMonth()
                val days = daysInMonth - dateelected
                if (days <= 6) {
                    calendar.add(Calendar.DATE, days - 1)
                } else {
                    calendar.add(Calendar.DATE, 7)
                }
            }
            Constants.EVENT_FILTER_TYPE.MONTH.value -> {
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(startDate)!!
                val (dateFormat: DateFormat, date, yearMonthObject: YearMonth) = getDaysOfMonth()
                val daysInMonth: Int = yearMonthObject.lengthOfMonth()
                calendar.add(Calendar.DATE, daysInMonth - 1)
            }
        }
        endDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
        if (BuildConfig.DEBUG) {
            Log.e("catch_date", "start: $startDate $endDate")
        }
    }

    private fun callEventApi() {
        viewModel.filterBy.value = filterType
        viewModel.date.value = startDate
        viewModel.endDate.value = endDate
        viewModel.page.value = pageNumber
        viewModel.hitEventApi()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setMonthAdapter() {
        val monthsArray = resources.getStringArray(R.array.month)
        var currentYear = year
        var ids = 0
        for (i in 0 until 24) {
            val data = MonthCalendarData()
            data.id = i
            data.monthId = ids
            data.month = monthsArray[ids]
            data.year = currentYear
            ids++
            if (i == 11) {
                ids = 0
                currentYear = (currentYear.toInt() + 1).toString()
            }
            monthList.add(data)
        }
        val dateFormat: DateFormat = SimpleDateFormat("MM")
        val date = Date()
        monthSelected = dateFormat.format(date).toInt() - 1
        val adapter = MonthAdapter(baseActivity!!, monthList, (dateFormat.format(date).toInt() - 1))
        binding.monthRV.adapter = adapter
        adapter.setOnItemClickListener(this)
        binding.monthRV.scrollToPosition(dateFormat.format(date).toInt() - 1)
    }


    @SuppressLint("SimpleDateFormat")
    private fun setDateAdapter() {
        dateList.clear()
        val (dateFormat: DateFormat, date, yearMonthObject: YearMonth) = getDaysOfMonth()
        val daysInMonth: Int = yearMonthObject.lengthOfMonth()
        for (i in 0 until daysInMonth) {
            val date = SimpleDateFormat("yyyy/MM/dd").parse(
                "${
                    year.toInt()
                }/${(monthSelected + 1)}/${i + 1}"
            )
            val day = SimpleDateFormat("EEE").format(date!!)
            dateList.add(DateData((i + 1).toString(), day))
        }

        val dayFormat: DateFormat = SimpleDateFormat("dd")
        val monthFormat: DateFormat = SimpleDateFormat("MM")
        val day = Date()
        dateAdapter = if ((monthSelected + 1) != monthFormat.format(day).toInt()) {
            dateList[0].isSelected = true
            DateAdapter(baseActivity!!, dateList, filterType)
        } else {
            dateList[dayFormat.format(day).toInt() - 1].isSelected = true
            DateAdapter(baseActivity!!, dateList, filterType)
        }

        binding.dateRV.adapter = dateAdapter
        dateAdapter?.setOnItemClickListener(this)
        if (((monthSelected + 1)) != monthFormat.format(day).toInt()) {
            dateelected = 0
            binding.dateRV.scrollToPosition(0)
        } else {
            dateelected = dayFormat.format(day).toInt() - 1
            binding.dateRV.scrollToPosition(dayFormat.format(day).toInt() - 1)
        }

    }

    private fun getDaysOfMonth(): Triple<DateFormat, Date, YearMonth> {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy")
        val date = Date()
        val yearMonthObject: YearMonth =
            YearMonth.of(year.toInt(), (monthSelected + 1))
        return Triple(dateFormat, date, yearMonthObject)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.logoutIV -> {
                showLogoutDialog()
            }
            R.id.notificationIV -> {
                findNavController().navigate(R.id.action_home_to_notification)
            }
            R.id.typeLL -> {
                rotate(180f)
                showPopupReportPost(binding.describeTV)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPopupReportPost(
        view: AppCompatTextView
    ) {
        val popupView: View =
            LayoutInflater.from(baseActivity!!).inflate(
                R.layout.popup_day_type,
                null
            )
        val width: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        val dayTV: AppCompatTextView =
            popupView.findViewById(R.id.dayTV) as AppCompatTextView
        val weekTV: AppCompatTextView =
            popupView.findViewById(R.id.weekTV) as AppCompatTextView
        val monthTV: AppCompatTextView =
            popupView.findViewById(R.id.monthTV) as AppCompatTextView
        popupWindow.showAtLocation(
            popupView,
            Gravity.TOP,
            popupLocateView(view)?.right!!,
            popupLocateView(view)?.top!!
        )
        // show the dim background
        val container: View = if (popupWindow.background == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent as View
            } else {
                popupWindow.contentView
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent.parent as View
            } else {
                popupWindow.contentView.parent as View
            }
        }
        val context: Context = popupWindow.contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.1f
        p.horizontalMargin = 10.0f
        wm.updateViewLayout(container, p)
        popupView.setOnTouchListener { v, event ->
            popupWindow.dismiss()
            true
        }
        dayTV.setOnClickListener {
            filterType = Constants.EVENT_FILTER_TYPE.DAY.value
            binding.calenderDatesCL.visibleView(true)
            binding.typeTV.text = getString(R.string.day)
            endDate = startDate
            popupWindow.dismiss()
            if ((monthSelected + 1) == SimpleDateFormat("MM").format(Calendar.getInstance().time)
                    .toInt()
            ) {
                startDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
            }
            calculateEndDate()
            callEventApi()
            dateList.forEachIndexed { index, video ->
                video.takeIf { it.isSelected }?.let {
                    dateList[index] = it.copy(isSelected = false)
                }
            }
            setDateAdapter()
        }
        weekTV.setOnClickListener {
            filterType = Constants.EVENT_FILTER_TYPE.WEEK.value
            binding.calenderDatesCL.visibleView(true)
            binding.typeTV.text = getString(R.string.week)
            popupWindow.dismiss()
            if ((monthSelected + 1) == SimpleDateFormat("MM").format(Calendar.getInstance().time)
                    .toInt()
            ) {
                startDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
            }
            setDateAdapter()
            calculateEndDate()
            callEventApi()
            highLightedDaysWeek()
        }
        monthTV.setOnClickListener {
            filterType = Constants.EVENT_FILTER_TYPE.MONTH.value
            binding.typeTV.text = getString(R.string.month)
            binding.calenderDatesCL.visibleView(false)
            popupWindow.dismiss()
            onMonthSelectStartDate()
            calculateEndDate()
            callEventApi()
        }

        popupWindow.setOnDismissListener {
            rotate(0f)
        }

    }

    private fun onMonthSelectStartDate() {
        Log.e("catch_date", "month: $monthSelected")
        val monthS = monthSelected
        val month = if ((monthS + 1) < 10) {
            "0${monthSelected + 1}"
        } else {
            (monthSelected + 1).toString()
        }
        startDate = year.plus("-$month-01")
    }

    private fun rotate(degree: Float) {
        val rotateAnim = RotateAnimation(
            0.0f, degree,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnim.duration = 0
        rotateAnim.fillAfter = true
        binding.arrowIV.startAnimation(rotateAnim)
    }


    private fun popupLocateView(v: View?): Rect? {
        val locInt = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(locInt)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = locInt[0]
        location.top = locInt[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    private fun showLogoutDialog() {
        dialog = Dialog(baseActivity!!, android.R.style.Theme_Translucent_NoTitleBar)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_logout)
        dialog?.setCancelable(true)
        val cancelBT = dialog?.findViewById(R.id.canelBT) as AppCompatTextView
        val logoutBT = dialog?.findViewById(R.id.logoutBT) as AppCompatTextView
        val logoutCL = dialog?.findViewById(R.id.logoutCL) as ConstraintLayout
        val card = dialog?.findViewById(R.id.card) as CardView
        card.setOnClickListener {

        }
        logoutCL.setOnClickListener {
            dialog?.dismiss()
        }
        cancelBT.setOnClickListener {
            dialog?.dismiss()
        }
        logoutBT.setOnClickListener {
            dialog?.dismiss()
            viewModel.hitLogOutApi()
        }
        dialog?.show()
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
                    binding.rollingRV.layoutManager!!.childCount
                totalItemCount =
                    binding.rollingRV.layoutManager!!.itemCount
                pastVisiblesItems =
                    (binding.rollingRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (isScrolling && visibleItemCount + pastVisiblesItems >= totalItemCount && (currentPage < totalPage)) {
                    isScrolling = false
                    currentPage++
                    pageNumber++
                    callEventApi()
                }
            }
        }
    }


}