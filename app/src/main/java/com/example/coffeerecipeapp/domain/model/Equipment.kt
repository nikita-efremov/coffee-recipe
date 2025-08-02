package com.example.coffeerecipeapp.domain.model

data class Equipment(
    val name: String,
    val isRequired: Boolean = true,
    val alternatives: List<String> = emptyList()
)