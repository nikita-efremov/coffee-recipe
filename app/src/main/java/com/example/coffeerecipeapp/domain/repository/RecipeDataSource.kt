package com.example.coffeerecipeapp.domain.repository

import com.example.coffeerecipeapp.domain.model.Equipment
import com.example.coffeerecipeapp.domain.model.EspressoBrewer
import com.example.coffeerecipeapp.domain.model.Grinder
import com.example.coffeerecipeapp.domain.model.PourOverDripper
import com.example.coffeerecipeapp.domain.model.Recipe
import com.example.coffeerecipeapp.domain.model.RecipeType

class RecipeDataSource : RecipeRepository {

    private val grinders = mapOf(
        "Comandante C40" to Grinder(
            name = "Comandante C40",
            clickSettings = mapOf(
                "pour_over_lance_hedrick" to 23,
                "aeropress_tim_wendelboe" to 13,
                "pour_over_hoffmann" to 25,
                "cold_brew_default" to 35,
                "pour_over_tetsu_kasya" to 28,
                "pour_over_kalita_george_howell" to 28,
                "aeropress_my_reverted" to 26,
                // New recipes
                "espresso_budget" to 10, // 9.5 rounds to 10
                "french_press_hoffmann" to 25,
                "espresso_classic" to 6,
                "espresso_modern" to 8, // approximate conversion from Niche Zero
                "espresso_turbo_shot" to 10, // approximate conversion from Niche Zero
                "pour_over_matt_winton" to 26,
                "pour_over_hoffmann_v2" to 23
            )
        ),
        "Timemore C2" to Grinder(
            name = "Timemore C2",
            clickSettings = mapOf(
                "pour_over_lance_hedrick" to 21,
                "aeropress_tim_wendelboe" to 12,
                "pour_over_hoffmann" to 23,
                "cold_brew_default" to 33,
                "pour_over_tetsu_kasya" to 23,
                "pour_over_kalita_george_howell" to 25,
                "aeropress_my_reverted" to 22,
                // New recipes
                "espresso_budget" to 9, // approximate conversion
                "french_press_hoffmann" to 22,
                "espresso_classic" to 5, // approximate conversion
                "espresso_modern" to 7, // approximate conversion
                "espresso_turbo_shot" to 9, // approximate conversion
                "pour_over_matt_winton" to 23,
                "pour_over_hoffmann_v2" to 21
            )
        ),
        "Niche Zero" to Grinder(
            name = "Niche Zero",
            clickSettings = mapOf(
                // Adding settings for recipes that specifically mention Niche Zero
                "espresso_classic" to 18,
                "espresso_modern" to 20,
                "espresso_turbo_shot" to 24,
                // Approximate settings for other recipes
                "espresso_budget" to 22
            )
        )
    )

    private val pourOverDrippers = mapOf(
        "V60 Ceramic" to PourOverDripper(
            name = "V60 Ceramic",
            temperature = 100,
            filter = "Hario Filter"
        ),
        "Kalita Tsubame" to PourOverDripper(
            name = "Kalita Tsubame",
            temperature = 93,
            filter = "Kalita Wave"
        )
    )

    private val espressoBrewers = mapOf(
        "Delonghi EC 685" to EspressoBrewer(
            name = "Delonghi EC 685",
            specificInstructions = mapOf(
                "preinfusion" to "automatic"
            )
        ),
        "Flair 58" to EspressoBrewer(
            name = "Flair 58",
            heatingLevels = listOf(1, 2, 3),
            supportedBaskets = listOf("IMS basket"),
            specificInstructions = mapOf(
                "manual_pressure" to "true"
            )
        )
    )

