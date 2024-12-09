package com.example.pruebafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebafirebase.Models.Ventiladores
import com.example.pruebafirebase.databinding.ActivityVentiladorBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Ventilador : AppCompatActivity() {

    private lateinit var binding: ActivityVentiladorBinding
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("ventiladores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentiladorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Ventilador", "Activity creada correctamente")

        // Configuración del botón de guardar
        binding.btnGuardarConfig.setOnClickListener {
            Log.d("Ventilador", "Botón Guardar presionado")
            guardarVentilador()
        }

        // Configurar el menú de navegación inferior
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    val intent = Intent(this, PostLogin::class.java)
                    startActivity(intent) // Cambia a la actividad de Perfil
                    true
                }
                R.id.item_2 -> {
                    val intent = Intent(this, VerVentiladores::class.java)
                    startActivity(intent) // Cambia a la actividad de Configuración
                    true
                }
                R.id.item_3 -> {
                    Toast.makeText(this, "Ya estás en Ventilador", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun guardarVentilador() {
        val nombre = binding.etNombre.text.toString().trim()
        val hora = binding.etHora.text.toString().trim()
        val temperatura = binding.etTemperatura.text.toString().trim()

        // Validación de campos vacíos
        if (nombre.isEmpty() || hora.isEmpty() || temperatura.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            Log.w("Ventilador", "Campos vacíos detectados")
            return
        }

        // Validación de formato de hora
        val regexHora = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        if (!regexHora.matches(hora)) {
            Toast.makeText(this, "Ingrese una hora válida en formato HH:mm", Toast.LENGTH_SHORT).show()
            Log.w("Ventilador", "Hora inválida ingresada: $hora")
            return
        }

        // Validación de temperatura como número
        try {
            val temperaturaNumerica = temperatura.toFloat()
            Log.d("Ventilador", "Temperatura válida: $temperaturaNumerica")
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Ingrese una temperatura válida", Toast.LENGTH_SHORT).show()
            Log.e("Ventilador", "Temperatura inválida ingresada: $temperatura")
            return
        }

        // Generar un ID único para el ventilador
        val id = database.push().key
        if (id == null) {
            Log.e("Ventilador", "No se pudo generar un ID único")
            Toast.makeText(this, "Error al generar el ID", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto Ventilador
        val ventilador = Ventiladores(id, nombre, hora, temperatura)
        Log.d("Ventilador", "Objeto creado: $ventilador")

        // Guardar en Firebase
        database.child(id).setValue(ventilador)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Ventilador", "Datos guardados exitosamente en Firebase")
                    Toast.makeText(this, "Ventilador guardado correctamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                } else {
                    Log.e("Ventilador", "Error al guardar: ${task.exception?.message}")
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Ventilador", "Error de Firebase: ${exception.message}")
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarCampos() {
        binding.etNombre.text?.clear()
        binding.etHora.text?.clear()
        binding.etTemperatura.text?.clear()
        Log.d("Ventilador", "Campos limpiados correctamente")
    }
}
