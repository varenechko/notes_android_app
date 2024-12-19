package com.example.notes

import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    private val notes = mutableListOf<String>()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создание корневого контейнера
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
        }

        // Создание ListView
        listView = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1f
            )
        }
        rootLayout.addView(listView)

        // Создание кнопки добавления заметки
        val addButton = Button(this).apply {
            text = "Добавить заметку"
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        rootLayout.addView(addButton)

        // Установка адаптера для ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listView.adapter = adapter

        // Обработчик для добавления новой заметки
        addButton.setOnClickListener {
            showNoteDialog(-1)
        }

        // Обработчик для редактирования заметок по клику
        listView.setOnItemClickListener { _, _, position, _ ->
            showNoteDialog(position)
        }

        // Обработчик для удаления заметок по долгому нажатию
        listView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteDialog(position)
            true
        }

        // Установка корневого макета
        setContentView(rootLayout)
    }

    private fun showNoteDialog(position: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(if (position == -1) "Новая заметка" else "Редактировать заметку")

        val input = EditText(this).apply {
            if (position != -1) setText(notes[position])
        }

        dialogBuilder.setView(input)

        dialogBuilder.setPositiveButton("Сохранить") { _, _ ->
            val noteText = input.text.toString().trim()
            if (noteText.isNotEmpty()) {
                if (position == -1) {
                    notes.add(noteText)
                } else {
                    notes[position] = noteText
                }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Заметка не может быть пустой", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBuilder.setNegativeButton("Отмена", null)
        dialogBuilder.create().show()
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Удалить заметку?")
            .setMessage("Вы уверены, что хотите удалить эту заметку?")
            .setPositiveButton("Да") { _, _ ->
                notes.removeAt(position)
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Нет", null)
            .create()
            .show()
    }
}
