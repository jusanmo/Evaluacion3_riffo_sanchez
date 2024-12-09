package com.example.pruebafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebafirebase.databinding.ActivityPostLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class PostLogin : AppCompatActivity() {

    //Configurar viewBinding
    private lateinit var binding: ActivityPostLoginBinding

    //Configurar firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityPostLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar el menú de navegación inferior
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_2 -> {
                    val intent = Intent(this, VerVentiladores::class.java)
                    startActivity(intent) // Cambia a la actividad de Configuración
                    true
                }
                R.id.item_3 -> {
                    val intent = Intent(this, Ventilador::class.java)
                    startActivity(intent) // Cambia a la actividad de Perfil
                    true
                }
                else -> false
            }
        }

        // Botón para cerrar sesión
        binding.btnLogout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        try {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: FirebaseAuthException) {
            Toast.makeText(this, "Error al cerrar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
