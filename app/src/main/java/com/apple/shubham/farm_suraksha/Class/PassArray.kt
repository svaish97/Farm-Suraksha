package com.apple.shubham.farm_suraksha.Class

import java.io.Serializable

data class PassArray(var arrayList:ArrayList<FinalDetails>):Serializable {
    constructor():this(ArrayList())
}