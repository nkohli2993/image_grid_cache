package com.rolling.meadows.views.view_main.homePages

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.os.Build
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
import androidx.navigation.fragment.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseAdapter
import com.rolling.meadows.base.BaseFragment
import com.rolling.meadows.data.DateData
import com.rolling.meadows.databinding.FragmentHomeBinding
import com.rolling.meadows.utils.extensions.visibleView
import com.rolling.meadows.views.view_main.homePages.adapter.DateAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.EventDateWiseAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.EventsAdapter
import com.rolling.meadows.views.view_main.homePages.adapter.MonthAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    BaseAdapter.OnItemClick {
    override fun getLayoutRes() = R.layout.fragment_home
    private var background: ArrayList<Int> = arrayListOf()
    private val lastDayInCalendar = Calendar.getInstance()
    var monthSelected = -1
    var dateelected = -1

    override fun onItemClick(vararg items: Any) {
        when {
            items[1] as String == "month" -> {
                monthSelected = items[0] as Int
                monthSelected++
                setDateAdapter()
            }
            items[1] as String == "date" -> {
                dateelected = items[0] as Int
            }
            else -> {
                findNavController().navigate(R.id.home_to_rolling_detail)
            }
        }

    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding.listener = this
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))
        changeStatusBarIconColor(true)
        background.clear()
        background.add(R.drawable.ic_background_pink)
        background.add(R.drawable.ic_background_yellow)
        background.add(R.drawable.ic_background_blue)
        background.add(R.drawable.ic_background_green)
        background.add(R.drawable.ic_background_pruple)
        // viewModel.hitNotificationApi()
        setMonthAdapter()
        setDateAdapter()
        setEventAdapter()
        lastDayInCalendar.add(Calendar.MONTH, 3)
    }

    private fun setMonthAdapter() {
        val months: ArrayList<String> = arrayListOf()
        months.add("January")
        months.add("February")
        months.add("March")
        months.add("April")
        months.add("May")
        months.add("June")
        months.add("July")
        months.add("August")
        months.add("September")
        months.add("October")
        months.add("November")
        months.add("December")
        val dateFormat: DateFormat = SimpleDateFormat("MM")
        val date = Date()
        monthSelected = dateFormat.format(date).toInt()
        val adapter = MonthAdapter(baseActivity!!, months, (dateFormat.format(date).toInt() - 1))
        binding.monthRV.adapter = adapter
        adapter.setOnItemClickListener(this)
        binding.monthRV.scrollToPosition(dateFormat.format(date).toInt() - 1)
    }


    @SuppressLint("SimpleDateFormat")
    private fun setDateAdapter() {
        Log.e("catch_exception", "month: ${monthSelected}")
        val dateList: ArrayList<DateData> = arrayListOf()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy")
        val date = Date()
        val yearMonthObject: YearMonth =
            YearMonth.of(dateFormat.format(date).toInt(), (monthSelected))
        val daysInMonth: Int = yearMonthObject.lengthOfMonth() //28

        for (i in 0 until daysInMonth) {
            val date = SimpleDateFormat("yyyy/MM/dd").parse(
                "${
                    dateFormat.format(date).toInt()
                }/${monthSelected}/${i + 1}"
            )
            val day = SimpleDateFormat("EEE").format(date!!)
            dateList.add(DateData((i + 1).toString(), day))
        }

        val dayFormat: DateFormat = SimpleDateFormat("dd")
        val monthFormat: DateFormat = SimpleDateFormat("MM")
        val day = Date()
        val adapter = if (monthSelected != monthFormat.format(day).toInt()) {
            DateAdapter(baseActivity!!, dateList, 0)
        } else {
            DateAdapter(baseActivity!!, dateList, (dayFormat.format(day).toInt() - 1))
        }

        binding.dateRV.adapter = adapter
        adapter.setOnItemClickListener(this)
        if (monthSelected != monthFormat.format(day).toInt()) {
            binding.dateRV.scrollToPosition(0)
        } else {
            binding.dateRV.scrollToPosition(dayFormat.format(day).toInt() - 1)
        }

    }

    private fun setEventAdapter() {
        binding.dateTV.visibleView(true)
        val adapter = EventsAdapter(baseActivity!!, background)
        binding.rollingRV.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    private fun setEventDateAdapter() {

        binding.dateTV.visibleView(false)
        val adapter = EventDateWiseAdapter(baseActivity!!, background)
        binding.rollingRV.adapter = adapter
        adapter.setOnItemClickListener(this)
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
            binding.calenderDatesCL.visibleView(true)
            binding.typeTV.text = getString(R.string.day)
            popupWindow.dismiss()
            setEventAdapter()
        }
        weekTV.setOnClickListener {
            binding.calenderDatesCL.visibleView(true)
            binding.typeTV.text = getString(R.string.week)
            popupWindow.dismiss()
            setEventDateAdapter()
        }
        monthTV.setOnClickListener {
            binding.typeTV.text = getString(R.string.month)
            binding.calenderDatesCL.visibleView(false)
            popupWindow.dismiss()
            setEventDateAdapter()
        }

        popupWindow.setOnDismissListener {
            rotate(0f)
        }

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
        val dialog = Dialog(baseActivity!!, android.R.style.Theme_Translucent_NoTitleBar)
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
//            viewModel.onClickLogout()
            baseActivity!!.goToInitialActivity()
        }
        dialog?.show()
    }


}