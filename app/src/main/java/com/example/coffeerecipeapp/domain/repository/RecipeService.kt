package com.example.coffeerecipeapp.domain.repository

import com.example.coffeerecipeapp.domain.calculator.RecipeCalculator
import com.example.coffeerecipeapp.domain.model.Recipe
import com.example.coffeerecipeapp.domain.model.RecipeConfiguration
import com.example.coffeerecipeapp.domain.model.RecipeResult

class RecipeService(
    private val repository: RecipeRepository,
    private val calculators: List<RecipeCalculator>
) {

    fun getAllRecipes(): List<Recipe> = repository.getAllRecipes()

    fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult {
        val calculator = calculators.find { it.canCalculate(configuration.recipe.type) }
            ?: throw IllegalArgumentException("No calculator found for recipe type: ${configuration.recipe.type}")

        return calculator.calculateRecipe(configuration)
    }

    fun getAvailableGrinders(): List<String> =
        repository.getGrinders().keys.toList()

    fun getAvailableDrippers(): List<String> =
        repository.getPourOverDrippers().keys.toList()

    fun getAvailableEspressoBrewers(): List<String> =
        repository.getEspressoBrewers().keys.toList()
}