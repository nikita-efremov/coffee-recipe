package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class AeropressCalculator(
    private val grinders: Map<String, Grinder>
) : RecipeCalculator {

    private val waterAmounts = mapOf(15 to 210, 18 to 257)

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.AEROPRESS

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val waterVolume = waterAmounts[coffeeGrams] ?: 210
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 13

        val equipment = listOf(
            "- Aeropress filter",
            "- Aeropress in upright position",
            "- 96°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}"
        )

        val steps = listOf(
            RecipeStep(1, "Add ${waterVolume}ml water, 3 stirs along the diameter (back to front), insert plunger to stop water", "0:00", waterAmount = waterVolume),
            RecipeStep(2, "Remove plunger, 3 stirs along the diameter (back to front), insert plunger, push", "1:00"),
            RecipeStep(3, "Should be finished", "1:30"),
            RecipeStep(4, "Tuning: Adjust by grind size")
        )

        return RecipeResult(equipment, steps)
    }
}