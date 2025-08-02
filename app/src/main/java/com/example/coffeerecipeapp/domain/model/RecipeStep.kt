package com.example.coffeerecipeapp.domain.model

data class RecipeStep(
    val stepNumber: Int,
    val instruction: String,
    val timing: String? = null,
    val temperature: Int? = null,
    val waterAmount: Int? = null
)