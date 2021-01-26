package com.finalproject.profitableshopping.view.products

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.fragments.*

class ManageProductActivity : AppCompatActivity(),
    ManageProductsFragment.Callbacks,
    AddProductFragment.Callbacks,
    ProductListFragment.Callbacks{
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

    private fun addFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container_manage_product, fragment)
            addToBackStack(null)
            commit()
        }

    override fun onItemSelected(itemId: Int) {
        addFragment(DetailsOfAllProductsFragment.newInstance(itemId.toString()))
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_manage_product, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onUpdateProductClicked(productId: String?) {
        setFragment(AddProductFragment.newInstance(productId!!))
    }

    override fun onDeleteProductClicked() {
        setFragment(ManageProductsFragment.newInstance())
    }

    override fun onAddProductClicked() {
        setFragment(AddProductFragment.newInstance(null))
    }

    override fun onSuccessAddProduct() {
        setFragment(ManageProductsFragment.newInstance())
    }



}