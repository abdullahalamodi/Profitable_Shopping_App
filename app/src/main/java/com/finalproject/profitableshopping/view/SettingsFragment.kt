package com.finalproject.profitableshopping.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.view.authentication.fragments.ActiveFragment
import com.finalproject.profitableshopping.view.authentication.fragments.LogInFragment
import com.finalproject.profitableshopping.view.authentication.fragments.SignUpFragment
import com.finalproject.profitableshopping.view.category.CategoryActivity
import com.finalproject.profitableshopping.view.products.ManageProductActivity
import com.finalproject.profitableshopping.view.products.fragments.DetailsOfAllProductsFragment
import com.finalproject.profitableshopping.view.products.fragments.ProductListFragment
import com.finalproject.profitableshopping.view.products.fragments.ShowAllProductsFragment
import com.finalproject.profitableshopping.view.user.ManageUserProfileFragment
import com.finalproject.profitableshopping.view.user.UserManageProfileFragment


class SettingsFragment : Fragment(),
    UserManageProfileFragment.Callbacks,
    ManageUserProfileFragment.Callbacks,
    AboutAppFragment.Callbacks,
    ContactUsFragment.Callbacks,
    LogInFragment.LoginCallbacks{

    private lateinit var aList: ListView
    private lateinit var uList: ListView

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onSettingsOpen(show: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks?.onSettingsOpen(true)
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_settings, container, false)


        var adminList = resources.getStringArray(R.array.admin_list)
        var userList = resources.getStringArray(R.array.user_list)

        aList = view.findViewById<ListView>(R.id.list_of_admin)
        uList = view.findViewById<ListView>(R.id.list_of_user)

        var adminAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, adminList)
        var userdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, userList)

        aList.adapter = adminAdapter
        uList.adapter = userdapter

        if (getUserToken() == "admin") {
            aList.visibility = View.VISIBLE
            uList.visibility = View.GONE
        } else if (getUserToken() == "user") {
            aList.visibility = View.GONE
            uList.visibility = View.VISIBLE
        } else {
            setCurrentFragment(LogInFragment.newInstance())
        }

        aList.setOnItemClickListener { parent, view, position, id ->

            if (position == 0) {
                when {
                    getUserToken() == "admin"
                    -> startActivity(Intent(context, CategoryActivity::class.java))
                    getUserToken() != "quest" -> {
                        Toast.makeText(context, "yor are not admin", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        addFragment(LogInFragment.newInstance())
                    }
                }

            } else if (position == 1) {


            } else if (position == 2) {

            } else if (position == 3) {

            } else if (position == 4) {

            } else if (position == 5) {
                addFragment(ContactUsFragment())
            } else if (position == 6) {
                addFragment(AboutAppFragment())
            } else if (position == 7) {
                logOut()
                setCurrentFragment(LogInFragment.newInstance())
            }

        }

        uList.setOnItemClickListener { parent, view, position, id ->

            if (position == 0) {
                addFragment(AboutAppFragment())

            } else if (position == 1) {
                startActivity(Intent(context, ManageProductActivity::class.java))

            } else if (position == 2) {

            } else if (position == 3) {
                addFragment(ContactUsFragment())

            } else if (position == 4) {
                addFragment(AboutAppFragment())

            } else if (position == 5) {

            } else if (position == 6) {
                logOut()
                setCurrentFragment(LogInFragment.newInstance())
            }

        }

        return view
    }


    private fun addFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.settings_container, fragment)
            addToBackStack(null)
            commit()
        }

    private fun setCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.container, fragment)
            commit()
        }


    private fun getUserToken(): String? {
        return AppSharedPreference.getUserToken(requireContext())
    }

    private fun logOut(token: String = "") {
        AppSharedPreference.saveUserToken(requireContext(), token)
    }

    override fun onRestPasswordClicked() {

    }

    override fun onUpdateClicked() {
    }

    override fun onOpenProfile() {
    }

    override fun onAboutAppOpen(show: Boolean) {
    }

    override fun onContactUsOpen(show: Boolean) {
    }

    override fun onSignUpClicked() {

    }

    override fun onLoginSuccess() {
    }
}