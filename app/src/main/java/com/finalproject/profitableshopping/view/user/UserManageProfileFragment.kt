package com.finalproject.profitableshopping.view.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.util.*

class UserManageProfileFragment : Fragment() {
/*
    lateinit var emailTv:TextView
    lateinit var phoneTv:TextView
    lateinit var isActiveTv:TextView*/
private val PICK_IMAGE_REQUEST = 0
    private var filePath: Uri? = null
    lateinit var image: ImageView
    lateinit var map : Button
    lateinit var reset: Button
    lateinit var changeInfo : Button
    var callbacks:Callbacks?=null

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  requireContext().supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       /* // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_user, container, false)
        emailTv = view.findViewById(R.id.user_email_tv)
        phoneTv = view.findViewById(R.id.user_phone_tv)
        isActiveTv = view.findViewById(R.id.user_isActive_tv)*/
        val view =  inflater.inflate(R.layout.fragment_user_profile, container, false)
        image=view.findViewById(R.id.image_preview)
        reset = view.findViewById(R.id.resetPassword)
        changeInfo =view.findViewById(R.id.changeInfo)
        updateUi()

        return view
    }

    override fun onStart() {
        super.onStart()
        reset.setOnClickListener {
            /*val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)*/
            callbacks?.onRestPasswordClicked()
        }


        changeInfo.setOnClickListener {
            /*val intent = Intent(this, UpdateInfoActivity::class.java)
            startActivity(intent)*/
            callbacks?.onUpdateClicked()
        }


        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
               // val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val bitmap=BitmapFactory.decodeFile(filePath!!.path)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            Toast.makeText(requireContext(), "SuccessFul", Toast.LENGTH_LONG).show()

        }
    }

    private fun updateUi(){
        /*val user = AppSharedPreference.getUserData(context!!)
        emailTv.text = "email : "+user?.email
        phoneTv.text = "phone :"+user?.phone
        isActiveTv.text = "i sActive :"+user?.isActive.toString()
        isActiveTv.text = "user id  :"+user?.id.toString()*/
    }
    interface Callbacks{
        fun onRestPasswordClicked()
        fun onUpdateClicked()
    }
    companion object {
        @JvmStatic
        fun newInstance() = UserManageProfileFragment()
    }
}