package com.example.crudroom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudroom2.databinding.ActivityMainBinding
import com.example.crudroom2.firebase.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduans")
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val pengaduanListLiveData: MutableLiveData<List<Pengaduan>> by lazy {
        MutableLiveData<List<Pengaduan>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListener()
        setupRecyclerView()
        observeBudgets()
    }


    override fun onStart() {
        super.onStart()
        loadNote()
    }

    private fun observeBudgets() {
        pengaduanListLiveData.observe(this) { pengaduan ->
            noteAdapter.setData(pengaduan.toMutableList())
        }
    }
    private fun loadNote(){
        pengaduanCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MainActivity", "Error listening for budget changes: ", error)
                return@addSnapshotListener
            }
            val item = snapshots?.toObjects(Pengaduan::class.java)
            if (item != null) {
                pengaduanListLiveData.postValue(item)
            }
        }
    }
    private fun intentEdit(id:String, intentType:Int){
        startActivity(Intent(this@MainActivity,EditActivity::class.java).putExtra("id",id).putExtra("intentType",intentType))
    }

    private fun setupListener(){
        with(binding){
            buttonCreate.setOnClickListener{
                intentEdit("0",1)
            }
        }
    }

    private fun setupRecyclerView(){
        noteAdapter = NoteAdapter(arrayListOf(),object :NoteAdapter.OnAdapterListener{
            override fun onClick(pengaduan: Pengaduan) {
                intentEdit(pengaduan.id,0)
            }

            override fun onUpdate(pengaduan: Pengaduan) {
                intentEdit(pengaduan.id,2)
            }

            override fun onDelete(pengaduan: Pengaduan) {
                deleteDialog(pengaduan)
            }

        })
        with(binding){
            listNote.apply {
                layoutManager= LinearLayoutManager(this@MainActivity)
                adapter=noteAdapter
            }
        }
    }
    private fun deletePengaduan(pengaduan: Pengaduan) {
        if (pengaduan.id.isEmpty()) {
            Log.d("MainActivity", "Error deleting: budget ID is empty!")
            return
        }
        pengaduanCollectionRef.document(pengaduan.id).delete()
            .addOnFailureListener {
                Log.d("MainActivity", "Error deleting budget: ", it)
            }
    }
    private fun deleteDialog(pengaduan: Pengaduan){
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Apakah anda yakin?")
            setNegativeButton("Batal"){dialogInterface,i->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){dialogInterface,i->
                dialogInterface.dismiss()
                deletePengaduan(pengaduan)
            }
        }
        alertDialog.show()
    }
}