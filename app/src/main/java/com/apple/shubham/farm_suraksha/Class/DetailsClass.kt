package com.apple.shubham.farm_suraksha.Class

import java.io.Serializable

data class DetailsClass(var name:String,var type:String,var address:String):Serializable {
    constructor():this("","","")
}
