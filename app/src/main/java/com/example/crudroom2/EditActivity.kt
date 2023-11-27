package com.example.crudroom2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.crudroom2.databinding.ActivityEditBinding
import com.example.crudroom2.firebase.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private var noteId:String = "0"
    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduans")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setupView()
        setContentView(binding.root)
        setupListener()
    }

    private fun setupView(){
        val intentType = intent.getIntExtra("intentType",0)
        when(intentType){
//            View
            0->{
                binding.buttonSave.visibility = View.GONE
                getNote()
            }
//            Update
            2->{
                binding.buttonSave.visibility = View.GONE
                binding.buttonUpdate.visibility = View.VISIBLE
                getNote()

            }
        }

    }

    private fun setupListener(){
        with(binding){
            buttonSave.setOnClickListener{
                val nama = editNama.text.toString()
                val judul = editTitle.text.toString()
                val isi = editNote.text.toString()
                val pengaduan  = Pengaduan("0",nama,judul,isi)

                pengaduanCollectionRef.add(pengaduan)
                    .addOnSuccessListener { documentReference ->
                        pengaduan.id = documentReference.id
                        Toast.makeText(applicationContext,"Data berhasil ditambahkan",Toast.LENGTH_LONG).show()
                        finish()
                        documentReference.set(pengaduan)
                            .addOnFailureListener {
                                Log.d("MainActivity", "Error updating budget ID: ", it)
                            }
                    }
                    .addOnFailureListener {
                        Log.d("MainActivity", "Error adding budget: ", it)
                    }
            }

            buttonUpdate.setOnClickListener{
                val noteRef = pengaduanCollectionRef.document(noteId)

                val namaValue = editNama.text.toString()
                val titleValue = editTitle.text.toString()
                val isiValue = editNote.text.toString()

                noteRef.update(mapOf(
                    "nama" to namaValue,
                    "judul" to titleValue,
                    "isi" to isiValue
                ))
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Data successfully updated!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w("FirestoreUpdate", "Error updating document", e)
                    }
            }

        }
    }

    private fun getNote(){
        noteId = intent.getStringExtra("id").toString()
        with(binding){
            pengaduanCollectionRef.whereEqualTo("id",noteId).get().addOnSuccessListener { documents ->

                for (document in documents) {
                    editNama.setText(document.getString("nama"))
                    editTitle.setText(document.getString("judul"))
                    editNote.setText(document.getString("isi"))
                }
            }
            .addOnFailureListener { exception ->
            }
        }
    }
}