package com.example.coffeerecipeapp.domain.model

data class PourOverDripper(
    val name: String,
    val temperature: Int,
    val filter: String,
    val specificInstructions: Map<String, String> = emptyMap()
)