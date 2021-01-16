package com.finalproject.profitableshopping.view.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.finalproject.profitableshopping.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_phone_number_login.*
import java.util.concurrent.TimeUnit

class PhoneNumberLoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks;
    lateinit var storeVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_login)
        auth= FirebaseAuth.getInstance()

        btn_get_otp.setOnClickListener {
            var photoNo: String = ed_phone_no.text.toString().trim()
            if (photoNo.isNotEmpty()) {
                sendVerificationCode("+967$photoNo")
            } else {
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_LONG).show()
            }
        }

        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code : String? =credential.smsCode
                if(code!=null){
                    ed_otp.setText(code)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, "Auth Failed ~_~", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                storeVerificationId = verificationId
                resendToken = token
                ly_phoneNumber.visibility= View.GONE
                ly_otp.visibility= View.VISIBLE
            }

        }

        btn_sign_up.setOnClickListener {
            var otp: String = ed_otp.text.toString().trim()
            if (otp.isNotEmpty()) {
                verifyVerificationCode(otp)
            } else {
                Toast.makeText(this, "Please Enter Otp", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendVerificationCode(phoneNo: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNo,
            60,
            TimeUnit.SECONDS,
            this,
            callback
        )
    }

    private fun verifyVerificationCode(code:String){
        val credential : PhoneAuthCredential =PhoneAuthProvider.getCredential(storeVerificationId,code)
        signUp(credential)
    }

    private fun signUp(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user: FirebaseUser?=it.result?.user

                    Toast.makeText(this, "SignUp Successfully", Toast.LENGTH_LONG).show()

                }else{
                    if (it.exception is FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(this, "Code Entered is incorrect", Toast.LENGTH_LONG).show()
                        ed_otp.setText("")
                    }
                }
            }

    }

}