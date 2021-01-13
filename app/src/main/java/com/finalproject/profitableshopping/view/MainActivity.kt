package com.finalproject.profitableshopping.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import kotlinx.android.synthetic.main.activity_main.*
import com.finalproject.profitableshopping.view.category.CategoryFragment
import com.finalproject.profitableshopping.view.products.fragments.AddProductFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductDetailsFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductListFragment

class MainActivity : AppCompatActivity(), ProductListFragment.Callbacks,AddProductFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hide app bar elevation
        supportActionBar?.elevation = 0.0f

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, ProductListFragment.newInstance())
                .commit()
        }
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    setContent("Home")
                    setCurrentFragment(ProductListFragment.newInstance())
                    true
                }
                R.id.menu_shopping_cart -> {
//                    setContent("Cart")
//                    setCurrentFragment(productListFragment)
                    true
                }
                R.id.menu_search -> {
//                    setContent("Search")
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

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    private fun addFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }

    private fun setContent(content: String) {
        supportActionBar?.title = content;
    }

    override fun onItemSelected(itemId: Int) {
        addFragment(ProductDetailsFragment.newInstance(itemId.toString()))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_product -> {
                setCurrentFragment(AddProductFragment.newInstance())
                true
            }
            R.id.menu_my_products -> {
                setCurrentFragment(ProductListFragment.newInstance())
                true
            }
            R.id.menu_categories -> {
                setCurrentFragment(CategoryFragment.newInstance())
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSuccessAddProduct() {
        setCurrentFragment(ProductListFragment.newInstance())
    }


}