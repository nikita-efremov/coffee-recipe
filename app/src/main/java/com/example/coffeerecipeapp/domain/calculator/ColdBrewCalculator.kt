package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class ColdBrewCalculator(
    private val grinders: Map<String, Grinder>
) : RecipeCalculator {

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.COLD_BREW

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val waterVolume = 800 // Fixed 800ml as per specification
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 35

        val equipment = listOf(
            "- Cold Brew Hario Bottle",
            "- Room temperature water",
            "- ${coffeeGrams}g coffee (coarse grind)",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}",
            "- Refrigerator"
        )

        val steps = listOf(
            RecipeStep(1, "Put grinded coffee in the Bottle"),
            RecipeStep(
                2,
                "Add whole amount of water (${waterVolume}ml)",
                waterAmount = waterVolume
            ),
            RecipeStep(3, "Shake thoroughly"),
            RecipeStep(4, "After 6 - 18 hours shake once again", "6h - 18h"),
            RecipeStep(5, "After 24 hours remove coffee grounds", "24h"),
            RecipeStep(6, "Pour into cup and enjoy")
        )

        return RecipeResult(equipment, steps)
    }
}
