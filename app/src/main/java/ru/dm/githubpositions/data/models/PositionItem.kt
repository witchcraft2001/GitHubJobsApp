package ru.dm.githubpositions.data.models

data class PositionItem(
    val id: String,
    override var createdAt: String,
    val company: String,
    val title: String,
    val description: String?,
    val howToApply: String?,
    val companyLogo: String?
) : Item()
