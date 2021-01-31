package com.finalproject.profitableshopping.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.view.MySales.MySalesFragment
import com.finalproject.profitableshopping.view.authentication.fragments.*
import com.finalproject.profitableshopping.view.cart.CartFragment
import com.finalproject.profitableshopping.view.category.CategoryActivity
import com.finalproject.profitableshopping.view.favorite.FavoriteFragment
import com.finalproject.profitableshopping.view.products.ManageProductActivity
import com.finalproject.profitableshopping.view.products.OrderDetailsFragment
import com.finalproject.profitableshopping.view.products.fragments.DetailsOfAllProductsFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductListFragment
import com.finalproject.profitableshopping.view.products.fragments.ShowAllProductsFragment
import com.finalproject.profitableshopping.view.purshases.ProchasesFragment
import com.finalproject.profitableshopping.view.user.ManageUserProfileFragment
import com.finalproject.profitableshopping.view.user.RestPasswordFragment
import com.finalproject.profitableshopping.view.user.UpdateInfoFragment
import com.finalproject.profitableshopping.view.user.UserManageProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),
    ProductListFragment.Callbacks,
    LogInFragment.LoginCallbacks,
    SignUpFragment.SignUpCallbacks,
    ProchasesFragment.Callbacks,
    CartFragment.Callbacks,
    ActiveFragment.ActiveAccountCallbacks,
    ShowAllProductsFragment.Callbacks,
    DetailsOfAllProductsFragment.Callbacks,
    UserManageProfileFragment.Callbacks,
    ManageUserProfileFragment.Callbacks,
    AboutAppFragment.Callbacks,
    ContactUsFragment.Callbacks,
    SettingsFragment.Callbacks,
   MySalesFragment.Callbacks{

  //  private lateinit var menu: Menu
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottomNav)


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

         supportActionBar?.hide()


        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, ProductListFragment.newInstance())
                .addToBackStack(null)
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
                    //if(getUserToken() != "admin" && getUserToken() !=null))
                    if(getUserToken()=="user") {
                        setCurrentFragment(CartFragment.newInstance())
                    }else{
                        setCurrentFragment(LogInFragment.newInstance())
                    }

                    true
                }

                /* R.id.menu_search -> {
 //                    setContent("Search")
                     setCurrentFragment(ShowAllProductsFragment.newInstance())
                     true
                 }*/
                R.id.menu_notification -> {
                    //if(getUserToken() != "admin" && getUserToken() != null)){
                    if(getUserToken()=="user") {
                        setContent("Favorites")
                        setCurrentFragment(FavoriteFragment.newInstance())
                    }else{
                        setCurrentFragment(LogInFragment.newInstance())
                    }
                    //0}
                    true
                }
                R.id.menu_settings -> {
                    setContent("Settings")
                    addFragment(SettingsFragment())
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

    private fun addCartBudge(count: Int) {
        if (count > 0) {
            val budge = bottomNav.getOrCreateBadge(R.id.menu_shopping_cart)
            budge.number = count
            budge.isVisible = true
        } else {
            bottomNav.removeBadge(R.id.menu_shopping_cart)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
                addToBackStack(null)
                commit()

        }

    private fun setContent(content: String) {
        supportActionBar?.title = content
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        filterMenuItems(menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    private fun filterMenuItems(menu: Menu) {
        if (getUserToken() == "admin") {
            menu.findItem(R.id.menu_login).isVisible = false
            menu.findItem(R.id.menu_categories).isVisible = true
            menu.findItem(R.id.menu_product_manage).isVisible = true
            menu.findItem(R.id.menu_my_products).isVisible = false
            menu.findItem(R.id.sign_out).isVisible = true
        } else if (getUserToken() == "user") {
            menu.findItem(R.id.menu_login).isVisible = false
            menu.findItem(R.id.menu_categories).isVisible = false
            menu.findItem(R.id.menu_product_manage).isVisible = false
            menu.findItem(R.id.menu_my_products).isVisible = true
            menu.findItem(R.id.sign_out).isVisible = true
        } else {
            menu.findItem(R.id.menu_login).isVisible = true
            menu.findItem(R.id.menu_categories).isVisible = false
            menu.findItem(R.id.menu_product_manage).isVisible = false
            menu.findItem(R.id.menu_my_products).isVisible = false
            menu.findItem(R.id.sign_out).isVisible = false
        }
    }

  /*  override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                //setCurrentFragment(AdminProductManagmentFragment.newInstance(1))
                true
            }

            R.id.menu_purchase -> {
                setCurrentFragment(ProchasesFragment.newInstance())
                showButtonNavigation(false)
                true
            }

            R.id.sign_out -> {
                logOut()
                setCurrentFragment(LogInFragment.newInstance())
                showButtonNavigation(false)
                true
            }

            R.id.menu_about -> {
                showButtonNavigation(false)
                addFragment(AboutAppFragment())

                true
            }
            R.id.contact_us -> {
                showButtonNavigation(false)
                addFragment(ContactUsFragment())

                true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }*/


    private fun getUserToken(): String? {
        return AppSharedPreference.getUserToken(this)
    }

    private fun getUserState(): Boolean {
        return AppSharedPreference.isActive(this)
    }

    private fun logOut(token: String = "") {
        AppSharedPreference.saveUserToken(this, token)
       // filterMenuItems(menu)
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

    override fun onCartBudgeRefresh(count: Int) {
        addCartBudge(count)
    }

    override fun onSignUpClicked() {
        setCurrentFragment(SignUpFragment.newInstance())
    }

    override fun onLoginSuccess() {
        setCurrentFragment(ProductListFragment.newInstance())
        showButtonNavigation(true)
       // filterMenuItems(menu)
    }


    override fun onCreateAccountSuccess() {
        setCurrentFragment(SignUpFragment.newInstance())
    }

    override fun onActiveAccount() {
        setCurrentFragment(ActiveUserAccountFragment.newInstance())
    }




    private var isNavHide = false
    fun anim(hide: Boolean) {
        if (isNavHide && hide || !isNavHide && !hide) return
        isNavHide = hide
        val moveY = if (hide) 2 * bottomNav!!.height else 0
        bottomNav!!.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300)
            .start()
    }

    override fun onAddToCartClicked() {

    }

    override fun onDetailsOpen(show: Boolean) {
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

    override fun onAboutAppOpen(show: Boolean) {
        //setCurrentFragment(AboutAppFragment())
        showButtonNavigation(show)
    }

    override fun onContactUsOpen(show: Boolean) {
        //setCurrentFragment(AboutAppFragment())
        showButtonNavigation(show)
    }

    override fun onSettingsOpen(show: Boolean) {
        showButtonNavigation(show)
    }

    override fun onContactUsSelected() {
       setCurrentFragment(ContactUsFragment.newInstance())
    }

    override fun onAboutAppSelected() {
        setCurrentFragment(AboutAppFragment.newInstance())
    }

    override fun onMyPurchaseSelected() {
        setCurrentFragment(ProchasesFragment.newInstance())
    }

    override fun onMySalesSelected() {
        setCurrentFragment(MySalesFragment.newInstance())
    }

    override fun onOpenOrderDatails(orderId: Int) {
        setCurrentFragment(OrderDetailsFragment.newInstance(orderId))
    }

    override fun onOpen(show: Boolean) {
       showButtonNavigation(show)
    }

    override fun clearBudge() {
        bottomNav.removeBadge(R.id.menu_shopping_cart)
    }

    /*override fun onBackPressed() {
    //    super.onBackPressed()
        var bottomSheetAddCat = ExitAppFragment();
        bottomSheetAddCat.show(supportFragmentManager,"Tag")
    }*/
}