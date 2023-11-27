package com.example.crudroom2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudroom2.databinding.NoteItemBinding
import com.example.crudroom2.firebase.Pengaduan

class NoteAdapter (private val note: ArrayList<Pengaduan>, private val listener:OnAdapterListener):
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
            textNama.text= note[position].nama
            textTitle.text = note[position].judul
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

    fun setData(list: List<Pengaduan>){
        note.clear()
        note.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(pengaduan: Pengaduan)
        fun onUpdate(pengaduan: Pengaduan)
        fun onDelete(pengaduan: Pengaduan)
    }
}