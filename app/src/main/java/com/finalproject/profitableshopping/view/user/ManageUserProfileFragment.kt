package com.finalproject.profitableshopping.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.viewmodel.ReportViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageUserProfileFragment : Fragment() {
    lateinit var reportViewModel: ReportViewModel
    lateinit var manage : FloatingActionButton
    lateinit var location: FloatingActionButton
    var userCountOfReport=0
    var callbacks:Callbacks?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }
    override fun onStart() {
        super.onStart()
        reportViewModel.getUserReports(AppSharedPreference.getUserId(requireContext())!!).observe(
            this,
            Observer{
                    userCountOfReport=it.size
                  Log.d("user report",it.size.toString())
            }
        )
        manage.setOnClickListener {
            callbacks?.onOpenProfile()
        }
        location.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportViewModel= ViewModelProviders.of(this).get(ReportViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_manage_user, container, false)
        manage=view.findViewById(R.id.UserManage)
        location=view.findViewById(R.id.location)
        return view
    }

    interface Callbacks{

        fun onOpenProfile()
    }
    companion object {


        fun newInstance() =
            ManageUserProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}