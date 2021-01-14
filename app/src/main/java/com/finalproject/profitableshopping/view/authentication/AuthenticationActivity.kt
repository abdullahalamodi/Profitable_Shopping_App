package com.finalproject.profitableshopping.view.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.finalproject.profitableshopping.PhoneNumberLoginActivity
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.SignIn
import com.finalproject.profitableshopping.SignUp

class AuthenticationActivity : AppCompatActivity(){

    lateinit var signIn: Button
    lateinit var signUp : Button
    lateinit var signWithPhone : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        signIn  = findViewById(R.id.btn_SignIn)
        signUp = findViewById(R.id.btn_SignUp)
        signWithPhone =findViewById(R.id.btn_SignWithPhone)

        signIn.setOnClickListener {
            var signInIntent= Intent(this,SignIn::class.java)
            startActivity(signInIntent)
        }

        signUp.setOnClickListener {
            var signUpIntent= Intent(this,SignUp::class.java)
            startActivity(signUpIntent)
        }

        signWithPhone.setOnClickListener {
            var intent= Intent(this, PhoneNumberLoginActivity::class.java)
            startActivity(intent)
        }
    }
    }


