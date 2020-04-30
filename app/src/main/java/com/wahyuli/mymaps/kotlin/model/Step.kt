package com.wahyuli.mymaps.kotlin.model

import com.google.gson.annotations.SerializedName
import com.wahyuli.mymaps.kotlin.model.*

data class Step(
    @SerializedName("distance")
        var distance: Distance?,
    @SerializedName("duration")
        var duration: Duration?,
    @SerializedName("end_location")
        var endLocation: EndLocation?,
    @SerializedName("html_instructions")
        var htmlInstructions: String?,
    @SerializedName("maneuver")
        var maneuver: String?,
    @SerializedName("polyline")
        var polyline: Polyline?,
    @SerializedName("start_location")
        var startLocation: StartLocation?,
    @SerializedName("travel_mode")
        var travelMode: String?
)