package com.example.mobilelab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val products = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val addButton: Button = findViewById(R.id.add_button)
        val calculateButton: Button = findViewById(R.id.calculate_button)

        adapter = ProductAdapter(products) { product ->
            showEditDialog(product)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            showAddDialog()
        }

        calculateButton.setOnClickListener {
            calculateTotalPriceForCurrentYear()
        }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.edit_name)
        val manufacturerEditText: EditText = dialogView.findViewById(R.id.edit_manufacturer)
        val yearEditText: EditText = dialogView.findViewById(R.id.edit_year)
        val quantityEditText: EditText = dialogView.findViewById(R.id.edit_quantity)
        val priceEditText: EditText = dialogView.findViewById(R.id.edit_price)

        AlertDialog.Builder(this)
            .setTitle("Добавить продукт")
            .setView(dialogView)
            .setPositiveButton("Добавить") { dialog, _ ->
                val name = nameEditText.text.toString()
                val manufacturer = manufacturerEditText.text.toString()
                val year = yearEditText.text.toString().toInt()
                val quantity = quantityEditText.text.toString().toInt()
                val price = priceEditText.text.toString().toDouble()
                val product = Product(name, manufacturer, year, quantity, price)
                products.add(product)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showEditDialog(product: Product) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.edit_name)
        val manufacturerEditText: EditText = dialogView.findViewById(R.id.edit_manufacturer)
        val yearEditText: EditText = dialogView.findViewById(R.id.edit_year)
        val quantityEditText: EditText = dialogView.findViewById(R.id.edit_quantity)
        val priceEditText: EditText = dialogView.findViewById(R.id.edit_price)

        nameEditText.setText(product.name)
        manufacturerEditText.setText(product.manufacturer)
        yearEditText.setText(product.year.toString())
        quantityEditText.setText(product.quantity.toString())
        priceEditText.setText(product.price.toString())

        AlertDialog.Builder(this)
            .setTitle("Редактировать продукт")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { dialog, _ ->
                product.name = nameEditText.text.toString()
                product.manufacturer = manufacturerEditText.text.toString()
                product.year = yearEditText.text.toString().toInt()
                product.quantity = quantityEditText.text.toString().toInt()
                product.price = priceEditText.text.toString().toDouble()
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Удалить") { dialog, _ ->
                products.remove(product)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun calculateTotalPriceForCurrentYear() {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val total = products.filter { it.year == currentYear }.sumOf { it.price * it.quantity }
        AlertDialog.Builder(this)
            .setTitle("Общая стоимость")
            .setMessage("Общая стоимость товаров, выпущенных в $currentYear году: $total")
            .setPositiveButton("ОК") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
