package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class EspressoCalculator(
    private val grinders: Map<String, Grinder>
) : RecipeCalculator {

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.ESPRESSO

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 15

        return when (configuration.recipe.id) {
            "espresso_budget" -> calculateBudgetEspresso(coffeeGrams, grinder, grindSetting)
            "espresso_classic" -> calculateClassicEspresso(coffeeGrams, grinder, grindSetting)
            "espresso_modern" -> calculateModernEspresso(coffeeGrams, grinder, grindSetting)
            "espresso_turbo_shot" -> calculateTurboShot(coffeeGrams, grinder, grindSetting)
            else -> throw IllegalArgumentException("Unknown espresso recipe: ${configuration.recipe.id}")
        }
    }

    private fun calculateBudgetEspresso(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val equipment = listOf(
            "- Delonghi EC 685",
            "- ${coffeeGrams}g coffee",
            "- $grindSetting clicks ${grinder.name}",
            "- Scale",
            "- Timer"
        )

        val steps = listOf(
            RecipeStep(1, "Grind ${coffeeGrams}g coffee with $grindSetting clicks"),
            RecipeStep(2, "Load coffee into portafilter and tamp"),
            RecipeStep(3, "Start automatic pre-infusion"),
            RecipeStep(4, "Push for 25 seconds after pre-infusion", "0:25")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateClassicEspresso(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val targetYield = 36 // 36g result drink

        val equipment = listOf(
            "- Flair 58",
            "- IMS basket",
            "- Heating level 3",
            "- 95°C water",
            "- ${coffeeGrams}g coffee",
            "- Target: ${targetYield}g output",
            "- $grindSetting clicks ${grinder.name}",
            "- Scale",
            "- Timer"
        )

        val steps = listOf(
            RecipeStep(1, "Heat water to 95°C, set heating level 3", temperature = 95),
            RecipeStep(2, "Grind ${coffeeGrams}g coffee with $grindSetting clicks"),
            RecipeStep(3, "Load coffee into IMS basket and level"),
            RecipeStep(4, "Pre-infusion with 3 bar pressure", "0:00 - 0:15"),
            RecipeStep(5, "Push with 9 bar pressure to get ${targetYield}g output", "0:15 - 0:40")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateModernEspresso(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val targetYield = 36 // 36g result drink

        val equipment = listOf(
            "- Flair 58",
            "- IMS basket",
            "- Heating level 2",
            "- 92°C water",
            "- ${coffeeGrams}g coffee",
            "- Target: ${targetYield}g output",
            "- $grindSetting clicks ${grinder.name}",
            "- Scale",
            "- Timer"
        )

        val steps = listOf(
            RecipeStep(1, "Heat water to 92°C, set heating level 2", temperature = 92),
            RecipeStep(2, "Grind ${coffeeGrams}g coffee with $grindSetting clicks"),
            RecipeStep(3, "Load coffee into IMS basket and level"),
            RecipeStep(4, "Push with 3 bar pressure to get ${targetYield}g output", "0:00 - 0:25")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateTurboShot(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val targetYield = 55 // 55g result drink

        val equipment = listOf(
            "- Flair 58",
            "- IMS basket",
            "- Heating level 3",
            "- 95°C water",
            "- ${coffeeGrams}g coffee",
            "- Target: ${targetYield}g output",
            "- $grindSetting clicks ${grinder.name}",
            "- Scale",
            "- Timer"
        )

        val steps = listOf(
            RecipeStep(1, "Heat water to 95°C, set heating level 3", temperature = 95),
            RecipeStep(2, "Grind ${coffeeGrams}g coffee with $grindSetting clicks"),
            RecipeStep(3, "Load coffee into IMS basket and level"),
            RecipeStep(4, "Push with 6 bar pressure to get ${targetYield}g output", "0:00 - 0:15")
        )

        return RecipeResult(equipment, steps)
    }
}