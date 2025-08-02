package com.example.coffeerecipeapp.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val type: RecipeType,
    val coffeeAmounts: List<Int>,
    val defaultCoffeeAmount: Int,
    val supportedGrinders: List<String>,
    val equipment: List<Equipment> = emptyList()
)