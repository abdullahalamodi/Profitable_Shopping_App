package com.finalproject.profitableshopping.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.firebase.NotifactionFragment
import com.finalproject.profitableshopping.data.firebase.NotifationActivity
import com.finalproject.profitableshopping.view.authentication.fragments.*
import com.finalproject.profitableshopping.view.cart.CartFragment
import com.finalproject.profitableshopping.view.category.CategoryActivity
import com.finalproject.profitableshopping.view.products.ManageProductActivity
import com.finalproject.profitableshopping.view.products.fragments.*
import com.finalproject.profitableshopping.view.user.*
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    ProductListFragment.Callbacks,
    LogInFragment.LoginCallbacks,
    SignUpFragment.SignUpCallbacks,
    ActiveFragment.ActiveAccountCallbacks,
    ShowAllProductsFragment.Callbacks,
    DetailsOfAllProductsFragment.Callbacks,
UserManageProfileFragment.Callbacks,
ManageUserProfileFragment.Callbacks{

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hide app bar elevation
        supportActionBar?.elevation = 0.0f

        //  val nested_content =findViewById<View>(R.id.container) as NestedScrollView
/*        buttonNav = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        val n =findViewById<View>(R.id.container)
       n.setOnScrollChangeListener() { v, X, Y, oldScrollX, oldScrollY ->
            if ( Y < oldScrollY) {
                anim(false)
            }
            if ( Y > oldScrollY) {
                anim(true)
            }
        }*/

        // supportActionBar?.hide()


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
                    showButtonNavigation(true)
                    true
                }
                R.id.menu_shopping_cart -> {
//                    setContent("Cart")
                    setCurrentFragment(CartFragment.newInstance())
                    true
                }
                R.id.menu_search -> {
//                    setContent("Search")
                    setCurrentFragment(ShowAllProductsFragment.newInstance())
                    true
                }
                R.id.menu_notification -> {
                    val i = Intent(this@MainActivity, NotifationActivity::class.java)
                    startActivity(i)
                   // setContent("Profile")
                   // setCurrentFragment(NotifactionFragment.newInstance())
                    true
                }
                R.id.menu_profile -> {
                    setContent("Profile")
                    setCurrentFragment(ShowProfileFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    private fun showButtonNavigation(show: Boolean) {
        if (show)
            bottomNav.visibility = View.VISIBLE
        else
            bottomNav.visibility = View.GONE
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()

        }

    private fun setContent(content: String) {
        supportActionBar?.title = content;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        filterMenuItems(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterMenuItems(menu: Menu){
        if (getUserToken() == "admin") {
            menu.findItem(R.id.menu_login).setVisible(false)
            menu.findItem(R.id.menu_categories).setVisible(true)
            menu.findItem(R.id.menu_product_manage).setVisible(true)
            menu.findItem(R.id.menu_my_products).setVisible(false)
            menu.findItem(R.id.sign_out).setVisible(true)
        } else if (getUserToken() == "user") {
            menu.findItem(R.id.menu_login).setVisible(false)
            menu.findItem(R.id.menu_categories).setVisible(false)
            menu.findItem(R.id.menu_product_manage).setVisible(false)
            menu.findItem(R.id.menu_my_products).setVisible(true)
            menu.findItem(R.id.sign_out).setVisible(true)
        } else {
            menu.findItem(R.id.menu_login).setVisible(true)
            menu.findItem(R.id.menu_categories).setVisible(false)
            menu.findItem(R.id.menu_product_manage).setVisible(false)
            menu.findItem(R.id.menu_my_products).setVisible(false)
            menu.findItem(R.id.sign_out).setVisible(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.menu_add_product -> {
//                if (getUserToken() != null) {
////                    if (getUserState())
//                    setCurrentFragment(AddProductFragment.newInstance(null))
////                    else {
////                        setCurrentFragment(ActiveFragment.newInstance())
////                    }
//                } else {
//                    setCurrentFragment(LogInFragment.newInstance())
//                }
//                true
//            }
            R.id.menu_login -> {
                setCurrentFragment(LogInFragment.newInstance())
                showButtonNavigation(false)
                true
            }
            R.id.menu_my_products -> {
                startActivity(Intent(this, ManageProductActivity::class.java))
                true
            }
            R.id.menu_categories -> {
                when {
                    getUserToken() == "admin"
                        // setCurrentFragment(CategoryFragment.newInstance())
                    -> startActivity(Intent(this, CategoryActivity::class.java))
                    getUserToken() != "quest" -> {
                        Toast.makeText(this, "yor are not admin", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        setCurrentFragment(LogInFragment.newInstance())
                    }
                }
                true
            }
            R.id.menu_product_manage -> {
                setCurrentFragment(AdminProductManagmentFragment.newInstance())
                true
            }

            R.id.sign_out -> {
                logOut()
              setCurrentFragment(LogInFragment.newInstance())
                showButtonNavigation(false)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun getUserToken(): String? {
        return AppSharedPreference.getUserToken(this)
    }

    private fun getUserState(): Boolean {
        return AppSharedPreference.isActive(this)
    }

    private fun logOut(token: String = "") {
        AppSharedPreference.saveUserToken(this, token)
        filterMenuItems(menu)
    }


    private fun addFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }

    override fun onItemSelected(itemId: Int) {
        addFragment(DetailsOfAllProductsFragment.newInstance(itemId.toString()))
    }

    override fun onSignUpClicked() {
        setCurrentFragment(SignUpFragment.newInstance())
    }

    override fun onLoginSuccess() {
        setCurrentFragment(ProductListFragment.newInstance())
        showButtonNavigation(true)
        filterMenuItems(menu)
    }


    override fun onCreateAccountSuccess() {
        setCurrentFragment(SignUpFragment.newInstance())
    }

    override fun onActiveAccount() {
        setCurrentFragment(ActiveUserAccountFragment.newInstance())
    }




    private var isNavHide = false
    public fun anim(hide: Boolean) {
        if (isNavHide && hide || !isNavHide && !hide) return
        isNavHide = hide
        val moveY = if (hide) 2 * bottomNav!!.height else 0
        bottomNav!!.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300)
            .start()
    }

    override fun onAddToCartClicked() {

    }

    override fun onDetailsOpen(show:Boolean) {
       showButtonNavigation(show)
    }

    override fun onRestPasswordClicked() {
       setCurrentFragment(RestPasswordFragment.newInstance())
    }



    override fun onUpdateClicked() {
        setCurrentFragment(UpdateInfoFragment.newInstance())
    }

    override fun onOpenProfile() {
        setCurrentFragment(UserManageProfileFragment.newInstance())
    }
}