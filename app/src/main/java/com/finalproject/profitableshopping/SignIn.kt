package com.finalproject.profitableshopping

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.finalproject.profitableshopping.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
   /* lateinit var email_login : EditText
    lateinit var passWord_login : EditText
    lateinit var btn_login : Button
    lateinit var sign_up : TextView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_login.setOnClickListener {
            var email = email_login.text.toString()
            var password = passWord_login.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if(!isValidEmail(email)){
                    Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(password.length<6){
                    Toast.makeText(this, "Password must be 6 digit at least ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                login(email, password)
            } else {
                Toast.makeText(this, "failed login", Toast.LENGTH_LONG).show()
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

    private fun login(email: String, password: String) {
        var p= ProgressDialog(this)
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        mAuth = FirebaseAuth.getInstance()
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) {
                p.dismiss()
                if (it.isSuccessful) {
                    verifyEmailAddress()
                } else {
                    Toast.makeText(this, "Your login Failed ${it.exception.toString()}", Toast.LENGTH_LONG)
                        .show()
                }
            }

        sign_up.setOnClickListener {
            var intent= Intent(this,SignIn::class.java)
            startActivity(intent)
        }

    }


    private fun verifyEmailAddress() {

        val user: FirebaseUser? = mAuth?.currentUser
        if(user!!.isEmailVerified){
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
            var intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Please verify your account", Toast.LENGTH_LONG).show()
        }

    }
    }
