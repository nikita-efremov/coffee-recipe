package com.example.coffeerecipeapp.domain.model

data class EspressoBrewer(
    val name: String,
    val heatingLevels: List<Int> = emptyList(),
    val supportedBaskets: List<String> = emptyList(),
    val specificInstructions: Map<String, String> = emptyMap()
)