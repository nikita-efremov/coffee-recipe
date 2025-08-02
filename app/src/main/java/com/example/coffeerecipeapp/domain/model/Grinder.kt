package com.example.coffeerecipeapp.domain.model

data class Grinder(
    val name: String,
    val clickSettings: Map<String, Int> // recipeId to click setting
)