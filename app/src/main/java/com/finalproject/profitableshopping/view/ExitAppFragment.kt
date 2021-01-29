package com.finalproject.profitableshopping.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.finalproject.profitableshopping.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ExitAppFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_exit_app, container, false)
            val exitBtn =view.findViewById<Button>(R.id.btn_exit_app).setOnClickListener {

            }
        return view
    }


}