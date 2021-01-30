package com.finalproject.profitableshopping.view.authentication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_manage_user.*

class ShowProfileFragment : Fragment() {


    var database1 = FirebaseDatabase.getInstance().reference
    lateinit var UserText1 : TextView
    //lateinit var UserText2 : TextView
    //lateinit var UserText3 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_show_profile, container, false)
        UserText1 = view.findViewById(R.id.UserT1)
     //   UserText2 = view.findViewById(R.id.UserT2)
      //  UserText3 = view.findViewById(R.id.UserT3)

        var getData = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var sb = StringBuilder()
                for(i in p0.children) {
                    var Uemail = i.child("uemail").getValue()
                    var Uname=i.child("uname").getValue()
                    var Upass=i.child("upass").getValue()
                    var Uimage =i.child("cover").getValue()
                    sb.append("${i.key} $Uemail\n  $Uname \n $Upass\n $Uimage\n")

                }
                UserText1.setText(sb)
             //   UserText2.setText(sb)
              //  UserText3.setText(sb)


                }

            }
        database1.addValueEventListener(getData)
        database1.addListenerForSingleValueEvent(getData)
        return view

    }




//    private fun checkCurrentUser() {
//        // [START check_current_user]
//         user = firebaseAuth.currentUser!!
//        if (user != null) {
//            // User is signed in
//        } else {
//            // No user is signed in
//        }
//        // [END check_current_user]
//    }

    companion object {

        @JvmStatic
        fun newInstance() = ShowProfileFragment()
    }
}