    private val recipes = listOf(
        // Existing recipes...
        Recipe(
            id = "pour_over_lance_hedrick",
            name = "Pour Over: Lance Hedrick",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = (12..45).toList(),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Pour Over Dripper", alternatives = listOf("V60 Ceramic", "Kalita Tsubame")),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "aeropress_tim_wendelboe",
            name = "Aeropress: Tim Wendelboe",
            type = RecipeType.AEROPRESS,
            coffeeAmounts = listOf(15, 18),
            defaultCoffeeAmount = 15,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Aeropress"),
                Equipment("Aeropress Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "pour_over_hoffmann",
            name = "Pour Over: Hoffmann",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = listOf(30),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("V60 Ceramic"),
                Equipment("Hario Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "cold_brew_default",
            name = "Cold Brew: Default",
            type = RecipeType.COLD_BREW,
            coffeeAmounts = listOf(47),
            defaultCoffeeAmount = 47,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Cold Brew Hario Bottle"),
                Equipment("Scale")
            )
        ),
        Recipe(
            id = "pour_over_tetsu_kasya",
            name = "Pour Over: Tetsu Kasya",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = listOf(30),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("V60 Ceramic"),
                Equipment("Hario Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "pour_over_kalita_george_howell",
            name = "Pour Over: Kalita George Howell",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = listOf(30),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Kalita Tsubame"),
                Equipment("Kalita Wave"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "aeropress_my_reverted",
            name = "Aeropress: My Reverted",
            type = RecipeType.AEROPRESS,
            coffeeAmounts = listOf(15),
            defaultCoffeeAmount = 15,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Aeropress in reverted position"),
                Equipment("Aeropress Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),

        // NEW RECIPES
        Recipe(
            id = "espresso_budget",
            name = "Espresso: Budget",
            type = RecipeType.ESPRESSO,
            coffeeAmounts = listOf(17),
            defaultCoffeeAmount = 17,
            supportedGrinders = listOf("Comandante C40", "Timemore C2", "Niche Zero"),
            equipment = listOf(
                Equipment("Delonghi EC 685"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "french_press_hoffmann",
            name = "French Press: Hoffmann",
            type = RecipeType.FRENCH_PRESS,
            coffeeAmounts = listOf(30),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("French Press"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "espresso_classic",
            name = "Espresso: Classic",
            type = RecipeType.ESPRESSO,
            coffeeAmounts = listOf(18),
            defaultCoffeeAmount = 18,
            supportedGrinders = listOf("Comandante C40", "Timemore C2", "Niche Zero"),
            equipment = listOf(
                Equipment("Flair 58"),
                Equipment("IMS basket"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "espresso_modern",
            name = "Espresso: Modern",
            type = RecipeType.ESPRESSO,
            coffeeAmounts = listOf(18),
            defaultCoffeeAmount = 18,
            supportedGrinders = listOf("Comandante C40", "Timemore C2", "Niche Zero"),
            equipment = listOf(
                Equipment("Flair 58"),
                Equipment("IMS basket"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "espresso_turbo_shot",
            name = "Espresso: Turbo Shot",
            type = RecipeType.ESPRESSO,
            coffeeAmounts = listOf(18),
            defaultCoffeeAmount = 18,
            supportedGrinders = listOf("Comandante C40", "Timemore C2", "Niche Zero"),
            equipment = listOf(
                Equipment("Flair 58"),
                Equipment("IMS basket"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "pour_over_matt_winton",
            name = "V60: Matt Winton",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = listOf(20),
            defaultCoffeeAmount = 20,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("V60 Ceramic"),
                Equipment("Hario Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "pour_over_hoffmann_v2",
            name = "V60: Hoffmann V2",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = listOf(18),
            defaultCoffeeAmount = 18,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("V60 Ceramic"),
                Equipment("Hario Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        )
    )

    override fun getAllRecipes(): List<Recipe> = recipes
    override fun getRecipeById(id: String): Recipe? = recipes.find { it.id == id }
    override fun getGrinders(): Map<String, Grinder> = grinders
    override fun getPourOverDrippers(): Map<String, PourOverDripper> = pourOverDrippers

    override fun getEspressoBrewers(): Map<String, EspressoBrewer> = espressoBrewers
}
