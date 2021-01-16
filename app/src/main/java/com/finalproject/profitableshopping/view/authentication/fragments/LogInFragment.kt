package com.finalproject.profitableshopping.view.authentication.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_log_in.*


class LogInFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    lateinit var email_login : EditText
    lateinit var passWord_login : EditText
    lateinit var signUpBtn : Button
     var loginCallbacks: LoginCallbacks?=null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginCallbacks=context as LoginCallbacks

    }
    override fun onStart() {
        super.onStart()
        signUpBtn.setOnClickListener {
           /* val intent = Intent(requireContext(), SignUp::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), " sign up", Toast.LENGTH_LONG)
                .show()*/
            loginCallbacks?.onSignUpClicked()
        }
        btn_login.setOnClickListener {
            var email = email_login.text.toString()
            var password = passWord_login.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if (!isValidEmail(email.trim())) {
                    Toast.makeText(requireContext(), "Please enter valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password.length < 6) {
                    Toast.makeText(requireContext(), "Password must be 6 digit at least ", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                login(email, password)
            } else {
                Toast.makeText(requireContext(), "failed login", Toast.LENGTH_LONG).show()
            }
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
        val view=inflater.inflate(R.layout.fragment_log_in, container, false)
        email_login=view.findViewById(R.id.email_login)
        passWord_login=view.findViewById(R.id.passWord_login)
        signUpBtn=view.findViewById(R.id.sign_up)
        return view
    }

    override fun onDetach() {
        super.onDetach()
        loginCallbacks=null
    }
    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private fun login(email: String, password: String) {
        if (CheckAdmin(email, password)) {
            saveUserToken("Admin")
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            //finish()

        }
        val p = ProgressDialog(requireContext())
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        mAuth = FirebaseAuth.getInstance()

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(requireActivity()) {
                p.dismiss()
                if (it.isSuccessful) {
                    verifyEmailAddress()
                  //  it.result?.user?.getIdToken(true)?.result?.token?.let { it1 -> saveUserToken(it1) }
                    saveUserToken(it.result?.user?.uid.toString())
                    saveUserData(email,password)
                    //finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Your login Failed ${it.exception.toString()}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }



    }


    private fun verifyEmailAddress() {

        val user: FirebaseUser? = mAuth?.currentUser
        if (user!!.isEmailVerified) {

            Toast.makeText(requireContext(), "Done", Toast.LENGTH_LONG).show()
            var intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Please verify your account", Toast.LENGTH_LONG).show()
            var intent = Intent(requireContext(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun saveUserToken(token: String) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(tokenKey, token)

        editor.apply()
        editor.commit()
    }

    private fun saveUserData(email: String, password: String) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(emailKey, email)
        editor.putString(passwordKey, password)
        editor.apply()
        editor.commit()
    }



    private fun CheckAdmin(email: String, password: String): Boolean {
        val admin = adminAccount()
        if (
            admin["email"] == email &&
            admin["password"] == password
        ) {
            saveUserToken("admin")
            return true
        }
        return false
    }

    private fun adminAccount(): HashMap<String, String> {
        val admin = HashMap<String, String>()
        admin["email"] = "admin@admin.com"
        admin["password"] = "123456"
        return admin
    }
interface LoginCallbacks{
    fun onSignUpClicked()
}
    companion object {
        const val sharedPrefFile = "user_pref";
        const val tokenKey = "token_key";
        private const val emailKey = "email_key";
        private const val passwordKey = "password_key";
         var isAccountActive = false;
        @JvmStatic
        fun newInstance() =
            LogInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}