package com.example.crudroom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudroom2.database.Note
import com.example.crudroom2.database.NoteDB
import com.example.crudroom2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db by lazy { NoteDB(this) }
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
        setupRecyclerView()
    }


    override fun onStart() {
        super.onStart()
        loadNote()
    }

    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getAllNotes()
            Log.d("Main","DBres : $notes")
            withContext(Dispatchers.Main){
                noteAdapter.setData(notes)
            }
        }
    }
    private fun intentEdit(id:Int, intentType:Int){
        startActivity(Intent(this@MainActivity,EditActivity::class.java).putExtra("id",id).putExtra("intentType",intentType))
    }

    private fun setupListener(){
        with(binding){
            buttonCreate.setOnClickListener{
                intentEdit(0,1)
            }
        }
    }

    private fun setupRecyclerView(){
        noteAdapter = NoteAdapter(arrayListOf(),object :NoteAdapter.OnAdapterListener{
            override fun onClick(note: Note) {
                intentEdit(note.id,0)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id,2)
            }

            override fun onDelete(note: Note) {
                deleteDialog(note)
            }

        })
        with(binding){
            listNote.apply {
                layoutManager= LinearLayoutManager(this@MainActivity)
                adapter=noteAdapter
            }
        }
    }

    private fun deleteDialog(note: Note){
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Apakah anda yakin?")
            setNegativeButton("Batal"){dialogInterface,i->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){dialogInterface,i->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    loadNote()
                }
            }
        }
        alertDialog.show()
    }
}