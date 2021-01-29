package com.finalproject.profitableshopping.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.products.fragments.DetailsOfAllProductsFragment
import com.finalproject.profitableshopping.view.user.ManageUserProfileFragment


class AboutAppFragment : Fragment() {

    var callbacks: Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    interface Callbacks {
        fun onAboutAppOpen(show: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as AboutAppFragment.Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks?.onAboutAppOpen(true)
        callbacks=null
    }


}