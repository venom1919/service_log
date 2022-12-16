package com.service_log.model

import com.google.gson.annotations.SerializedName

data class PostsResponse(

    @SerializedName("status_code")
    var status: Int,

)