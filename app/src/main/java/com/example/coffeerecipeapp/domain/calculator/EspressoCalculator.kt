package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.EspressoBrewer
import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class EspressoCalculator(
    private val grinders: Map<String, Grinder>,
    private val espressoBrewers: Map<String, EspressoBrewer>
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

    private fun calculateMattWintonRecipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = 300 // Fixed 300ml as per specification

        val equipment = listOf(
            "• V60 Ceramic",
            "- Hario Filter",
            "- 93°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}",
            "- Strong pouring from center"
        )

        val steps = listOf(
            RecipeStep(1, "Add 60ml water from center", "0:00", temperature = 93, waterAmount = 60),
            RecipeStep(2, "Add 60ml water from center", "0:30", waterAmount = 60),
            RecipeStep(3, "Add 60ml water each time when coffee starts dripping", "Variable timing", waterAmount = 60),
            RecipeStep(4, "Continue until ${waterVolume}ml total", waterAmount = 60),
            RecipeStep(5, "Final pour", waterAmount = 60),
            RecipeStep(6, "Tuning: Long time, dry, bitter → make coarser"),
            RecipeStep(7, "Quick time, hollow, sharp, sour → make finer")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateHoffmannV2Recipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = 300 // Fixed 300ml as per specification

        val equipment = listOf(
            "• V60 Ceramic",
            "- Hario Filter",
            "- 100°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}",
            "- Gentle pouring"
        )

        val steps = listOf(
            RecipeStep(1, "Make hole in coffee bed, add 60ml water, swirl", "0:00", temperature = 100, waterAmount = 60),
            RecipeStep(2, "Add 60ml water (up to 120ml)", "0:45 - 1:00", waterAmount = 60),
            RecipeStep(3, "Pause", "1:00 - 1:10"),
            RecipeStep(4, "Add 60ml water (up to 180ml)", "1:10 - 1:20", waterAmount = 60),
            RecipeStep(5, "Pause", "1:20 - 1:30"),
            RecipeStep(6, "Add 60ml water (up to 240ml)", "1:30 - 1:40", waterAmount = 60),
            RecipeStep(7, "Pause", "1:40 - 1:50"),
            RecipeStep(8, "Add 60ml water (up to 300ml), swirl", "1:50 - 2:00", waterAmount = 60),
            RecipeStep(9, "Expected finish time", "3:00")
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