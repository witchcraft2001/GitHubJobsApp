package ru.dm.githubpositions.data.responses

import com.google.gson.annotations.SerializedName

data class PositionResponse(
    val id: String,
    @SerializedName("created_at")
    val createdAt: String,
    val company: String,
    val title: String,
    val description: String?,
    @SerializedName("how_to_apply")
    val howToApply: String?,
    @SerializedName("company_logo")
    val companyLogo: String?
)
