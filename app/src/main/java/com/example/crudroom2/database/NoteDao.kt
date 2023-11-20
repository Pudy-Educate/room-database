package com.example.crudroom2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes():List<Note>

    @Query("SELECT * FROM notes WHERE id=:note_id")
    suspend fun getNote(note_id: Int):List<Note>
}