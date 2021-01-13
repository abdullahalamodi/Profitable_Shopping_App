package com.finalproject.profitableshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignIn : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    lateinit var email_login : EditText
    lateinit var passWord_login : EditText
    lateinit var btn_login : Button
    lateinit var sign_up : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        btn_login.setOnClickListener {
            var email = email_login.text.toString()
            var password = passWord_login.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun login(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) {

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

        }else{
            Toast.makeText(this, "Please verify your account", Toast.LENGTH_LONG).show()
        }

    }
    }
