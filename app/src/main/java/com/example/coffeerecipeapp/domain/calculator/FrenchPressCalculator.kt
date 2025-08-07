package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class FrenchPressCalculator(
    private val grinders: Map<String, Grinder>
) : RecipeCalculator {

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.FRENCH_PRESS

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 25

        return when (configuration.recipe.id) {
            "french_press_hoffmann" -> calculateHoffmannFrenchPress(coffeeGrams, grinder, grindSetting)
            else -> throw IllegalArgumentException("Unknown french press recipe: ${configuration.recipe.id}")
        }
    }

    private fun calculateHoffmannFrenchPress(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = coffeeGrams * 16 // 1:16 ratio

        val equipment = listOf(
            "- French Press",
            "- 95°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}",
            "- Timer"
        )

        val steps = listOf(
            RecipeStep(1, "Grind ${coffeeGrams}g coffee with $grindSetting clicks"),
            RecipeStep(2, "Add all ${waterVolume}ml water at 95°C", "0:00", temperature = 95, waterAmount = waterVolume),
            RecipeStep(3, "Stir gently", "4:00"),
            RecipeStep(4, "Pour into cups (don't press the plunger)", "9:00")
        )

        return RecipeResult(equipment, steps)
    }
}