package com.finalproject.profitableshopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var user_name: EditText
    lateinit var user_email: EditText
    lateinit var user_pass: EditText
    lateinit var confrim_pass: EditText
    lateinit var btn_register: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        user_name = findViewById(R.id.register_user_name)
        user_email = findViewById(R.id.register_email)
        user_pass = findViewById(R.id.register_password)
        confrim_pass = findViewById(R.id.register_confirm_password)
        btn_register = findViewById(R.id.btn_register)
        btn_register.setOnClickListener {
            var userName = user_name.text.toString()
            var email = user_email.text.toString()
            var password = user_pass.text.toString()
            var confirmPassword = confrim_pass.text.toString()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length > 6 && password.length < 14) {
                    if (password == confirmPassword) {
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
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {

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


}
