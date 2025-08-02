package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.PourOverDripper
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult
import com.example.coffeerecipeapp.domain.model.RecipeStep
import com.example.coffeerecipeapp.domain.model.RecipeType

class PourOverCalculator(
    private val grinders: Map<String, Grinder>,
    private val drippers: Map<String, PourOverDripper>
) : RecipeCalculator {

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.POUR_OVER

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val dripperName = configuration.specificEquipment["dripper"] ?: "V60 Ceramic"
        val dripper = drippers[dripperName]
            ?: throw IllegalArgumentException("Unknown dripper: $dripperName")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 23

        return when (configuration.recipe.id) {
            "pour_over_hoffmann" -> calculateHoffmannRecipe(coffeeGrams, grinder, grindSetting)
            "pour_over_tetsu_kasya" -> calculateTetsuKasyaRecipe(coffeeGrams, grinder, grindSetting)
            else -> calculateLanceHedrickRecipe(coffeeGrams, dripper, dripperName, grinder, grindSetting)
        }
    }

    private fun calculateHoffmannRecipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = 500 // Fixed 500ml as per specification

        val equipment = listOf(
            "• V60 Ceramic",
            "- Hario Filter",
            "- 100°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}"
        )

        val bloomWater = coffeeGrams * 2 // 2g water per 1g coffee
        val secondPourTarget = (waterVolume * 0.6).toInt() // 60% of total volume

        val steps = listOf(
            RecipeStep(1, "Add ${bloomWater}ml water (2g per 1g coffee), swirl, let it bloom", "0:00", waterAmount = bloomWater),
            RecipeStep(2, "Add water till ${secondPourTarget}ml (60% of total volume)", "0:45 - 1:15", waterAmount = secondPourTarget - bloomWater),
            RecipeStep(3, "Add water till ${waterVolume}ml (100% of total volume). Stir 1 turn clockwise, stir 1 turn anticlockwise, make gentle swirl", "1:15 - 1:45", waterAmount = waterVolume - secondPourTarget),
            RecipeStep(4, "Should be finished", "3:30")
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateTetsuKasyaRecipe(coffeeGrams: Int, grinder: Grinder, grindSetting: Int): RecipeResult {
        val waterVolume = 500 // Fixed 500ml as per specification

        val equipment = listOf(
            "• V60 Ceramic",
            "- Hario Filter",
            "- 95°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}"
        )

        val steps = listOf(
            RecipeStep(1, "Add 100ml of water (up to 100ml)", "0:00", waterAmount = 100),
            RecipeStep(2, "Add 100ml of water (up to 200ml)", "0:45", waterAmount = 100),
            RecipeStep(3, "Add 100ml of water (up to 300ml)", "1:30", waterAmount = 100),
            RecipeStep(4, "Add 100ml of water (up to 400ml)", "2:15", waterAmount = 100),
            RecipeStep(5, "Add 100ml of water (up to 500ml)", "3:00", waterAmount = 100)
        )

        return RecipeResult(equipment, steps)
    }

    private fun calculateLanceHedrickRecipe(
        coffeeGrams: Int,
        dripper: PourOverDripper,
        dripperName: String,
        grinder: Grinder,
        grindSetting: Int
    ): RecipeResult {
        val waterVolume = coffeeGrams * 16
        val firstPour = coffeeGrams * 3
        val secondPourTarget = coffeeGrams * 10

        val equipment = listOf(
            "• $dripperName",
            "- ${dripper.filter} filter",
            "- ${dripper.temperature}°C water",
            "- ${coffeeGrams}g coffee",
            "- ${waterVolume}ml water",
            "- $grindSetting clicks ${grinder.name}"
        )

        val steps = generatePourOverSteps(coffeeGrams, waterVolume, dripper, dripperName)

        return RecipeResult(equipment, steps)
    }

    private fun generatePourOverSteps(
        coffeeGrams: Int,
        waterVolume: Int,
        dripper: PourOverDripper,
        dripperName: String
    ): List<RecipeStep> {
        val firstPour = coffeeGrams * 3
        val secondPourTarget = coffeeGrams * 10

        return when (dripperName) {
            "V60 Ceramic" -> listOf(
                RecipeStep(1, "Put Ceramic V60 on kettle during boiling (without filter)"),
                RecipeStep(2, "After water is boiled, put Ceramic V60 on carafe, insert filter and additionally preheat with 300ml water"),
                RecipeStep(3, "Put ${coffeeGrams}g grinder coffee, make a hole in coffee bed"),
                RecipeStep(4, "Pour ${firstPour}ml water, stir thoroughly", waterAmount = firstPour),
                RecipeStep(5, "Wait until all coffee is dripped"),
                RecipeStep(6, "Pour relatively fast till ${secondPourTarget}ml, then relatively slow till ${waterVolume}ml"),
                RecipeStep(7, "Do a gentle swirl")
            )
            "Kalita Tsubame" -> listOf(
                RecipeStep(1, "Put Kalita Tsubame on kettle during heating (without filter)"),
                RecipeStep(2, "After water reaches ${dripper.temperature}°C, put Kalita on carafe, insert filter and preheat with 300ml water", temperature = dripper.temperature),
                RecipeStep(3, "Put ${coffeeGrams}g grinder coffee, level the bed"),
                RecipeStep(4, "Pour ${firstPour}ml water in circular motion, stir gently", waterAmount = firstPour),
                RecipeStep(5, "Wait until all coffee is dripped"),
                RecipeStep(6, "Pour in 3-4 pulses till ${secondPourTarget}ml, then slowly till ${waterVolume}ml"),
                RecipeStep(7, "Let it finish dripping")
            )
            else -> listOf(
                RecipeStep(1, "Put $dripperName on kettle during heating (without filter)"),
                RecipeStep(2, "After water reaches ${dripper.temperature}°C, put dripper on carafe, insert filter and preheat with 300ml water", temperature = dripper.temperature),
                RecipeStep(3, "Put ${coffeeGrams}g grinder coffee, prepare the bed"),
                RecipeStep(4, "Pour ${firstPour}ml water, stir thoroughly", waterAmount = firstPour),
                RecipeStep(5, "Wait until all coffee is dripped"),
                RecipeStep(6, "Pour till ${secondPourTarget}ml, then slowly till ${waterVolume}ml"),
                RecipeStep(7, "Finish brewing")
            )
        }
    }
}