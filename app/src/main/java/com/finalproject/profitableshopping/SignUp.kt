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
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var userNameEt: EditText
    lateinit var userEmailEt: EditText
    lateinit var userPassEt: EditText
    lateinit var confrimPassEt: EditText
    lateinit var registerBtn: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userNameEt = findViewById(R.id.register_user_name)
        userEmailEt = findViewById(R.id.register_email)
        userPassEt = findViewById(R.id.register_password)
        confrimPassEt = findViewById(R.id.register_confirm_password)
        registerBtn = findViewById(R.id.btn_register)

        registerBtn.setOnClickListener {
            var userName = userNameEt.text.toString()
            var email = userEmailEt.text.toString()
            var password = userPassEt.text.toString()
            var confirmPassword = confrimPassEt.text.toString()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length > 6 && password.length < 14) {
                    if (password == confirmPassword) {
                        if(!isValidEmail(email)){
                            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        register(userName, email, password)
                    } else {
                        Toast.makeText(this, "password not equal confirmPassword", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Password length must be between 6 and 14 ", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "some fields empty", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun register(userName: String, email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        var p= ProgressDialog(this)
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                p.dismiss()
                if (it.isSuccessful) {
                    sendEmailVerification()
                } else {
                    Toast.makeText(this, " You registered Failed ${it.exception.toString()}", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(this, "You registered successful", Toast.LENGTH_LONG).show()
                var intent = Intent(this, SignIn::class.java)
                startActivity(intent)
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
}
