package com.finalproject.profitableshopping.view.products

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.products.fragments.AddProductFragment
import com.finalproject.profitableshopping.view.products.fragments.ManageProductsFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductDetailsFragment

class ManageProductActivity : AppCompatActivity(), ManageProductsFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_prudect)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_manage_product, ManageProductsFragment())
                .commit()
        }
    }

    override fun onItemSelected(itemId: Int) {
        val fragment = ProductDetailsFragment.newInstance(itemId.toString())
        val fm = supportFragmentManager
        fm?.beginTransaction().replace(R.id.container_manage_product, fragment).addToBackStack(null)
            .commit();
    }

}