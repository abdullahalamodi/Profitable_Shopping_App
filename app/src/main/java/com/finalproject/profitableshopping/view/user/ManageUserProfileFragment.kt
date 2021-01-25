package com.finalproject.profitableshopping.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.firebase.NotifationActivity
import com.finalproject.profitableshopping.view.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageUserProfileFragment : Fragment() {

    lateinit var manage : FloatingActionButton
    lateinit var location: FloatingActionButton
    lateinit var notifationM: FloatingActionButton
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
        manage.setOnClickListener {
            callbacks?.onOpenProfile()
        }
        location.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
    notifationM.setOnClickListener {
            val intent = Intent(requireContext(), NotifationActivity::class.java)
            startActivity(intent)

        }
    }
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
       val view= inflater.inflate(R.layout.fragment_manage_user, container, false)
        manage=view.findViewById(R.id.UserManage)
        location=view.findViewById(R.id.location)
        notifationM=view.findViewById(R.id.notification)
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