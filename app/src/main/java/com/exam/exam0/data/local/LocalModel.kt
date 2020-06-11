package com.exam.exam0.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exam.exam0.data.remote.RemoteDto
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Parcelize
@Entity(
    tableName = "meds"
)
open class LocalModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val pieces: Int,
    val type: String,
    val best_before: Date,
    val picture: ByteArray,
    val base_substance: String,
    val base_substance_quantity: String,
    val description: String
) : Parcelable {
    var image : ByteArray? =null

    fun toRemote(): RemoteDto =
        RemoteDto(id, name, pieces, type, best_before, Base64.getEncoder().encodeToString(picture), base_substance, base_substance_quantity, description)
}