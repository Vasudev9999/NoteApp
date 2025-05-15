package com.vasu.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vasu.notesapp.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var addNoteFab: FloatingActionButton
    private lateinit var adapter: NoteAdapter
    private val notesList = ArrayList<Note>()

    private val ADD_NOTE_REQUEST_CODE = 101
    private val EDIT_NOTE_REQUEST_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.notesRecyclerView)
        addNoteFab = findViewById(R.id.addNoteFab)

        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(
            notesList,
            { position -> editNote(position) },
            { position -> showDeleteConfirmationDialog(position) }
        )
        recyclerView.adapter = adapter

        addNoteFab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE)
        }
    }

    private fun editNote(position: Int) {
        val intent = Intent(this, AddNoteActivity::class.java)

        val note = notesList[position]
        intent.putExtra("title", note.title)
        intent.putExtra("desc", note.description)
        intent.putExtra("date", note.date)
        intent.putExtra("index", position)

        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val note = notesList[position]

        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete '${note.title}'?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteNote(position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteNote(position: Int) {
        notesList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val title = data.getStringExtra("title") ?: ""
            val description = data.getStringExtra("desc") ?: ""
            val date = data.getStringExtra("date") ?: getCurrentDate()

            when (requestCode) {
                ADD_NOTE_REQUEST_CODE -> {
                    notesList.add(Note(title, description, date))
                    adapter.notifyItemInserted(notesList.size - 1)
                }
                EDIT_NOTE_REQUEST_CODE -> {
                    val position = data.getIntExtra("index", -1)
                    if (position != -1) {
                        notesList[position] = Note(title, description, date)
                        adapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}