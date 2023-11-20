package com.example.crudroom2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudroom2.database.Note
import com.example.crudroom2.databinding.NoteItemBinding

class NoteAdapter (private val note: ArrayList<Note>,private val listener:OnAdapterListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding:NoteItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return note.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.apply {
            textTitle.text= note[position].title
            textTitle.setOnClickListener(){
                listener.onClick(note[position])
            }
            iconEdit.setOnClickListener(){
                listener.onUpdate(note[position])
            }
            iconDelete.setOnClickListener(){
                listener.onDelete(note[position])
            }
        }
    }

    fun setData(list: List<Note>){
        note.clear()
        note.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(note:Note)
        fun onUpdate(note: Note)
        fun onDelete(note: Note)
    }
}