package com.finalproject.profitableshopping.view.authentication.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.viewmodel.FavoriteViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    lateinit var userNameEt: EditText
    lateinit var userEmailEt: EditText
    lateinit var userPassEt: EditText
    lateinit var confrimPassEt: EditText
    lateinit var registerBtn: Button
    lateinit var auth: FirebaseAuth
    var signUpCallbacks: SignUpCallbacks? = null
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpCallbacks = context as SignUpCallbacks
    }

    override fun onStart() {
        super.onStart()
        registerBtn.setOnClickListener {
            var userName = userNameEt.text.toString()
            var email = userEmailEt.text.toString()
            var password = userPassEt.text.toString()
            var confirmPassword = confrimPassEt.text.toString()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length > 6 && password.length < 14) {
                    if (password == confirmPassword) {
                        if (!isValidEmail(email)) {
                            Toast.makeText(
                                requireContext(),
                                "Please enter valid email",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        register(userName, email, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "password not equal confirmPassword",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password length must be between 6 and 14 ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "some fields empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        userNameEt = view.findViewById(R.id.register_user_name)
        userEmailEt = view.findViewById(R.id.register_email)
        userPassEt = view.findViewById(R.id.register_password)
        confrimPassEt = view.findViewById(R.id.register_confirm_password)
        registerBtn = view.findViewById(R.id.btn_register)

        return view
    }

    override fun onDetach() {
        super.onDetach()
        signUpCallbacks = null
    }

    private fun register(userName: String, email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        var p = ProgressDialog(requireContext())
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                p.dismiss()
                if (it.isSuccessful) {
                    sendEmailVerification()
                    // create favorite for user
/*                    favoriteViewModel.createFavorite(
                        Favorite(
                            null,
                            AppSharedPreference.getUserId(requireContext())!!
                        )
                    ).observe(
                        this,
                        Observer {
                            AppSharedPreference.setFavoriteId(requireContext(), it)
                        }
                    )*/
                } else {
                    Toast.makeText(
                        requireContext(),
                        " You registered Failed ${it.exception.toString()}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "You registered successful", Toast.LENGTH_LONG)
                    .show()
                /* var intent = Intent(requireContext(), SignIn::class.java)
                 startActivity(intent)*/
                signUpCallbacks?.onCreateAccountSuccess()
            }
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {

            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    interface SignUpCallbacks {
        fun onCreateAccountSuccess()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}