package com.example.coffeerecipeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeerecipeapp.domain.calculator.AeropressCalculator
import com.example.coffeerecipeapp.domain.calculator.PourOverCalculator
import com.example.coffeerecipeapp.domain.model.*
import com.example.coffeerecipeapp.domain.repository.RecipeDataSource
import com.example.coffeerecipeapp.domain.repository.RecipeService

class MainActivity : AppCompatActivity() {
    private lateinit var recipeSpinner: Spinner
    private lateinit var coffeeAmountSpinner: Spinner
    private lateinit var grinderSpinner: Spinner
    private lateinit var pourOverSpinner: Spinner
    private lateinit var pourOverLabel: TextView
    private lateinit var equipmentList: TextView
    private lateinit var recipeSteps: TextView
    private lateinit var recipeTitle: TextView

    // Dependencies - In a real app, use Dependency Injection (Hilt/Dagger)
    private val recipeRepository = RecipeDataSource()
    private val recipeService by lazy {
        val calculators = listOf(
            PourOverCalculator(recipeRepository.getGrinders(), recipeRepository.getPourOverDrippers()),
            AeropressCalculator(recipeRepository.getGrinders())
        )
        RecipeService(recipeRepository, calculators)
    }

    private var selectedRecipe: Recipe? = null
    private var selectedGrinder: String = ""
    private var selectedDripper: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupSpinners()
        setInitialValues()
    }

    private fun initializeViews() {
        recipeSpinner = findViewById(R.id.recipe_spinner)
        coffeeAmountSpinner = findViewById(R.id.coffee_amount_spinner)
        grinderSpinner = findViewById(R.id.grinder_spinner)
        pourOverSpinner = findViewById(R.id.pour_over_spinner)
        pourOverLabel = findViewById(R.id.pour_over_label)
        equipmentList = findViewById(R.id.equipment_list)
        recipeSteps = findViewById(R.id.recipe_steps)
        recipeTitle = findViewById(R.id.recipe_title)
    }

    private fun setupSpinners() {
        setupRecipeSpinner()
    }

    private fun setInitialValues() {
        val recipes = recipeService.getAllRecipes()
        if (recipes.isNotEmpty()) {
            selectedRecipe = recipes.first()
            updateUIForSelectedRecipe()
        }
    }

    private fun setupRecipeSpinner() {
        val recipes = recipeService.getAllRecipes()
        val recipeNames = recipes.map { it.name }

        val adapter = createSpinnerAdapter(recipeNames)
        recipeSpinner.adapter = adapter

        recipeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedRecipe = recipes[position]
                updateUIForSelectedRecipe()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun updateUIForSelectedRecipe() {
        selectedRecipe?.let { recipe ->
            // Update the title to show the selected recipe name
            recipeTitle.text = recipe.name

            setupCoffeeAmountSpinner(recipe)
            setupGrinderSpinner(recipe)
            setupEquipmentSpinner(recipe)
            updateRecipeDisplay()
        }
    }

    private fun setupCoffeeAmountSpinner(recipe: Recipe) {
        val coffeeAmounts = recipe.coffeeAmounts.map { "${it}g" }
        val adapter = createSpinnerAdapter(coffeeAmounts)
        coffeeAmountSpinner.adapter = adapter

        val defaultIndex = recipe.coffeeAmounts.indexOf(recipe.defaultCoffeeAmount)
        if (defaultIndex >= 0) {
            coffeeAmountSpinner.setSelection(defaultIndex)
        }

        coffeeAmountSpinner.onItemSelectedListener = createSimpleSelectionListener { updateRecipeDisplay() }
    }

    private fun setupGrinderSpinner(recipe: Recipe) {
        val adapter = createSpinnerAdapter(recipe.supportedGrinders)
        grinderSpinner.adapter = adapter

        if (recipe.supportedGrinders.isNotEmpty()) {
            selectedGrinder = recipe.supportedGrinders.first()
            grinderSpinner.setSelection(0)
        }

        grinderSpinner.onItemSelectedListener = createSimpleSelectionListener {
            selectedGrinder = recipe.supportedGrinders[grinderSpinner.selectedItemPosition]
            updateRecipeDisplay()
        }
    }

    private fun setupEquipmentSpinner(recipe: Recipe) {
        when (recipe.type) {
            RecipeType.POUR_OVER -> {
                val drippers = recipeService.getAvailableDrippers()
                val adapter = createSpinnerAdapter(drippers)
                pourOverSpinner.adapter = adapter
                pourOverSpinner.visibility = android.view.View.VISIBLE
                pourOverLabel.visibility = android.view.View.VISIBLE

                if (drippers.isNotEmpty()) {
                    selectedDripper = drippers.first()
                    pourOverSpinner.setSelection(0)
                }

                pourOverSpinner.onItemSelectedListener = createSimpleSelectionListener {
                    selectedDripper = drippers[pourOverSpinner.selectedItemPosition]
                    updateRecipeDisplay()
                }
            }
            else -> {
                pourOverSpinner.visibility = android.view.View.GONE
                pourOverLabel.visibility = android.view.View.GONE
                selectedDripper = ""
            }
        }
    }

    private fun updateRecipeDisplay() {
        selectedRecipe?.let { recipe ->
            try {
                val coffeeAmount = recipe.coffeeAmounts[coffeeAmountSpinner.selectedItemPosition]
                val equipmentMap = if (selectedDripper.isNotEmpty()) {
                    mapOf("dripper" to selectedDripper)
                } else {
                    emptyMap()
                }

                val configuration = RecipeConfiguration(
                    recipe = recipe,
                    coffeeAmount = coffeeAmount,
                    grinder = selectedGrinder,
                    specificEquipment = equipmentMap
                )

                val result = recipeService.calculateRecipe(configuration)
                displayRecipeResult(result)
            } catch (e: Exception) {
                displayError("Error calculating recipe: ${e.message}")
            }
        }
    }

    private fun displayRecipeResult(result: RecipeResult) {
        equipmentList.text = result.equipment.joinToString("\n")

        val stepsText = result.steps.joinToString("\n\n") { step ->
            val timing = step.timing?.let { "$it - " } ?: ""
            "$timing${step.instruction}"
        }
        recipeSteps.text = stepsText
    }

    private fun displayError(message: String) {
        equipmentList.text = "Error loading equipment"
        recipeSteps.text = message
    }

    private fun createSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
        return object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            items
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }.apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun createSimpleSelectionListener(onSelected: () -> Unit): android.widget.AdapterView.OnItemSelectedListener {
        return object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                onSelected()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }
}