package com.finalproject.profitableshopping.view.report.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.Complain
import com.finalproject.profitableshopping.data.models.Report
import com.finalproject.profitableshopping.viewmodel.ComplainViewModel
import com.finalproject.profitableshopping.viewmodel.ReportViewModel

class ComplainDialog :DialogFragment(){
  lateinit var complainRv:RecyclerView
    lateinit var complainViewModel:ComplainViewModel
    lateinit var reportViewModel: ReportViewModel
   lateinit var adapter:ComplainAdapter
    var complainId:Int=0
    var productId:String=""
    var userId:String=""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments.let {
            productId= it?.getString("PROID")!!
            userId=it?.getString("UID")!!
        }
        complainViewModel= ViewModelProviders.of(this).get(ComplainViewModel::class.java)
        reportViewModel= ViewModelProviders.of(this).get(ReportViewModel::class.java)
        val view = activity?.layoutInflater?.inflate(R.layout.complain_dialog,null)
        complainRv= view!!.findViewById(R.id.complain_recycler_view)
        complainRv.layoutManager=LinearLayoutManager(requireContext())
        complainRv.adapter=ComplainAdapter(complainViewModel.comp)
        complainViewModel.complainListLiveData.observe(
            this,
            Observer {

               // adapter=ComplainAdapter(it)
               // adapter=ComplainAdapter(complainViewModel.comp)
                Log.d("coplain list", complainViewModel.comp.size.toString())
            }
        )

        return AlertDialog
            .Builder(requireContext(),R.style.AppTheme)
            .setView(view)
            .setTitle("ما هي شكواك")
            .setPositiveButton("ارسال"){dialog,_ ->
             val report=Report(
                 id=null,
                 complainId = complainId,
                 fromId = AppSharedPreference.getUserId(requireContext())!!,
                 to_id =userId,
                 productId = productId,
                 date = "22-1-2021"

             )
                reportViewModel.addReport(report).observe(
                    this,
                    Observer {
                        Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                    }
                )
                dialog.dismiss()

            }.setNegativeButton("الغاء"){dialog,_ ->
                dialog.dismiss()

            }
            .create()
    }

    inner class ComplainAdapter(val complainList:List<Complain>): RecyclerView.Adapter<ComplainHoldrer>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainHoldrer {
          val view=layoutInflater.inflate(R.layout.complain_list_item,parent,false)
            return ComplainHoldrer(view)
        }

        override fun onBindViewHolder(holder: ComplainHoldrer, position: Int) {
            val complain=complainList[position]
            Log.d("complain title",complainList.size.toString())
            holder.bind(complain)
        }

        override fun getItemCount(): Int {
           return complainList.size
        }
    }

    inner class ComplainHoldrer(view: View):RecyclerView.ViewHolder(view),View.OnClickListener{

        val complainCB=view.findViewById<CheckBox>(R.id.complain_check_box)
        var complainTitleTv=view.findViewById<TextView>(R.id.complain_title_tv)
        var complain=Complain(null)
        init {
            view.setOnClickListener(this)
        }
        fun bind(complain:Complain){
            this.complain=complain
            complainTitleTv.text=complain.title


        }
        override fun onClick(p0: View?) {
            complainCB.isChecked=true
            complainId= this.complain.id!!

        }

    }
    companion object {
        fun newInstance(productId:String,userId:String):ComplainDialog {


            return ComplainDialog().apply {
                arguments=Bundle().apply {
                    putString("PROID",productId)
                    putString("UID",userId)
                }
            }
        }
    }
}