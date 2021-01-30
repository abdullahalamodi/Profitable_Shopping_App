package com.finalproject.profitableshopping.view.authentication.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.finalproject.profitableshopping.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ShowProfileActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var imageView : ImageView
    lateinit var nameTV : TextView
    lateinit var emailTV : TextView
    lateinit var phoneTV : TextView
    lateinit var query: Query
    lateinit var ds: DataSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")
        imageView =findViewById(R.id.avaterIv)
        nameTV = findViewById(R.id.nameTV)
        emailTV =findViewById(R.id.emailTV)
        phoneTV = findViewById(R.id.PhoneTV)

        var getData = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for(ds in snapshot.children) {
                    var name: String = "" + ds.child("uname").getValue()
                    var pass: String = "" + ds.child("upass").getValue()
                    sb.append("${ds.key} $name $pass")
                }
                //  var phone :String = ""+ds.child("password").getValue()

                nameTV.setText(sb)
                emailTV.setText(sb)
                //phoneTV.setText(phone)

            }

        }
        reference.addValueEventListener(getData)
        reference.addListenerForSingleValueEvent(getData)




    }
}