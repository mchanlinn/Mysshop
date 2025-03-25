package net.thetoo.myshop

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter
    private lateinit var dbHelper: DBHelper
    private var itemList = mutableListOf<Triple<Int, String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        listView = findViewById(R.id.listView)
        loadData()

        val editButton: Button = findViewById(R.id.btn_edit)
        val saveButton: Button = findViewById(R.id.btn_save)
        val deleteButton: Button = findViewById(R.id.btn_delete)
        val addButton: Button = findViewById(R.id.btn_add)

        editButton.setOnClickListener { editItem() }
        saveButton.setOnClickListener { saveData() }
        deleteButton.setOnClickListener { deleteItem() }
        addButton.setOnClickListener { addItem() }
    }

    private fun loadData() {
        val data = dbHelper.getAllItems()
        itemList.clear()
        data.forEach { (id, number, name, cost) ->
            itemList.add(Triple(number, name, cost))
        }
        adapter = CustomAdapter(this, itemList)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
    }

    private fun editItem() {
        val position = listView.checkedItemPosition
        if (position != ListView.INVALID_POSITION) {
            val selectedItem = itemList[position]
            val (number, oldName, cost) = selectedItem

            val input = EditText(this)
            input.setText(oldName)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val updatedName = input.text.toString()
                    dbHelper.updateItem(position + 1, number, updatedName, cost)
                    itemList[position] = Triple(number, updatedName, cost)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun deleteItem() {
        val position = listView.checkedItemPosition
        if (position != ListView.INVALID_POSITION) {
            dbHelper.deleteItem(position + 1)
            itemList.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    }

    private fun addItem() {
        val inputNumber = EditText(this)
        val inputName = EditText(this)
        val inputCost = EditText(this)

        inputNumber.hint = "Enter Number"
        inputName.hint = "Enter Item Name"
        inputCost.hint = "Enter Cost"

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(inputNumber)
            addView(inputName)
            addView(inputCost)
        }

        AlertDialog.Builder(this)
            .setTitle("Add Item")
            .setView(layout)
            .setPositiveButton("OK") { _, _ ->
                val number = inputNumber.text.toString().toIntOrNull() ?: return@setPositiveButton
                val name = inputName.text.toString()
                val cost = inputCost.text.toString()

                dbHelper.insertItem(number, name, cost)
                itemList.add(Triple(number, name, cost))
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveData() {
        Toast.makeText(this, "Data saved to database", Toast.LENGTH_SHORT).show()
    }
}
