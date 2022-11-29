package com.rolling.meadows.views.authentication.intro

import android.os.Bundle
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseFragment

class IntroFragment : BaseFragment<com.rolling.meadows.databinding.FragmentIntroBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_intro


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*binding.registerBT.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment)
        }*/


      /*  binding.loginTV.setSpanString(
            baseActivity!!.getString(R.string.already_have_account_log_in),
            startPos = 22,
            isBold = true,
            color = R.color.black,
            onClick = {
                findNavController().navigate(R.id.action_introFragment_to_loginFragment)
            })*/

    }

    override fun observeViewModel() {

    }

    override fun initViewBinding() {

    }
}