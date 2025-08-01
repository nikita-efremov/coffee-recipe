// RecipeCalculator.kt
package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.*

interface RecipeCalculator {
    fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult
    fun canCalculate(recipeType: RecipeType): Boolean
}

// PourOverCalculator.kt
class PourOverCalculator(
    private val grinders: Map<String, Grinder>,
    private val drippers: Map<String, PourOverDripper>
) : RecipeCalculator {

    override fun canCalculate(recipeType: RecipeType): Boolean =
        recipeType == RecipeType.POUR_OVER

    override fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val coffeeGrams = configuration.coffeeAmount
        val waterVolume = coffeeGrams * 16
        val grinder = grinders[configuration.grinder]
            ?: throw IllegalArgumentException("Unknown grinder: ${configuration.grinder}")

        val dripperName = configuration.specificEquipment["dripper"] ?: "V60 Ceramic"
        val dripper = drippers[dripperName]
            ?: throw IllegalArgumentException("Unknown dripper: $dripperName")

        val grindSetting = grinder.clickSettings[configuration.recipe.id] ?: 23

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

// AeropressCalculator.kt
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