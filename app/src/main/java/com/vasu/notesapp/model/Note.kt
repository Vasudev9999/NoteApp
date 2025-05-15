package com.vasu.notesapp.model

data class Note(
    val title: String,           // Title of the note
    val description: String,     // Description of the note
    val date: String = ""        // Date of the note (optional with default value)
)