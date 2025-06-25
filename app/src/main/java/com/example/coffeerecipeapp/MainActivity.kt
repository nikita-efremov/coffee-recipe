package com.example.coffeerecipeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var coffeeAmountSpinner: Spinner
    private lateinit var grinderSpinner: Spinner
    private lateinit var equipmentList: TextView
    private lateinit var recipeSteps: TextView

    // Coffee amount options (in grams)
    private val coffeeAmounts = (12..45).toList().toIntArray()
    private val defaultCoffeeAmount = 30

    // Grinder options
    private val grinders = arrayOf("Comandante C40", "Timemore C2")
    private val grinderSettings = mapOf(
        "Comandante C40" to 23,
        "Timemore C2" to 21
    )
    private var selectedGrinder = grinders[0] // Default to Comandante C40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        coffeeAmountSpinner = findViewById(R.id.coffee_amount_spinner)
        grinderSpinner = findViewById(R.id.grinder_spinner)
        equipmentList = findViewById(R.id.equipment_list)
        recipeSteps = findViewById(R.id.recipe_steps)

        // Setup spinners
        setupCoffeeAmountSpinner()
        setupGrinderSpinner()

        // Set initial values
        updateRecipeValues(defaultCoffeeAmount)
    }

    private fun setupCoffeeAmountSpinner() {
        // Create spinner adapter with simple custom approach
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            coffeeAmounts.map { "${it}g" }
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coffeeAmountSpinner.adapter = adapter

        // Set default selection
        val defaultIndex = coffeeAmounts.indexOf(defaultCoffeeAmount)
        coffeeAmountSpinner.setSelection(defaultIndex)

        // Set listener for changes
        coffeeAmountSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Set text color for selected item
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                updateRecipeValues(coffeeAmounts[position])
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupGrinderSpinner() {
        // Create grinder spinner adapter
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            grinders
        ) {
            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(getColor(R.color.primary_text_color))
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        grinderSpinner.adapter = adapter

        // Set default selection (Comandante C40)
        grinderSpinner.setSelection(0)

        // Set listener for changes
        grinderSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Set text color for selected item
                (view as? TextView)?.setTextColor(getColor(R.color.primary_text_color))
                selectedGrinder = grinders[position]

                // Update recipe with current coffee amount when grinder changes
                val currentCoffeeAmount = coffeeAmounts[coffeeAmountSpinner.selectedItemPosition]
                updateRecipeValues(currentCoffeeAmount)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun updateRecipeValues(coffeeGrams: Int) {
        // Calculate dependent values
        val waterVolume = coffeeGrams * 16  // Total water
        val firstPour = coffeeGrams * 3     // Step 4: first pour
        val secondPourTarget = coffeeGrams * 10  // Step 6: pour to this amount
        val finalPourTarget = waterVolume   // Step 6: final amount (same as total water)

        // Get grind setting for selected grinder
        val grindSetting = grinderSettings[selectedGrinder] ?: 23

        // Update equipment list
        val equipmentText = """• Ceramic V60
- Cafec Abaca filter
- 100 degrees water
- ${coffeeGrams}g coffee
- ${waterVolume}ml water
- ${grindSetting} clicks on ${selectedGrinder}"""

        equipmentList.text = equipmentText

        // Update recipe steps
        val stepsText = """1. Put Ceramic V60 on kettle during boiling (without filter)

2. After water is boiled, put Ceramic V60 on carafe, insert filter and additionally preheat with 300ml water

3. Put ${coffeeGrams}g grinder coffee, make a hole in coffee bed

4. Pour ${firstPour}ml water, stir thoroughly

5. Wait until all coffee is dripped

6. Pour relatively fast till ${secondPourTarget}ml, then relatively slow till ${finalPourTarget}ml

7. Do a gentle swirl"""

        recipeSteps.text = stepsText
    }
}