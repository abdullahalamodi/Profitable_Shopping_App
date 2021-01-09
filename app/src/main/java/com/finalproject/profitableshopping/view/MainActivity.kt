package com.finalproject.profitableshopping.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.categorty.CategoryCrudFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            val fragmentManager=supportFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, CategoryCrudFragment.newInstance())
                .commit()
        }
    }
}