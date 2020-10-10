package com.apple.shubham.farm_suraksha.Class

import java.io.Serializable

data class BlockChainData(var name:String,var address:String,var type:String,var product:String,var amount:String,var timeStamp:String):Serializable {
    constructor():this("","","","","","")
}
