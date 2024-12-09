package com.example.pruebafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebafirebase.Adapter.AdapterVentilador
import com.example.pruebafirebase.Models.Ventiladores
import com.example.pruebafirebase.databinding.ActivityVerVentiladoresBinding
import com.google.firebase.database.*

class VerVentiladores : AppCompatActivity() {

    private lateinit var binding: ActivityVerVentiladoresBinding
    private var ventiladoresList: MutableList<Ventiladores> = mutableListOf() // Lista mutable
    private lateinit var adapter: AdapterVentilador
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("ventiladores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerVentiladoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el diseño del sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el menú de navegación inferior
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    val intent = Intent(this, PostLogin::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_2 -> {
                    Toast.makeText(this, "Ya estás en Ver ventiladores", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_3 -> {
                    val intent = Intent(this, Ventilador::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Inicializar el adaptador con una lista mutable
        adapter = AdapterVentilador(ventiladoresList)
        binding.rvVentiladores.layoutManager = LinearLayoutManager(this)
        binding.rvVentiladores.adapter = adapter

        // Cargar los ventiladores registrados desde Firebase
        loadVentiladores()
    }

    private fun loadVentiladores() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ventiladoresList.clear() // Limpiar la lista antes de agregar nuevos elementos
                    for (ventiladorSnapshot in snapshot.children) {
                        val ventilador = ventiladorSnapshot.getValue(Ventiladores::class.java)
                        ventilador?.let {
                            ventiladoresList.add(it) // Agregar el ventilador a la lista mutable
                        }
                    }

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged() // Actualizar el RecyclerView
                } else {
                    Toast.makeText(this@VerVentiladores, "No hay ventiladores registrados", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("VerVentiladores", "Error al cargar los ventiladores: ${error.message}")
                Toast.makeText(this@VerVentiladores, "Error al cargar los ventiladores", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
