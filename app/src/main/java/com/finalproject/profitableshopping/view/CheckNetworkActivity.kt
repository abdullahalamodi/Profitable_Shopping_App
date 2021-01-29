package com.finalproject.profitableshopping.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.finalproject.profitableshopping.R
import kotlinx.android.synthetic.main.activity_check_network.*

class CheckNetworkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_network)

        btn_try_again.setOnClickListener {
            checkNetwork()
        }
    }

    private fun checkNetwork(){
        val connectionManger=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManger.activeNetworkInfo
        val isConnected:Boolean=activeNetwork?.isConnectedOrConnecting==true
        if (isConnected){
            val i = Intent(this@CheckNetworkActivity, MainActivity::class.java)
            startActivity(i)
            finish()
            Toast.makeText(this, "You connected Successful", Toast.LENGTH_LONG).show()
        }else{
            /*val i = Intent(this@CheckNetworkActivity, CheckNetworkActivity::class.java)
            startActivity(i)
            finish()*/
            Toast.makeText(this, "There is no network", Toast.LENGTH_LONG).show()
        }
    }
}