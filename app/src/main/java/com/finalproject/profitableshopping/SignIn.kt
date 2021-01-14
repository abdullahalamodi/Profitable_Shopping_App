package com.finalproject.profitableshopping

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
     lateinit var sign_up : TextView*/

     lateinit var signUpBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signUpBtn = findViewById(R.id.sign_up)
        btn_login.setOnClickListener {
            var email = email_login.text.toString()
            var password = passWord_login.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                if (!isValidEmail(email)) {
                    Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password.length < 6) {
                    Toast.makeText(this, "Password must be 6 digit at least ", Toast.LENGTH_SHORT)
                        .show()
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
        if (CheckAdmin(email, password)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val p = ProgressDialog(this)
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        mAuth = FirebaseAuth.getInstance()
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) {
                p.dismiss()
                if (it.isSuccessful) {
                    verifyEmailAddress()
                    saveUserToken(it.result?.user?.uid.toString())
                    saveUserData(email,password)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Your login Failed ${it.exception.toString()}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            Toast.makeText(this, " sign up", Toast.LENGTH_LONG)
                .show()
        }

    }


    private fun verifyEmailAddress() {

        val user: FirebaseUser? = mAuth?.currentUser
        if (user!!.isEmailVerified) {

            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
            var intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please verify your account", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserToken(token: String) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(tokenKey, token)
        editor.apply()
        editor.commit()
    }

    private fun saveUserData(email: String, password: String) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
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

    companion object {
        private const val sharedPrefFile = "user_pref";
        private const val tokenKey = "token_key";
        private const val emailKey = "email_key";
        private const val passwordKey = "password_key";

    }
}
