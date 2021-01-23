package com.finalproject.profitableshopping.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.finalproject.profitableshopping.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateInfoActivity : AppCompatActivity() {
    lateinit var FullName : TextInputLayout
    lateinit var Email: TextInputLayout
    lateinit var PassWord: TextInputLayout
    lateinit var Phone: TextInputLayout
    lateinit var  fullNameLabel : TextView
    lateinit var  UserNameLabel: TextView

    lateinit var _USERNAME : String
    lateinit var _NAME : String
    lateinit var _EMAIL : String
    lateinit var _PASSWORD : String
    lateinit var _PHONENO : String



    private lateinit var reference: DatabaseReference


    lateinit var update: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_info)

        FullName = findViewById(R.id.full_name_profile)
        Email = findViewById(R.id.full_email_profile)
        PassWord = findViewById(R.id.full_pass_profile)
        Phone = findViewById(R.id.full_phone_profile)
        fullNameLabel= findViewById(R.id.fullname_field)
        UserNameLabel = findViewById(R.id.username_field)
        //fun for retrive Data from firebase
        showAllUserData()
        reference = FirebaseDatabase.getInstance().getReference("users")
        update = findViewById(R.id.update)
        update.setOnClickListener {
          if (isNameChange() || isPasswordChange()){
              Toast.makeText(this ,"Data has been update",Toast.LENGTH_LONG).show()
          }else{
              Toast.makeText(this ,"Data is same and can not be updated",Toast.LENGTH_LONG).show()
          }
        }


    }

    private fun isNameChange():Boolean{
        if(!_NAME.equals(FullName.getEditText()?.getText().toString())){
            reference.child(_USERNAME).child("name").setValue(FullName.getEditText()?.getText().toString())
            _NAME=FullName.getEditText()?.getText().toString();
            return true
        }else{
            return false
        }

    }
    private fun isPasswordChange():Boolean{
        if(!_PASSWORD.equals(PassWord.getEditText()?.getText().toString()))
        {
            reference.child(_USERNAME).child("password").setValue(PassWord.getEditText()?.getText().toString());
            _PASSWORD=PassWord.getEditText()?.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private fun showAllUserData() {
        var intent : Intent = getIntent()
        var user_userName : String? = intent.getStringExtra("username")
        var user_name : String? = intent.getStringExtra("name")
        var user_email : String? = intent.getStringExtra("email")
        var user_phoneNo : String? = intent.getStringExtra("phoneNo")
        var user_password : String? = intent.getStringExtra("password")


        fullNameLabel.setText(user_name)
        UserNameLabel.setText(user_userName)
        FullName.getEditText()?.setText(user_name)
        Email.getEditText()?.setText(user_email)
        Phone.getEditText()?.setText(user_phoneNo)
        PassWord.getEditText()?.setText(user_password)
    }
}