package com.tzeench.treeprotectormobile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SatelliteDto(
    @SerialName("colored_photo_url") val coloredPhotoUrl: String,
    @SerialName("binary_photo_url") val binaryPhotoUrl: String
)
