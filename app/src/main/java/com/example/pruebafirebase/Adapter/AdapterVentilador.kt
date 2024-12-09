package com.example.pruebafirebase.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebafirebase.Models.Ventiladores
import com.example.pruebafirebase.R

class AdapterVentilador(private var ventiladorList: MutableList<Ventiladores>) :
    RecyclerView.Adapter<AdapterVentilador.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvVentiladorTitulo)
        val hora: TextView = itemView.findViewById(R.id.tvHoraVentilador)
        val temperatura: TextView = itemView.findViewById(R.id.tvTemperaturaVentilador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ventilador, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ventilador = ventiladorList[position]
        holder.nombre.text = ventilador.nombre
        holder.hora.text = ventilador.hora
        holder.temperatura.text = ventilador.temperatura
    }

    override fun getItemCount(): Int {
        return ventiladorList.size
    }

    // Método para actualizar la lista de ventiladores
    fun updateVentiladores(newVentiladores: List<Ventiladores>) {
        ventiladorList.clear()
        ventiladorList.addAll(newVentiladores)
        notifyDataSetChanged()
    }
}
