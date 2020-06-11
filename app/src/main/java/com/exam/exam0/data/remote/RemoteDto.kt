package com.exam.exam0.data.remote

import com.exam.exam0.data.local.LocalModel
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*


data class RemoteDto(
    @SerializedName("id")
    var remoteId: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("pieces")
    var pieces: Int? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("bestBefore")
    var best_before: Date? = null,

    @SerializedName("picture")
    var picture: String? = null,

    @SerializedName("baseSubstance")
    var base_substance: String? = null,

    @SerializedName("baseSubstanceQuantity")
    var base_substance_quantity: String? = null,

    @SerializedName("description")
    var description: String? = null
) {
    fun convertToLocalEntity() = LocalModel(
        remoteId ?: 0,
        name ?: "",
        pieces ?: 0,
        type ?: "",
        best_before ?: LocalDateTime.now() as Date,
        if (picture == null) byteArrayOf(0x17.toByte()) else Base64.getDecoder().decode(picture),
        base_substance ?: "",
        base_substance_quantity ?: "",
        description ?: ""
    )
}