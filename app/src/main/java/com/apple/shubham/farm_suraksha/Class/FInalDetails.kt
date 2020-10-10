package com.apple.shubham.farm_suraksha.Class

import java.io.Serializable

data class FinalDetails(var name:String,var type:String,var address:String,var product:String,var amount:String ):Serializable {
    constructor():this("","","","","")

}