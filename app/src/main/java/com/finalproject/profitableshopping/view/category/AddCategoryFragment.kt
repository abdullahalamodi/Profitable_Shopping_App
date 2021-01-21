package com.finalproject.profitableshopping.view.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finalproject.profitableshopping.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddCategoryFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_category,container,false)
        return view;
    }

}