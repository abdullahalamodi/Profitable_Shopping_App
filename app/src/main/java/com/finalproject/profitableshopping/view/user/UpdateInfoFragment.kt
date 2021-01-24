package com.finalproject.profitableshopping.view.user

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.finalproject.profitableshopping.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UpdateInfoFragment : Fragment() {
    lateinit var FullName : TextInputLayout
    lateinit var Email: TextInputLayout
    lateinit var PassWord: TextInputLayout
    lateinit var Phone: TextInputLayout
    lateinit var  fullNameLabel : TextView
    lateinit var  UserNameLabel: TextView

    lateinit var _USERNAME : String
    lateinit var _NAME : String
    lateinit var _EMAIL : String
    lateinit var _PASSWORD : String
    lateinit var _PHONENO : String
     private lateinit var reference: DatabaseReference
    lateinit var update: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        reference = FirebaseDatabase.getInstance().getReference("users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_update_info, container, false)

        FullName = view.findViewById(R.id.full_name_profile)
        Email = view.findViewById(R.id.full_email_profile)
        PassWord = view.findViewById(R.id.full_pass_profile)
        Phone = view.findViewById(R.id.full_phone_profile)
        fullNameLabel= view.findViewById(R.id.fullname_field)
        UserNameLabel = view.findViewById(R.id.username_field)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAllUserData()
    }
    private fun showAllUserData() {
        var intent : Intent = getIntent("")
        var user_userName : String? = intent.getStringExtra("username")
        var user_name : String? = intent.getStringExtra("name")
        var user_email : String? = intent.getStringExtra("email")
        var user_phoneNo : String? = intent.getStringExtra("phoneNo")
        var user_password : String? = intent.getStringExtra("password")


        fullNameLabel.setText(user_name)
        UserNameLabel.setText(user_userName)
        FullName.getEditText()?.setText(user_name)
        Email.getEditText()?.setText(user_email)
        Phone.getEditText()?.setText(user_phoneNo)
        PassWord.getEditText()?.setText(user_password)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UpdateInfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}