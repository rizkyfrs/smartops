package com.smartfren.smartops.data.models

import okhttp3.MultipartBody

data class Stolen(
    val name: String,
    val image: String,
    val file: MultipartBody.Part
)