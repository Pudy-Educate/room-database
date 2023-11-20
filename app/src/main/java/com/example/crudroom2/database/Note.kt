package com.example.crudroom2.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title:String,
    val note:String,
    val date:String
)
