package com.finalproject.profitableshopping.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.finalproject.profitableshopping.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserManageActivity : AppCompatActivity() {

    lateinit var manage : FloatingActionButton
    lateinit var location: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_manage)
        manage=findViewById(R.id.UserManage)
        location=findViewById(R.id.location)
        manage.setOnClickListener {
            val intent = Intent(this@UserManageActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }
        location.setOnClickListener {
            val intent = Intent(this@UserManageActivity, MapActivity::class.java)
            startActivity(intent)
        }
    }
}