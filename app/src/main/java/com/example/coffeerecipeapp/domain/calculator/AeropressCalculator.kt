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
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 13

        return when (configuration.recipe.id) {
            "aeropress_my_reverted" -> calculateRevertedAeropressRecipe(coffeeGrams, grinder, grindSetting)
            else -> calculateTimWendelboeRecipe(coffeeGrams, grinder, grindSetting)
        }
    }

    private fun calculateRevertedAeropressRecipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = 214 // Fixed 214ml as per specification
        val bloomWater = 45 // Fixed 45g bloom water

        val equipment = listOf(
            "- Aeropress in reverted position",
            "- Aeropress filter",
            "- 95°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}"
        )

        val steps = listOf(
            RecipeStep(1, "Add ${bloomWater}g of water, steer aggressively", "0:00", waterAmount = bloomWater),
            RecipeStep(2, "Bloom", "0:00 - 0:30"),
            RecipeStep(3, "Add water till ${waterVolume}g, steer gently, close", "0:30", waterAmount = waterVolume - bloomWater),
            RecipeStep(4, "Pushing", "1:30 - 2:00")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateTimWendelboeRecipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = waterAmounts[coffeeGrams] ?: 210

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