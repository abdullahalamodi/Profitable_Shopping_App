package com.finalproject.profitableshopping.view.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.products.fragments.AdminProductManagmentFragment

class CategoryActivity : AppCompatActivity(), AddCategoryFragment.CallBacks,
CategoryFragment.CategoryCallbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            setFragment(CategoryFragment.newInstance())
        }

    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_category, fragment)
            .commit()
    }

    override fun onSuccess(fragment: AddCategoryFragment) {
        setFragment(CategoryFragment.newInstance())
    }

    override fun onOpenProductManager(catgoryId: Int) {
        setFragment(AdminProductManagmentFragment.newInstance(catgoryId))
    }
}