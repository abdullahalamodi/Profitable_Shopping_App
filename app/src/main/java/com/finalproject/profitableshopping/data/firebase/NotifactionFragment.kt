package com.finalproject.profitableshopping.data.firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finalproject.profitableshopping.R


class NotifactionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifaction, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = NotifactionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}