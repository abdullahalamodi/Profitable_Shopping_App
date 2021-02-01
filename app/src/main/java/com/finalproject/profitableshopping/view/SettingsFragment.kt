package com.finalproject.profitableshopping.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.firebase.NotifationActivity
import com.finalproject.profitableshopping.view.authentication.fragments.LogInFragment
import com.finalproject.profitableshopping.view.authentication.fragments.ShowProfileFragment
import com.finalproject.profitableshopping.view.category.CategoryActivity
import com.finalproject.profitableshopping.view.products.ManageProductActivity
import com.finalproject.profitableshopping.view.report.dialog.AddComplainDialog
import com.finalproject.profitableshopping.view.user.ManageUserProfileFragment
import com.finalproject.profitableshopping.view.user.UserManageProfileFragment


class SettingsFragment : Fragment(),
    UserManageProfileFragment.Callbacks,
    ManageUserProfileFragment.Callbacks,
    ShowProfileFragment.Callbacks,
    AboutAppFragment.Callbacks,
    ContactUsFragment.Callbacks,
    LogInFragment.LoginCallbacks {

    private lateinit var aList: ListView
    private lateinit var uList: ListView

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onSettingsOpen(show: Boolean)
        fun onContactUsSelected()
        fun onAboutAppSelected()
        fun onMyPurchaseSelected()
        fun onMySalesSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
        callbacks?.onSettingsOpen(false)
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

        val adminImage = arrayOf<Int>(
            R.drawable.ic_category, R.drawable.ic_notifications, R.drawable.ic_reset,
            R.drawable.ic_contact, R.drawable.ic_phone_android,
            R.drawable.logout
        )

        val userImage = arrayOf<Int>(
            R.drawable.ic_my_products, R.drawable.ic_orders, R.drawable.ic_sells,
            R.drawable.ic_contact_us, R.drawable.ic_phone_android,
            R.drawable.logout
        )

        var adminAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, adminList)
        var userAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, userList)

        //    aList.adapter = adminAdapter
        //   uList.adapter = userAdapter

        var aAdapter = MyListAdapter(this, adminList, adminImage)
        aList.adapter = aAdapter

        var uAdapter = MyListAdapter(this, userList, userImage)
        uList.adapter = uAdapter


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
                        Toast.makeText(context, "you are not admin", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        addFragment(LogInFragment.newInstance())
                    }
                }


            } else if (position == 1) {
                AddComplainDialog.newInstance().apply {
                    show(this@SettingsFragment.parentFragmentManager, "report")
                }
            } else if (position == 2) {
                startActivity(Intent(context, NotifationActivity::class.java))
            } else if (position == 3) {
                addFragment(ContactUsFragment())
            } else if (position == 4) {
                addFragment(AboutAppFragment())
            } else if (position == 5) {
                logOut()
                setCurrentFragment(LogInFragment.newInstance())
            }

        }

        uList.setOnItemClickListener { parent, view, position, id ->

            if (position == 0) {
                //addFragment(AboutAppFragment())
                startActivity(Intent(context, ManageProductActivity::class.java))


            } else if (position == 1) {
                callbacks?.onMyPurchaseSelected()

            } else if (position == 2) {
                callbacks?.onMySalesSelected()

            } else if (position == 3) {
                callbacks?.onContactUsSelected()

            } else if (position == 4) {
                callbacks?.onAboutAppSelected()


            } else if (position == 5) {
                logOut()
                setCurrentFragment(LogInFragment.newInstance())

            }
//            else if (position == 6) {
//
//            }

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
                .addToBackStack(null)
                .commit()
        }


    private fun getUserToken(): String? {
        return AppSharedPreference.getUserToken(requireContext())
    }

    private fun logOut(token: String? = null) {
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

    override fun onShowProfile(show: Boolean) {


    }

    inner class MyListAdapter(
        private val context: SettingsFragment,
        private val title: Array<String>,
        private val imgid: Array<Int>
    ) : ArrayAdapter<String>(requireContext(), R.layout.list_item_settings, title) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {

            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.list_item_settings, null, true)

            val titleText = rowView.findViewById(R.id.tv_title_settings) as TextView
            val imageView = rowView.findViewById(R.id.img_settings) as ImageView

            titleText.text = title[position]
            imageView.setImageResource(imgid[position])

            return rowView
        }

    }

    inner class UserListAdapter(
        private val context: SettingsFragment,
        private val title: Array<String>,
        private val imgid: Array<Int>
    ) : ArrayAdapter<String>(requireContext(), R.layout.list_item_settings, title) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {

            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.list_item_settings, null, true)

            val titleText = rowView.findViewById(R.id.tv_title_settings) as TextView
            val imageView = rowView.findViewById(R.id.img_settings) as ImageView

            titleText.text = title[position]
            imageView.setImageResource(imgid[position])

            return rowView
        }
    }
}
