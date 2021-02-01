package com.finalproject.profitableshopping.view.authentication.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.firebase.Firebase
import com.finalproject.profitableshopping.view.AboutAppFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.*
import kotlinx.android.synthetic.main.fragment_manage_user.*
import java.io.File
import java.lang.Exception
import kotlin.coroutines.Continuation


private const val PICK_IMAGE_REQUEST = 0
class ShowProfileFragment : Fragment() {
   // var callbacks: Callbacks? = null
    lateinit var filePath: Uri
    var database1 :DatabaseReference?=null
    lateinit var firebaseStore: FirebaseStorage
    lateinit var storageReference: StorageReference

      //  .getReference("Users").child(AppSharedPreference.getUserId(requireContext())!!)
    lateinit var UserText1 : TextView
    lateinit var UserText2 : TextView
    lateinit var UserText3 : TextView
    lateinit var image: ImageView
    private var coverChecker : String ="cover"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database1 = FirebaseDatabase.getInstance().getReference()
            .child("Users").child(AppSharedPreference.getUserId(requireContext())!!)
        storageReference = FirebaseStorage.getInstance().getReference().child("Image/")
        try {
            var localfile: File = File.createTempFile("", "jpg")
            storageReference.getFile(localfile)
                .addOnSuccessListener(
                    object : OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                        override fun onSuccess(taskSnapshot: FileDownloadTask.TaskSnapshot?) {
                            Toast.makeText(requireContext(), " Picture Retrived ..... ", Toast.LENGTH_LONG).show()
                         var bitmap : Bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            image.setImageBitmap(bitmap)
                        }
                    }).addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        Toast.makeText(requireContext(), " Faild  ..... ", Toast.LENGTH_LONG).show()
                    }

                })

        }catch (e: Exception){
            e.printStackTrace()
        }
                arguments?.let {

                }

            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_show_profile, container, false)

        UserText1 = view.findViewById(R.id.UserT1)
        UserText2 = view.findViewById(R.id.UserT2)
        UserText3 = view.findViewById(R.id.UserT3)
        image=view.findViewById(R.id.image_preview)

//        var getData = object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                var sb = StringBuilder()
//                for (i in p0.children) {
//                    var Uname = i.child(AppSharedPreference.getUserId(requireContext())!!).child("userName")
//                        .getValue()
//                 //   var Uemail = i.child("Users")
//                    //    .child(AppSharedPreference.getUserId(requireContext())!!).child("userEmail")
//                   //     .getValue()
//                    //    var Upass = i.child("Users").
//                    //  child(AppSharedPreference.getUserId(requireContext())!!).child("userName")
//                    //  var Uname=i.child("uname").getValue()
//                    // var Upass=i.child("upass").getValue()
//                    //  var Uimage =i.child("cover").getValue()
//                    sb.append("$Uname")
//                    UserText1.setText(sb)
//                }
//             //  UserText1.setText(sb)
//                //   UserText2.setText(sb)
//                //  UserText3.setText(sb)
//
//
//                }
//
//            }
//        database1.addValueEventListener(getData)
//      database1.addListenerForSingleValueEvent(getData)


     return view

   }

    override fun onStart() {
        super.onStart()
        image.setOnClickListener {
            coverChecker = "cover"
            launchGallery()

        }
    }
    private fun launchGallery() {
        var intent = Intent(Intent.ACTION_GET_CONTENT).apply {
          setType("image/*")
      }
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    //  val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
        filePath = data?.data!!
        val bitmap = MediaStore.Images.Media.getBitmap(getActivity()?.getContentResolver(), filePath)
        image.setImageBitmap(bitmap)
        //  Toast.makeText(requireContext(), " Uploading ..... ", Toast.LENGTH_LONG).show()
        uploadImage()

    }
}
      private fun  uploadImage() {
//        val storageRef = FirebaseStorage.getInstance().getReference().child("Images/image.jpg")
//
//        storageRef.putFile(filePath).addOnCompleteListener {
//            if(it.isSuccessful){
//                Toast.makeText(requireContext(), " SuccessFul Uploading", Toast.LENGTH_LONG).show()
//            }
//            else{
//                Toast.makeText(requireContext(), " Faild Uploading ", Toast.LENGTH_LONG).show()
//            }
//        }
          if (filePath != null) {
              val fileRef = storageReference!!.child(System.currentTimeMillis().toString() +".jpg")
              var uploadTask : StorageTask<*>
              uploadTask =fileRef.putFile(filePath!!)
              uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                      task -> if(!task.isSuccessful)
              {
                  task.exception?.let {
                      throw  it
                  }
              }
                  return@Continuation fileRef.downloadUrl
              }).addOnCompleteListener {
                      task ->  if(task.isSuccessful){
                  val downloadUrl = task.result
                  val url = downloadUrl.toString()
                  if(coverChecker == "cover"){
                      val mapCoverImg = HashMap<String , Any>()
                      mapCoverImg["cover"] = url
                      database1!!.updateChildren(mapCoverImg)
                      coverChecker = "cover "

                  }
                  //  progressBar.dismiss()
              }
              }

              //  Toast.makeText(requireContext(), " SuccessFul ", Toast.LENGTH_LONG).show()
          }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database1?.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val saveUserInfo=snapshot.getValue(SaveUserInfo::class.java)
              //  UserText1.text=saveUserInfo?.profileImage

                UserText2.text=saveUserInfo?.userName
                UserText3.text=saveUserInfo?.userEmail
                UserText1.text=saveUserInfo?.userPhone

            }

        })
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

    interface Callbacks {
        fun onShowProfile(show: Boolean)
    }

  //  override fun onAttach(context: Context) {
     //   super.onAttach(context)
//        callbacks = context as ShowProfileFragment.Callbacks
 //   }

//    override fun onDetach() {
//        super.onDetach()
//        callbacks?.onShowProfile(true)
//        callbacks=null
//    }

    companion object {

        @JvmStatic
        fun newInstance() = ShowProfileFragment()
    }
}