package com.apple.shubham.farm_suraksha

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.apple.shubham.farm_suraksha.Adapters.NodeAdapter
import com.apple.shubham.farm_suraksha.Class.BlockChainData

import com.apple.shubham.farm_suraksha.Class.FinalDetails
import com.apple.shubham.farm_suraksha.Class.PassArray
import kotlinx.android.synthetic.main.node_list.*

class NodeClass: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.node_list)
//        var passArray=intent.getSerializableExtra("PassArray") as PassArray
//        var arrayData=passArray.arrayList

        var senderData=intent.getSerializableExtra("Sender") as ArrayList<BlockChainData>
        var receiverData=intent.getSerializableExtra("Receiver") as ArrayList<BlockChainData>
        var arrayList=ArrayList<BlockChainData>()
        var size=senderData.size
        for(i in size-1 downTo 0)
        {
            if(i== size-1)
            {
                arrayList.add(senderData[i])
                arrayList.add(receiverData[i])
            }
            else
                arrayList.add(receiverData[i])

        }
        var adapter=NodeAdapter(arrayList)
        recyclerView.layoutManager= LinearLayoutManager(this@NodeClass)
        recyclerView.adapter=adapter
        //Log.e("Data",map.toString())
        Log.e("Data",senderData.toString())
        Log.e("Data",receiverData.toString())
    }
}