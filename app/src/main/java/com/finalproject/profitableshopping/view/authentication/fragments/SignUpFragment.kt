package com.finalproject.profitableshopping.view.authentication.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.view.MainActivity
import com.finalproject.profitableshopping.viewmodel.FavoriteViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

private const val PICK_IMAGE_REQUEST = 0

class SignUpFragment : Fragment() {
    lateinit var filePath: Uri
    lateinit var image: ImageView
    lateinit var firebaseStore: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var userNameEt: EditText
    lateinit var userEmailEt: EditText
    lateinit var userPassEt: EditText
    lateinit var confrimPassEt: EditText
    lateinit var registerBtn: Button
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private var coverChecker : String ="cover"
   var database1 = FirebaseDatabase.getInstance().reference




    var signUpCallbacks: SignUpCallbacks? = null
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpCallbacks = context as SignUpCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
       // var database1 = FirebaseDatabase.getInstance().reference


        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onStart() {
        super.onStart()
        registerBtn.setOnClickListener {
            var userName = userNameEt.text.toString()
            var email = userEmailEt.text.toString()
            var password = userPassEt.text.toString()
            var confirmPassword = confrimPassEt.text.toString()
             var image : String = image.toString()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length > 6 && password.length < 14) {
                    if (password == confirmPassword) {
                        if (!isValidEmail(email)) {
                            Toast.makeText(requireContext(), "Please enter valid email", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        register(userName, email, password)



                    } else {
                        Toast.makeText(requireContext(), "password not equal confirmPassword", Toast.LENGTH_LONG).show()
                    }
                }

                else {
                    Toast.makeText(requireContext(), "Password length must be between 6 and 14 ", Toast.LENGTH_LONG).show()
                }
                database1.child("Users").setValue(SaveUserInfo(userName,email,password,image))
            } else {
                Toast.makeText(requireContext(), "some fields empty", Toast.LENGTH_LONG).show()
            }
        }
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


    private fun uploadImage() {
//        val storageRef = FirebaseStorage.getInstance().getReference().child("Images/image.jpg")
//
//            storageRef.putFile(filePath).addOnCompleteListener {
//               if(it.isSuccessful){
//                   Toast.makeText(requireContext(), " SuccessFul ", Toast.LENGTH_LONG).show()
//               }
//                else{
//                   Toast.makeText(requireContext(), " Faild ", Toast.LENGTH_LONG).show()
//               }
//            }

        if (filePath != null) {
            val fileRef = storageReference!!.child(System.currentTimeMillis().toString() +".jpg")
            var uploadTask : StorageTask<*>
            uploadTask =fileRef.putFile(filePath!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
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
                    coverChecker = " "

                }
            //  progressBar.dismiss()
            }
            }

          //  Toast.makeText(requireContext(), " SuccessFul ", Toast.LENGTH_LONG).show()
        }
            //val progressBar = ProgressDialog(context)
         //   progressBar.setMessage("Image is Uploading , Please wait......")
          //  progressBar.show()
        //Toast.makeText(requireContext(), " SuccessFul ", Toast.LENGTH_LONG).show()
        Toast.makeText(requireContext(), " Uploading ..... ", Toast.LENGTH_LONG).show()
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        userNameEt = view.findViewById(R.id.register_user_name)
        userEmailEt = view.findViewById(R.id.register_email)
        userPassEt = view.findViewById(R.id.register_password)
        confrimPassEt = view.findViewById(R.id.register_confirm_password)
        registerBtn = view.findViewById(R.id.btn_register)
        image=view.findViewById(R.id.image_preview)

        return view
    }

    override fun onDetach() {
        super.onDetach()
        signUpCallbacks = null
    }

    private fun register(userName: String, email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
       // reference = database.getReference("users")
      //  reference.child("username").setValue(userName)
      //  reference.child("email").setValue(email)
      //  reference.child("password").setValue(password)


        var p = ProgressDialog(requireContext())
        p.setMessage("please wait")
        p.setCanceledOnTouchOutside(false)
        p.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                p.dismiss()
                if (it.isSuccessful) {
                    sendEmailVerification()
                    // create favorite for user
/*                    favoriteViewModel.createFavorite(
                        Favorite(
                            null,
                            AppSharedPreference.getUserId(requireContext())!!
                        )
                    ).observe(
                        this,
                        Observer {
                            AppSharedPreference.setFavoriteId(requireContext(), it)
                        }
                    )*/
                } else {
                    Toast.makeText(requireContext(), " You registered Failed ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "You registered successful", Toast.LENGTH_LONG)
                    .show()
                 var intent = Intent(requireContext(), MainActivity::class.java)
                 startActivity(intent)
                signUpCallbacks?.onCreateAccountSuccess()
            }
        }
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {

            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    interface SignUpCallbacks {
        fun onCreateAccountSuccess()
    }

    companion object {

        @JvmStatic
        fun newInstance() = SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}