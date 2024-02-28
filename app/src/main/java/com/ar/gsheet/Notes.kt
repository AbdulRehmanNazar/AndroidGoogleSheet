package com.ar.gsheet

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Notes(
    @Json(name = "Title")
    val title: String?,
    @Json(name = "Description")
    val description: String?,
)
