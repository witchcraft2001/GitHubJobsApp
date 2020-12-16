package ru.dm.githubpositions.data.models

data class Position(
    val id: String,
    val createdAt: String,
    val company: String,
    val title: String,
    val description: String?,
    val howToApply: String?,
    val companyLogo: String?
)
