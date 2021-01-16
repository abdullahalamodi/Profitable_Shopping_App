package com.finalproject.profitableshopping.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference

class UserFragment : Fragment() {

    lateinit var emailTv:TextView
    lateinit var phoneTv:TextView
    lateinit var isActiveTv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user, container, false)
        emailTv = view.findViewById(R.id.user_email_tv)
        phoneTv = view.findViewById(R.id.user_phone_tv)
        isActiveTv = view.findViewById(R.id.user_isActive_tv)
        updateUi()
        return view
    }

    fun updateUi(){
        val user = AppSharedPreference.getUserData(context!!)
        emailTv.text = "email : "+user?.email
        phoneTv.text = "phone :"+user?.phone
        isActiveTv.text = "i sActive :"+user?.isActive.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserFragment()
    }
}