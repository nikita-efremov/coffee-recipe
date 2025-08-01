package com.example.coffeerecipeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var recipeSpinner: Spinner
    private lateinit var coffeeAmountSpinner: Spinner
    private lateinit var grinderSpinner: Spinner
    private lateinit var pourOverSpinner: Spinner
    private lateinit var equipmentList: TextView
    private lateinit var recipeSteps: TextView

    // Recipe names
    private val recipes = arrayOf("Pour Over Recipe", "Aeropress V2 (Tim Wendelboe)")
    private var selectedRecipe = recipes[0]

    // Pour Over Recipe Data
    private val pourOverCoffeeAmounts = (12..45).toList().toIntArray()
    private val pourOverDefaultAmount = 30
    private val pourOverTypes = arrayOf("V60 Ceramic", "Kalita Tsubame")
    private var selectedPourOver = pourOverTypes[0]
    private val pourOverGrinders = arrayOf("Comandante C40", "Timemore C2")
    private var selectedPourOverGrinder = pourOverGrinders[0]

    private val pourOverGrinderSettings = mapOf(
        "Comandante C40" to 23,
        "Timemore C2" to 21
    )

    private val pourOverSettings = mapOf(
        "V60 Ceramic" to PourOverConfig(100, "Cafec Abaca"),
        "Kalita Tsubame" to PourOverConfig(93, "Kalita Wave")
    )

    // Aeropress V2 Recipe Data
    private val aeropressCoffeeAmounts = arrayOf(15, 18) // Only 15g and 18g as per recipe
    private val aeropressDefaultAmount = 15
    private val aeropressGrinders = arrayOf("Comandante C40", "Timemore C2")
    private var selectedAeropressGrinder = aeropressGrinders[0]

    private val aeropressGrinderSettings = mapOf(
        "Comandante C40" to 13, // 12-15 clicks average
        "Timemore C2" to 12
    )

    private val aeropressWaterAmounts = mapOf(
        15 to 210, // 15g -> 210ml
        18 to 257  // 18g -> 257ml
    )

    // Data classes
    data class PourOverConfig(val temperature: Int, val filter: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recipeSpinner = findViewById(R.id.recipe_spinner)
        coffeeAmountSpinner = findViewById(R.id.coffee_amount_spinner)
        grinderSpinner = findViewById(R.id.grinder_spinner)
        pourOverSpinner = findViewById(R.id.pour_over_spinner)
        equipmentList = findViewById(R.id.equipment_list)
        recipeSteps = findViewById(R.id.recipe_steps)

        // Setup spinners
        setupRecipeSpinner()
        setupSpinnersForCurrentRecipe()

        // Set initial values
        updateRecipeDisplay()
    }

    private fun setupRecipeSpinner() {
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            recipes
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        recipeSpinner.adapter = adapter

        recipeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedRecipe = recipes[position]
                setupSpinnersForCurrentRecipe()
                updateRecipeDisplay()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupSpinnersForCurrentRecipe() {
        when (selectedRecipe) {
            "Pour Over Recipe" -> {
                setupPourOverSpinners()
                pourOverSpinner.visibility = android.view.View.VISIBLE
            }
            "Aeropress V2 (Tim Wendelboe)" -> {
                setupAeropressSpinners()
                pourOverSpinner.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupPourOverSpinners() {
        // Setup coffee amount spinner for pour over
        val coffeeAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            pourOverCoffeeAmounts.map { "${it}g" }
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        coffeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coffeeAmountSpinner.adapter = coffeeAdapter

        val defaultIndex = pourOverCoffeeAmounts.indexOf(pourOverDefaultAmount)
        coffeeAmountSpinner.setSelection(defaultIndex)

        coffeeAmountSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                updateRecipeDisplay()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // Setup grinder spinner for pour over
        val grinderAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            pourOverGrinders
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        grinderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grinderSpinner.adapter = grinderAdapter
        grinderSpinner.setSelection(0)

        grinderSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedPourOverGrinder = pourOverGrinders[position]
                updateRecipeDisplay()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // Setup pour over type spinner
        val pourOverAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            pourOverTypes
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        pourOverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pourOverSpinner.adapter = pourOverAdapter
        pourOverSpinner.setSelection(0)

        pourOverSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedPourOver = pourOverTypes[position]
                updateRecipeDisplay()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupAeropressSpinners() {
        // Setup coffee amount spinner for Aeropress
        val coffeeAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            aeropressCoffeeAmounts.map { "${it}g" }
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        coffeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coffeeAmountSpinner.adapter = coffeeAdapter

        val defaultIndex = aeropressCoffeeAmounts.indexOf(aeropressDefaultAmount)
        coffeeAmountSpinner.setSelection(defaultIndex)

        coffeeAmountSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                updateRecipeDisplay()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // Setup grinder spinner for Aeropress
        val grinderAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            aeropressGrinders
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        grinderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grinderSpinner.adapter = grinderAdapter
        grinderSpinner.setSelection(0)

        grinderSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedAeropressGrinder = aeropressGrinders[position]
                updateRecipeDisplay()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun updateRecipeDisplay() {
        when (selectedRecipe) {
            "Pour Over Recipe" -> displayPourOverRecipe()
            "Aeropress V2 (Tim Wendelboe)" -> displayAeropressRecipe()
        }
    }

    private fun displayPourOverRecipe() {
        val coffeeGrams = pourOverCoffeeAmounts[coffeeAmountSpinner.selectedItemPosition]

        // Calculate dependent values
        val waterVolume = coffeeGrams * 16  // Total water
        val firstPour = coffeeGrams * 3     // Step 4: first pour
        val secondPourTarget = coffeeGrams * 10  // Step 6: pour to this amount
        val finalPourTarget = waterVolume   // Step 6: final amount (same as total water)

        // Get grind setting for selected grinder
        val grindSetting = pourOverGrinderSettings[selectedPourOverGrinder] ?: 23

        // Get pour over configuration for selected type
        val pourOverConfig = pourOverSettings[selectedPourOver] ?: PourOverConfig(100, "Cafec Abaca")

        // Update equipment list
        val equipmentText = """• ${selectedPourOver}
- ${pourOverConfig.filter} filter
- ${pourOverConfig.temperature}°C water
- ${coffeeGrams}g coffee
- ${waterVolume}ml water
- ${grindSetting} clicks ${selectedPourOverGrinder}"""

        equipmentList.text = equipmentText

        // Update recipe steps with pour over specific instructions
        val stepsText = when (selectedPourOver) {
            "V60 Ceramic" -> """1. Put Ceramic V60 on kettle during boiling (without filter)

2. After water is boiled, put Ceramic V60 on carafe, insert filter and additionally preheat with 300ml water

3. Put ${coffeeGrams}g grinder coffee, make a hole in coffee bed

4. Pour ${firstPour}ml water, stir thoroughly

5. Wait until all coffee is dripped

6. Pour relatively fast till ${secondPourTarget}ml, then relatively slow till ${finalPourTarget}ml

7. Do a gentle swirl"""

            "Kalita Tsubame" -> """1. Put Kalita Tsubame on kettle during heating (without filter)

2. After water reaches ${pourOverConfig.temperature}°C, put Kalita on carafe, insert filter and preheat with 300ml water

3. Put ${coffeeGrams}g grinder coffee, level the bed

4. Pour ${firstPour}ml water in circular motion, stir gently

5. Wait until all coffee is dripped

6. Pour in 3-4 pulses till ${secondPourTarget}ml, then slowly till ${finalPourTarget}ml

7. Let it finish dripping"""

            else -> """1. Put ${selectedPourOver} on kettle during heating (without filter)

2. After water reaches ${pourOverConfig.temperature}°C, put dripper on carafe, insert filter and preheat with 300ml water

3. Put ${coffeeGrams}g grinder coffee, prepare the bed

4. Pour ${firstPour}ml water, stir thoroughly

5. Wait until all coffee is dripped

6. Pour till ${secondPourTarget}ml, then slowly till ${finalPourTarget}ml

7. Finish brewing"""
        }

        recipeSteps.text = stepsText
    }

    private fun displayAeropressRecipe() {
        val coffeeGrams = aeropressCoffeeAmounts[coffeeAmountSpinner.selectedItemPosition]
        val waterVolume = aeropressWaterAmounts[coffeeGrams] ?: 210
        val grindSetting = aeropressGrinderSettings[selectedAeropressGrinder] ?: 13

        // Update equipment list
        val equipmentText = """• Aeropress V2
- Aeropress filter
- 96°C water
- ${coffeeGrams}g coffee
- ${waterVolume}ml water
- ${grindSetting} clicks ${selectedAeropressGrinder}"""

        equipmentList.text = equipmentText

        // Update recipe steps for Aeropress V2 (Tim Wendelboe)
        val grinderRange = when (selectedAeropressGrinder) {
            "Comandante C40" -> "12-15"
            "Timemore C2" -> "12"
            else -> "12-15"
        }

        val stepsText = """Aeropress V2 (Tim Wendelboe)
${coffeeGrams}g coffee - ${waterVolume}g water
Tuning by grind size
${grinderRange} clicks ${selectedAeropressGrinder}
96 degrees water

0:00 - Add ${waterVolume}ml water, 3 stirs along the diameter (back to front), insert plunger to stop water

1:00 - Remove plunger, 3 stirs along the diameter (back to front), insert plunger, push

1:30 - Should be finished

Tuning: Adjust by grind size"""

        recipeSteps.text = stepsText
    }
}