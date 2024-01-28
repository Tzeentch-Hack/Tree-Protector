package com.tzeench.treeprotectormobile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllTreesModelDto(
    @SerialName("data") val trees: List<TreeModelDto>
)

@Serializable
data class TreeModelDto(
    @SerialName("tree_id") val treeId: Int?,
    @SerialName("machine_id") val deviceId: String?,
    @SerialName("status") val status: String?,
    @SerialName("date_created") val dateCreated: String?,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("tree_kind") val treeType: String?,
    @SerialName("coordinates") val coordinates: String?
)
