package com.finalproject.profitableshopping.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.finalproject.profitableshopping.R

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 4000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed(
            {
                checkNetwork()
            }, SPLASH_TIME_OUT
        ) // 3000 is the delayed time in milliseconds.
    }

    fun checkNetwork(){
        val connectionManger=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectionManger.activeNetworkInfo
        val isConnected:Boolean=activeNetwork?.isConnectedOrConnecting==true
        if (isConnected){
            val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(i)
            finish()
          //  Toast.makeText(this, "Network connection is available", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(this@SplashScreenActivity, CheckNetworkActivity::class.java)
            startActivity(i)
            finish()
            Toast.makeText(this, "Network connection is not available", Toast.LENGTH_SHORT).show()
        }
    }
}