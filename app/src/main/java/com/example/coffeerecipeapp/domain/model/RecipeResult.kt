package com.example.coffeerecipeapp.domain.model

data class RecipeResult(
    val equipment: List<String>,
    val steps: List<RecipeStep>
)