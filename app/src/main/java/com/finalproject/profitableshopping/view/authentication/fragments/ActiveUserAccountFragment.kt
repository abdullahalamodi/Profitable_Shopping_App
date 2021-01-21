package com.finalproject.profitableshopping.view.authentication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.finalproject.profitableshopping.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_phone_number_login.*
import java.util.concurrent.TimeUnit

class ActiveUserAccountFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks;
    lateinit var storeVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onStart() {
        super.onStart()
        btn_get_otp.setOnClickListener {
            var photoNo: String = ed_phone_no.text.toString().trim()
            if (photoNo.isNotEmpty() &&photoNo.length==9 ) {
                sendVerificationCode("+967$photoNo")
            } else {
                Toast.makeText(requireContext(), "Please Enter Your Phone Number contain 9 digits", Toast.LENGTH_LONG).show()
            }
        }
        btn_sign_up.setOnClickListener {
            var otp: String = ed_otp.text.toString().trim()
            if (otp.isNotEmpty()) {
                verifyVerificationCode(otp)
            } else {
                Toast.makeText(requireContext(), "Please Enter Otp", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code : String? =credential.smsCode
                if(code!=null){
                    ed_otp.setText(code)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(requireContext().applicationContext, "Auth Failed ~_~", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                storeVerificationId = verificationId
                resendToken = token
                ly_phoneNumber.visibility= View.GONE
                ly_otp.visibility= View.VISIBLE
            }

        }
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_user_account, container, false)
    }
    private fun sendVerificationCode(phoneNo: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNo,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callback
        )
    }

    private fun verifyVerificationCode(code:String){
        val credential : PhoneAuthCredential =PhoneAuthProvider.getCredential(storeVerificationId,code)
        signUp(credential)
    }

    private fun signUp(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()){
                if(it.isSuccessful){
                    val user: FirebaseUser?=it.result?.user
                    saveUserState(true)

                    Toast.makeText(requireContext(), "SignUp Successfully", Toast.LENGTH_LONG).show()

                }else{
                    if (it.exception is FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(requireContext(), "Code Entered is incorrect", Toast.LENGTH_LONG).show()
                        ed_otp.setText("")
                    }
                }
            }

    }
    private fun saveUserState(isActive: Boolean) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(
            LogInFragment.sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(LogInFragment.isAccountActive.toString(), isActive)

        editor.apply()
        editor.commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ActiveUserAccountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}