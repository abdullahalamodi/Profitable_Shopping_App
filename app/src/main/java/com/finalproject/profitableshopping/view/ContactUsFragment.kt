package com.finalproject.profitableshopping.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.view.products.fragments.ProductListFragment
import kotlinx.android.synthetic.main.fragment_contact_us.*


class ContactUsFragment : Fragment() {

    var callbacks: Callbacks? = null
    private var open = true
    private lateinit var memberDetailsTv1: TextView
    private lateinit var memberDetailsTv2: TextView
    private lateinit var memberDetailsTv3: TextView
    private lateinit var memberDetailsTv4: TextView
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var cardView3: CardView
    private lateinit var cardView4: CardView
    private lateinit var imgMore1: ImageView
    private lateinit var imgMore2: ImageView
    private lateinit var imgMore3: ImageView
    private lateinit var imgMore4: ImageView


    interface Callbacks {
        fun onContactUsOpen(show: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as ContactUsFragment.Callbacks
        callbacks?.onContactUsOpen(false)
    }

    override fun onDetach() {
        super.onDetach()
       // callbacks?.onContactUsOpen(true)
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
        val view = inflater.inflate(R.layout.fragment_contact_us, container, false)

        memberDetailsTv1 = view.findViewById(R.id.member_details1)
        memberDetailsTv2 = view.findViewById(R.id.member_details2)
        memberDetailsTv3 = view.findViewById(R.id.member_details3)
        memberDetailsTv4 = view.findViewById(R.id.member_details4)
        cardView1 = view.findViewById(R.id.cardView)
        cardView2 = view.findViewById(R.id.cardView2)
        cardView3 = view.findViewById(R.id.cardView3)
        cardView4 = view.findViewById(R.id.cardView4)

        imgMore1=view.findViewById(R.id.img_more1)
        imgMore2=view.findViewById(R.id.img_more2)
        imgMore3=view.findViewById(R.id.img_more3)
        imgMore4=view.findViewById(R.id.img_more4)

        cardView1.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv1.visibility = View.VISIBLE
                imgMore1.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv1.visibility = View.GONE
                imgMore1.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }
        cardView2.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv2.visibility = View.VISIBLE
                imgMore2.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv2.visibility = View.GONE
                imgMore2.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        cardView3.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv3.visibility = View.VISIBLE
                imgMore3.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv3.visibility = View.GONE
                imgMore3.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        cardView4.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv4.visibility = View.VISIBLE
                imgMore4.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv4.visibility = View.GONE
                imgMore4.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        imgMore1.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv1.visibility = View.VISIBLE
                imgMore1.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv1.visibility = View.GONE
                imgMore1.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        imgMore2.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv2.visibility = View.VISIBLE
                imgMore2.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv2.visibility = View.GONE
                imgMore2.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        imgMore3.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv3.visibility = View.VISIBLE
                imgMore3.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv3.visibility = View.GONE
                imgMore3.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        imgMore4.setOnClickListener {
            if (open) {
                open = false
                memberDetailsTv4.visibility = View.VISIBLE
                imgMore4.setImageResource(R.drawable.ic_drop_up)
            } else {
                open = true
                memberDetailsTv4.visibility = View.GONE
                imgMore4.setImageResource(R.drawable.ic_arrow_drop_down)
            }
        }

        return view;
    }
    companion object {
        fun newInstance(): ContactUsFragment {
            return ContactUsFragment()
        }
    }
}