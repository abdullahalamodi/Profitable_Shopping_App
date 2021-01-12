package com.finalproject.profitableshopping.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.finalproject.profitableshopping.R
import kotlinx.android.synthetic.main.activity_main.*
import com.finalproject.profitableshopping.view.category.CategoryFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductListFragment

class MainActivity : AppCompatActivity() ,ProductListFragment.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hide app bar elevation
        supportActionBar?.elevation = 0.0f

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, CategoryFragment.newInstance())
                .commit()
        }

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    setContent("Home")
                    true
                }
                R.id.menu_shopping_cart -> {
                    setContent("Cart")
                    true
                }
                R.id.menu_search -> {
                    setContent("Search")
                    true
                }
                R.id.menu_notification -> {
                    setContent("Profile")
                    true
                }
                R.id.menu_profile -> {
                    setContent("Profile")
                    true
                }
                else -> false
            }
        }
    }

    private fun setContent(content: String) {
        setTitle(content)
        // tvLabel.text = content
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, CategoryFragment.newInstance())
            .commit()
    }

    override fun onItemSelected(itemId: Int) {

    }
/*
    override fun onFloatButtonClicked() {

    }*/

}