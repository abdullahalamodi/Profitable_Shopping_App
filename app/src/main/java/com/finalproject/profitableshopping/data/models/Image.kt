package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName

data class ImageModel (
    var id:Int=0,
    @SerializedName("image_path")
    var imagePath:String=""
)