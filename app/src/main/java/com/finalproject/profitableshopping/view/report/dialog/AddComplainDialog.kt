package com.finalproject.profitableshopping.view.report.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Complain
import com.finalproject.profitableshopping.viewmodel.ComplainViewModel
import com.google.android.material.textfield.TextInputEditText


class AddComplainDialog : DialogFragment() {

    lateinit var complainViewModel: ComplainViewModel
    lateinit var complainTitleEt: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        complainViewModel = ViewModelProviders.of(this).get(ComplainViewModel::class.java)

        val view = activity?.layoutInflater?.inflate(R.layout.add_complain_dilog, null)
        complainTitleEt = view?.findViewById(R.id.report_name_et) as TextInputEditText

        return AlertDialog
            .Builder(requireContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
            .setView(view)
            .setPositiveButton("إضافة") { dialog, _ ->
                val com = Complain(
                    null,
                    complainTitleEt.text.toString()
                )
                complainViewModel.addComplain(com).observe(
                    this,
                    Observer {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                )
                dialog.dismiss()

            }
            .setNegativeButton("الغاء") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        fun newInstance(): AddComplainDialog {


            return AddComplainDialog().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
}