package com.finalproject.profitableshopping.view.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.firebase.NotifationActivity
import com.finalproject.profitableshopping.view.MainActivity
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.authentication.fragments.SaveUserInfo
import com.finalproject.profitableshopping.view.products.fragments.ManageProductsFragment
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.finalproject.profitableshopping.viewmodel.ReportViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.*
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.Exception

private const val PICK_IMAGE_REQUEST = 0
class ManageUserProfileFragment : Fragment() {

    lateinit var filePath: Uri
    lateinit var firebaseStore: FirebaseStorage
    lateinit var storageReference: StorageReference
    private var coverChecker : String ="cover"
    lateinit var image :ImageView

    lateinit var reportViewModel: ReportViewModel
    lateinit var manage: ImageView
    lateinit var location: TextView
    var userCountOfReport = 0
    lateinit var notifationM: ImageView
//save data in firebase
    var database1 : DatabaseReference?=null

    lateinit var UserText1 : TextView
    lateinit var UserText2 : TextView
    lateinit var UserText3 : TextView

    //////////////////////////////////
    private lateinit var userId: String
    private lateinit var productViewModel: ProductViewModel
    private lateinit var manageProductsRv: RecyclerView
    private lateinit var productSearchEt: EditText
    private lateinit var addProductBtn: Button
    private var openMoreOptions = true
    private var adapterManageProduct: ManageUserProfileFragment.ManageProductAdapter =
        ManageProductAdapter(emptyList())
    private lateinit var progressBar: ProgressBar

    ///////////////////////////////////
    var callbacks: Callbacks? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onStart() {
        super.onStart()
        //For LaunchGallery
        image.setOnClickListener {
            coverChecker = "cover"
            launchGallery()

        }
        reportViewModel.getUserReports(AppSharedPreference.getUserId(requireContext())!!).observe(
            this,
            Observer {
                userCountOfReport = it.size
                Log.d("user report", it.size.toString())
            }
        )
        manage.setOnClickListener {
            callbacks?.onOpenProfile()
        }
        location.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
        notifationM.setOnClickListener {
            val intent = Intent(requireContext(), NotifationActivity::class.java)
            startActivity(intent)

        }
    }
    private fun launchGallery() {
        var intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            setType("image/*")
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Show Profile :
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
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        //userId = AppSharedPreference.getUserId(context!!)!!

        //////////////////////////////
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        //save data in firebase
        database1= FirebaseDatabase.getInstance().getReference()
            .child("Users").child(AppSharedPreference.getUserId(requireContext())!!)
        arguments?.let {
        userId=it.getString("USERID")!!
        }
        productViewModel.refreshUserList(userId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_user, container, false)
        manage = view.findViewById(R.id.UserManage)
        location = view.findViewById(R.id.location)
        notifationM = view.findViewById(R.id.notification)

        //save Profile

        UserText1 = view.findViewById(R.id.UserT1)
        UserText2 = view.findViewById(R.id.UserT2)
        UserText3 = view.findViewById(R.id.UserT3)
        image=view.findViewById(R.id.image)

        ///////////////////////////
        manageProductsRv = view.findViewById(R.id.rv_my_product)
      //  progressBar = view.findViewById(R.id.progress_circular_manage_product)
        manageProductsRv.layoutManager = LinearLayoutManager(context)
        manageProductsRv.adapter = adapterManageProduct
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        showProgress(true)
        productViewModel.userProductsLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
             //   showProgress(false)
                updateUI(prodcts)
            })

        database1?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val saveUserInfo=snapshot.getValue(SaveUserInfo::class.java)
                UserText1.text=saveUserInfo?.userName
                UserText2.text=saveUserInfo?.userEmail
                UserText3.text=saveUserInfo?.userPhone
               // Picasso.with(context).load(image).into(image)

            }

        })
    }



    private fun updateUI(productsList: List<Product>) {
        adapterManageProduct = ManageProductAdapter(productsList)
        manageProductsRv.adapter = adapterManageProduct
    }


    private inner class ManageProductHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var manageProductImageIv = view.findViewById(R.id.img_manage_product) as ImageView
        var manageProductNameTv: TextView =
            view.findViewById(R.id.tv_name_manage_product) as TextView
        var manageProductRialPriceTv: TextView =
            view.findViewById(R.id.tv_price_manage_product) as TextView
        var manageProductMoreOptionIV: ImageView =
            view.findViewById(R.id.img_more_options_product) as ImageView

        var product = Product()

        init {
            view.setOnClickListener(this)
        }


        fun bind(pro: Product) {
            product = pro
            manageProductNameTv.text = pro.name
            manageProductRialPriceTv.text = "RY:" + pro.rialPrice.toString()
            manageProductMoreOptionIV.visibility = View.GONE

            if (product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = product.images[0].getUrl()
                    it.load(path)
                        .resize(45, 45)
                        .centerCrop()
                        .placeholder(R.drawable.shoe)
                        .into(manageProductImageIv)
                }
            } else {
                manageProductImageIv.setImageResource(R.drawable.shoe)
            }
        }

        override fun onClick(p0: View?) {
          //  callbacks?.onItemSelected(this.product.id)
        }
    }

    private inner class ManageProductAdapter(var productsList: List<Product>) :
        RecyclerView.Adapter<ManageProductHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProductHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.list_item_manage_product,
                parent, false
            )
            return ManageProductHolder(view)
        }

        override fun onBindViewHolder(holder: ManageProductHolder, position: Int) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }

        fun updateList(list: List<Product>) {
            productsList = list
            notifyDataSetChanged();
        }
    }


    interface Callbacks {

        fun onOpenProfile()
    }

    companion object {

        fun newInstance(userId:String?) = ManageUserProfileFragment().apply {
                arguments = Bundle().apply {
                  putString("USERID",userId)
                }
            }
    }
}