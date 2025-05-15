package com.vasu.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var titleLayout: TextInputLayout
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var saveButton: MaterialButton

    private var notePosition = -1
    private var existingDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        toolbar = findViewById(R.id.toolbar)
        titleLayout = findViewById(R.id.titleInputLayout)
        titleInput = findViewById(R.id.titleEditText)
        descriptionLayout = findViewById(R.id.descInputLayout)
        descriptionInput = findViewById(R.id.descEditText)
        saveButton = findViewById(R.id.saveNoteBtn)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        notePosition = intent.getIntExtra("index", -1)

        if (notePosition != -1) {
            supportActionBar?.title = "Edit Note"

            val title = intent.getStringExtra("title") ?: ""
            val description = intent.getStringExtra("desc") ?: ""
            existingDate = intent.getStringExtra("date")

            titleInput.setText(title)
            descriptionInput.setText(description)
        } else {
            supportActionBar?.title = "Add New Note"
        }

        saveButton.setOnClickListener {
            saveNoteAndReturn()
        }
    }

    private fun saveNoteAndReturn() {
        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()

        if (title.isEmpty()) {
            titleLayout.error = "Title cannot be empty"
            return
        } else {
            titleLayout.error = null
        }

        val date = existingDate ?: getCurrentDate()

        val message = if (notePosition != -1) {
            "Note updated successfully"
        } else {
            "Note saved successfully"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val resultIntent = Intent()
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("desc", description)
        resultIntent.putExtra("date", date)
        resultIntent.putExtra("index", notePosition)

        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}