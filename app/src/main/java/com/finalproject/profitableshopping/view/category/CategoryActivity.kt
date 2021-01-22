package com.finalproject.profitableshopping.view.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.finalproject.profitableshopping.R

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_category,CategoryFragment.newInstance())
                .commit()
        }
    }
}