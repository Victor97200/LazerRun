package com.example.lazerrun

data class Summary(
    val id: String,
    val categoryId: String,
    val totalTime: Long,
    val runTime: Long,
    val shootingTime: Long,
    val avgSpeed: Double,
    val minShootingTime: Long,
    val avgShootingTime: Long,
    val maxShootingTime: Long,
    val missedShots: Int,
    val startDateTime: String
)
