package com.example.coffeerecipeapp.domain.repository

import com.example.coffeerecipeapp.domain.model.Equipment
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
                "aeropress_tim_wendelboe" to 13
            )
        ),
        "Timemore C2" to Grinder(
            name = "Timemore C2",
            clickSettings = mapOf(
                "pour_over_lance_hedrick" to 21,
                "aeropress_tim_wendelboe" to 12
            )
        )
    )

    private val pourOverDrippers = mapOf(
        "V60 Ceramic" to PourOverDripper(
            name = "V60 Ceramic",
            temperature = 100,
            filter = "Cafec Abaca"
        ),
        "Kalita Tsubame" to PourOverDripper(
            name = "Kalita Tsubame",
            temperature = 93,
            filter = "Kalita Wave"
        )
    )

    private val recipes = listOf(
        Recipe(
            id = "pour_over_lance_hedrick",
            name = "Pour Over: Lance Hedrick (mod)",
            type = RecipeType.POUR_OVER,
            coffeeAmounts = (12..45).toList(),
            defaultCoffeeAmount = 30,
            supportedGrinders = listOf("Comandante C40", "Timemore C2"),
            equipment = listOf(
                Equipment("Pour Over Dripper", alternatives = listOf("V60 Ceramic", "Kalita Tsubame")),
                Equipment("Filter"),
                Equipment("Kettle"),
                Equipment("Scale"),
                Equipment("Timer")
            )
        ),
        Recipe(
            id = "aeropress_tim_wendelboe",
            name = "Aeropress: Tim Wendelboe (orig)",
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
        )
        // Easy to add more recipes here!
    )

    override fun getAllRecipes(): List<Recipe> = recipes

    override fun getRecipeById(id: String): Recipe? =
        recipes.find { it.id == id }

    override fun getGrinders(): Map<String, Grinder> = grinders

    override fun getPourOverDrippers(): Map<String, PourOverDripper> = pourOverDrippers
}