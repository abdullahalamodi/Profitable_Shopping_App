package com.finalproject.profitableshopping.view.authentication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.authentication.PhoneNumberLoginActivity

class ActiveFragment : Fragment() {
    private var activeAccountCallbacks: ActiveAccountCallbacks? = null
    lateinit var signWithPhone : Button


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activeAccountCallbacks=context as ActiveAccountCallbacks
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onStart() {
        super.onStart()
        signWithPhone.setOnClickListener {
            activeAccountCallbacks?.onActiveAccount()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_active, container, false)
        signWithPhone=view.findViewById(R.id.btn_SignWithPhone)
        return view
    }

    override fun onDetach() {
        super.onDetach()
        activeAccountCallbacks=null
    }
interface ActiveAccountCallbacks{
  fun  onActiveAccount()
}
    companion object {

        @JvmStatic
        fun newInstance() =
            ActiveFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}