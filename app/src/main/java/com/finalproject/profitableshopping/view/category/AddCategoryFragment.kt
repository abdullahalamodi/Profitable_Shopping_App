package com.finalproject.profitableshopping.view.category


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.getFileName
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.UploadRequestBody
import com.finalproject.profitableshopping.view.products.fragments.AddProductFragment
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddCategoryFragment : BottomSheetDialogFragment(), UploadRequestBody.UploadCallback {

    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private lateinit var loadBtn: ImageView
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var progressBar: ProgressBar
    private var selectedImageUri: Uri? = null


    override fun onStart() {
        super.onStart()

        addBtn.setOnClickListener {
            showProgress(true)
            val catMap = Category();
            catMap.name = categoryNameEt.text.toString()
            val response = categoryViewModel.addCategory(catMap)
            //will display message after get response
            response.observe(
                viewLifecycleOwner,
                Observer { id ->
                    showProgress(false)
                    uploadImage(id).observe(
                        viewLifecycleOwner,
                        Observer {
                            showProgress(false)
                            categoryViewModel.refresh()
                        }
                    )
                }
            )
        }

        loadBtn.setOnClickListener {
            pickImages()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_category, container, false)
        categoryNameEt = view.findViewById(R.id.category_name_et)
        addBtn = view.findViewById(R.id.add_category_btn)
        loadBtn = view.findViewById(R.id.load_image_btn)
        progressBar = view.findViewById(R.id.progress_circular)
        return view;
    }


    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun pickImages() {
        openImageChooser()
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, AddProductFragment.REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AddProductFragment.REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    loadBtn.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage(productId: String): MutableLiveData<String> {
        val responseLiveData = MutableLiveData<String>()
        if (selectedImageUri == null) {
            context?.showMessage("Select an Image First")
            responseLiveData.value = ""
            return responseLiveData
        }
        showProgress(true)
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null)
                ?: return responseLiveData
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =
            File(context?.cacheDir, context?.contentResolver?.getFileName(selectedImageUri!!)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        categoryViewModel.uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), productId)
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                context?.showMessage(t.message!!)
                progressBar.progress = 0
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    context?.showMessage(it)
                    progressBar.progress = 100
                    showProgress(false)
                }
            }

        })
        responseLiveData.value = ""
        return responseLiveData
    }


    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101

        @JvmStatic
        fun newInstance() = AddCategoryFragment()

    }
}