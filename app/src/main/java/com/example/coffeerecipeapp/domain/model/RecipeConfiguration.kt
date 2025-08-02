package com.example.coffeerecipeapp.domain.model

data class RecipeConfiguration(
    val recipe: Recipe,
    val coffeeAmount: Int,
    val grinder: String,
    val specificEquipment: Map<String, String> = emptyMap()
)