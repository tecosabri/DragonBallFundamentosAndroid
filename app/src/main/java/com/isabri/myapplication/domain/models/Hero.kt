package com.isabri.myapplication.domain.models

data class Hero(
    val photo: String,
    val id: String,
    val name: String,
    val maxLive: Int = 100,
    var currentLive: Int = 100
)
