// Recipe.kt
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

enum class RecipeType {
    POUR_OVER,
    AEROPRESS,
    FRENCH_PRESS,
    ESPRESSO
}

data class Equipment(
    val name: String,
    val isRequired: Boolean = true,
    val alternatives: List<String> = emptyList()
)

// RecipeConfiguration.kt
data class RecipeConfiguration(
    val recipe: Recipe,
    val coffeeAmount: Int,
    val grinder: String,
    val specificEquipment: Map<String, String> = emptyMap()
)

// RecipeResult.kt
data class RecipeResult(
    val equipment: List<String>,
    val steps: List<RecipeStep>
)

data class RecipeStep(
    val stepNumber: Int,
    val instruction: String,
    val timing: String? = null,
    val temperature: Int? = null,
    val waterAmount: Int? = null
)

// Grinder.kt
data class Grinder(
    val name: String,
    val clickSettings: Map<String, Int> // recipeId to click setting
)

// PourOverDripper.kt
data class PourOverDripper(
    val name: String,
    val temperature: Int,
    val filter: String,
    val specificInstructions: Map<String, String> = emptyMap()
)