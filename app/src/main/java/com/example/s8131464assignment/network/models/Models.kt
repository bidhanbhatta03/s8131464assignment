package com.example.s8131464assignment.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val status: String?,
    val keypass: String?
)

@Parcelize
data class DashboardItem(
    @SerializedName(value = "property1", alternate = ["species", "title", "name"])
    val property1: String?,
    @SerializedName(value = "property2", alternate = ["scientificName", "code", "identifier"])
    val property2: String?,
    @SerializedName(value = "description", alternate = ["details", "summary", "note"])
    val description: String?,
    val id: String? = null,
    @SerializedName(value = "extra1", alternate = ["habitat", "category"]) 
    val extra1: String? = null,
    @SerializedName(value = "extra2", alternate = ["diet", "type"]) 
    val extra2: String? = null,
    @SerializedName(value = "extra3", alternate = ["conservationStatus", "status"]) 
    val extra3: String? = null
) : Parcelable

// Matches API response
data class DashboardResponse(
    val entities: List<DashboardItem>,
    val entityTotal: Int
)


