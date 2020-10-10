package com.apple.shubham.farm_suraksha.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apple.shubham.farm_suraksha.Class.BlockChainData
import com.apple.shubham.farm_suraksha.NodeClass
import com.apple.shubham.farm_suraksha.R
import kotlinx.android.synthetic.main.dialog_details.view.*
import kotlinx.android.synthetic.main.node_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class NodeAdapter(var nodeList: ArrayList<BlockChainData>) : RecyclerView.Adapter<NodeAdapter.NodeViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NodeViewHolder {
        var layoutInflater = p0.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return NodeViewHolder(layoutInflater.inflate(R.layout.node_view, null))

    }

    override fun getItemCount(): Int = nodeList.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(p0: NodeViewHolder, p1: Int) {
        var nodeClass = nodeList[p1]
        if (p1 % 2 == 0) {
            p0.itemView.llRight.visibility = View.GONE
            p0.itemView.rightCard.visibility = View.GONE

            if (nodeClass.type.equals("f"))
                p0.itemView.tvLeftDistributer.text = "Producer"
            else if (nodeClass.type .equals("d"))
                p0.itemView.tvLeftDistributer.text = "Distributer"
            else if (nodeClass.type.equals("r"))
                p0.itemView.tvLeftDistributer.text = "Retailer"
            else
                p0.itemView.tvLeftDistributer.text = "WholeSaler"
            p0.itemView.tvNameLeft.text = nodeList[p1].name
            //p0.itemView.tvAddressLeft.text = nodeList[p1].address
            if (p1 == nodeList.size - 1) {
                p0.itemView.farmerLayout.visibility=View.VISIBLE
                p0.itemView.imageFarmerLeft.visibility=View.INVISIBLE
            }
        } else {
            p0.itemView.llLeft.visibility = View.GONE
            p0.itemView.leftCard.visibility = View.GONE

            if (nodeClass.type.equals("f"))
                p0.itemView.tvRightDistributer.text = "Producer"
            else if (nodeClass.type .equals("d"))
                p0.itemView.tvRightDistributer.text = "Distributer"
            else if (nodeClass.type.equals("r"))
                p0.itemView.tvRightDistributer.text = "Retailer"
            else
                p0.itemView.tvRightDistributer.text = "WholeSaler"
            p0.itemView.tvNameRight.text = nodeList[p1].name
            //p0.itemView.tvAddressRight.text = nodeList[p1].address
            if (p1 == nodeList.size - 1) {
                p0.itemView.farmerLayout.visibility=View.VISIBLE
                p0.itemView.imageFarmerRight.visibility=View.INVISIBLE
            }
        }
        p0.itemView.cardLeft.setOnClickListener {
            var alertDialog=AlertDialog.Builder(p0.itemView.context).create()
            alertDialog.setCancelable(false)
            var lf=p0.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view=lf.inflate(R.layout.dialog_details,null)
            view.btnClose.setOnClickListener {
                alertDialog.dismiss()
            }

            view.btnClose.setOnClickListener {
                alertDialog.dismiss()
            }
            view.tvName.text="Name: "+nodeList[p1].name
//            view.tvAddress.text="Address: "+nodeList[p1].address
            var timeStamp=nodeList[p1].timeStamp.toLong()
            var cal=Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis=timeStamp*1000
            var date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
            view.tvAddress.text="Date & Time: "+date
            if (nodeClass.type.equals("f"))
                view.tvType.text = "Category: "+"Producer"
            else if (nodeClass.type .equals("d"))
                view.tvType.text = "Category: "+"Distributer"
            else if (nodeClass.type.equals( "r"))
                view.tvType.text = "Category: "+"Retailer"
            else
                view.tvType.text="Category: "+"WholeSaler"

            view.tvProductName.text="Product: "+nodeList[p1].product
            view.tvAmount.text="Amount in Kg: "+nodeList[p1].amount

            alertDialog.setView(view)
                // alertDialog.setTitle(p0.itemView.context.getString(R.string.chain_title))
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
//            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Close"
//            ) { dialog, which -> alertDialog.dismiss() }
            alertDialog.show()

        }
        p0.itemView.cardRight.setOnClickListener {
            var alertDialog=AlertDialog.Builder(p0.itemView.context).create()
            alertDialog.setCancelable(false)
            var lf=p0.itemView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view=lf.inflate(R.layout.dialog_details,null)
            view.btnClose.setOnClickListener {
                alertDialog.dismiss()
            }
            view.tvName.text= "Name: "+nodeList[p1].name
//            view.tvAddress.text="Address: "+nodeList[p1].address
            var timeStamp=nodeList[p1].timeStamp.toLong()
            var cal=Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis=timeStamp*1000
            var date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
            view.tvAddress.text="Date & Time: "+date
            if (nodeClass.type.equals( 'f'))
                view.tvType.text = "Category: "+"Producer"
            else if (nodeClass.type .equals( 'd'))
                view.tvType.text = "Category: "+"Distributer"
            else if (nodeClass.type.equals( 'r'))
                view.tvType.text = "Category: "+"Retailer"
            else
                view.tvType.text="Category: "+"WholeSaler"

            view.tvProductName.text="Product: "+nodeList[p1].product
            view.tvAmount.text="Amount in Kg: "+nodeList[p1].amount
            alertDialog.setView(view)
            // alertDialog.setTitle(p0.itemView.context.getString(R.string.chain_title))
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
//            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Close"
//            ) { dialog, which -> alertDialog.dismiss() }
            alertDialog.show()
        }

    }

    class NodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}