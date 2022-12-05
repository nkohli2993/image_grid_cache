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
import com.rolling.meadows.databinding.FragmentHomeBinding
import com.rolling.meadows.databinding.FragmentRollingDetailBinding
import java.util.*


class RollingDetailFragment : BaseFragment<FragmentRollingDetailBinding>() {

    override fun getLayoutRes() = R.layout.fragment_rolling_detail
    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding.listener = this
        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        changeStatusBarIconColor(true)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id){
            R.id.backIV ->{
                findNavController().popBackStack()
            }
        }
    }



}