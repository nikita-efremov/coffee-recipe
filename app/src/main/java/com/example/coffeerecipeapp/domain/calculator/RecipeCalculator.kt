package com.example.coffeerecipeapp.domain.calculator

import com.example.coffeerecipeapp.domain.model.*

interface RecipeCalculator {
    fun calculateRecipe(configuration: RecipeConfiguration): RecipeResult
    fun canCalculate(recipeType: RecipeType): Boolean
}