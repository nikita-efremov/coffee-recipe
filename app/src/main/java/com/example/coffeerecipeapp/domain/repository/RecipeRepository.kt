package com.example.coffeerecipeapp.domain.repository

import com.example.coffeerecipeapp.domain.model.*

interface RecipeRepository {
    fun getAllRecipes(): List<Recipe>
    fun getRecipeById(id: String): Recipe?
    fun getGrinders(): Map<String, Grinder>
    fun getPourOverDrippers(): Map<String, PourOverDripper>
    fun getEspressoBrewers(): Map<String, EspressoBrewer>
}
