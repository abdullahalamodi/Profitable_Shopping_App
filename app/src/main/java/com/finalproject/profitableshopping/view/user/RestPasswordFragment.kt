package com.finalproject.profitableshopping.view.user

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.authentication.fragments.LogInFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_rest_password.*


class RestPasswordFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rest_password, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        btnResetPassword.setOnClickListener {
            val email = edtResetEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Check email to reset your password!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Fail to send reset password email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        btnBack.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
           transaction?.replace(R.id.container, LogInFragment())
            transaction?.addToBackStack(null)
           transaction?.commit()

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = RestPasswordFragment().apply {
            arguments = Bundle().apply {


            }
        }
    }

}

