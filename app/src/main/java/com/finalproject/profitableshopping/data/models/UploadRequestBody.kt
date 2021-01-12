package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName

data class serverResponse(
  @SerializedName("success")
  var success: Boolean,
 @SerializedName("message")
var message: String
 )