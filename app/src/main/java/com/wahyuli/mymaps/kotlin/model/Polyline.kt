package com.wahyuli.mymaps.kotlin.model

import com.google.gson.annotations.SerializedName

data class Polyline(
        @SerializedName("points")
        var points: String?
)