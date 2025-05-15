package com.vasu.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vasu.notesapp.model.Note

class NoteAdapter(
    private val notes: List<Note>,
    private val onNoteClick: (position: Int) -> Unit,
    private val onNoteLongClick: (position: Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.noteTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.noteDate)
        val descriptionTextView: TextView = itemView.findViewById(R.id.noteDescription)

        init {
            itemView.setOnClickListener {
                onNoteClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                onNoteLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.description

        if (note is Note && note::class.java.getDeclaredFields().any { it.name == "date" }) {
            holder.dateTextView.visibility = View.VISIBLE
            try {
                val dateField = note::class.java.getDeclaredField("date")
                dateField.isAccessible = true
                val date = dateField.get(note) as? String
                holder.dateTextView.text = date ?: ""
            } catch (e: Exception) {
                holder.dateTextView.visibility = View.GONE
            }
        } else {
            holder.dateTextView.visibility = View.GONE
        }
    }

    override fun getItemCount() = notes.size
}