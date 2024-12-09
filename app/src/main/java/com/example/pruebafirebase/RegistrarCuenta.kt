package com.example.pruebafirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebafirebase.databinding.ActivityRegistrarBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrarCuenta : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar viewBinding
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth y Firestore
        auth = Firebase.auth


        // Configurar el botón de registro
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString().trim()

            // Validar los campos
            if (email.isEmpty()) {
                binding.etEmailRegister.error = "El correo es requerido"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPasswordRegister.error = "La contraseña es requerida"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etPasswordRegister.error = "La contraseña debe tener al menos 6 caracteres"
                return@setOnClickListener
            }

            // Registrar usuario en Firebase
            registrarUsuarioConFirebase(email, password)
        }

        // Redirigir a la pantalla de inicio de sesión
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun registrarUsuarioConFirebase(correo: String, contrasena: String) {
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    Snackbar.make(binding.root, "Cuenta creada con éxito.", Snackbar.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Error en el registro
                    Snackbar.make(
                        binding.root,
                        "Hubo un problema al registrar tu cuenta. Inténtalo nuevamente.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                // Manejo de errores
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}
