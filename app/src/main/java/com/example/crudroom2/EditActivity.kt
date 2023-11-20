package com.example.crudroom2

import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.crudroom2.database.Note
import com.example.crudroom2.database.NoteDB
import com.example.crudroom2.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class EditActivity : AppCompatActivity(),OnDateSetListener {
    private lateinit var binding: ActivityEditBinding
    private var noteId:Int = 0

    private val db by lazy { NoteDB(this) }

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
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().insertNote(
                        Note(id = 0,editTitle.text.toString(),editNote.text.toString(),setTanggal.text.toString())
                    )
                    finish()
                }
            }

            buttonUpdate.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().updateNote(
                        Note(id = noteId,editTitle.text.toString(),editNote.text.toString(),setTanggal.text.toString())
                    )
                    finish()
                }
            }

            btPickDate.setOnClickListener {
                val mDatePickerDialogFragment: com.example.crudroom2.DatePicker = DatePicker()
                mDatePickerDialogFragment.show(supportFragmentManager, "DATE PICK")
            }
        }
    }

    private fun getNote(){
        noteId = intent.getIntExtra("id",0)
        with(binding){
            CoroutineScope(Dispatchers.IO).launch {
                val notes = db.noteDao().getNote(noteId)[0]
                editTitle.setText(notes.title)
                editNote.setText(notes.note)
                setTanggal.text = notes.date
            }
        }
    }

    override fun onDateSet(
        view: android.widget.DatePicker,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val mCalendar = Calendar.getInstance()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        val selectedDate = DateFormat.getDateInstance(DateFormat.LONG).format(mCalendar.time)
        val text : TextView = findViewById(R.id.setTanggal)
        text.text = selectedDate
    }
}