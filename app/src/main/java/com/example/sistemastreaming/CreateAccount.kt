package com.example.sistemastreaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sistemastreaming.Models.PostUser
import com.example.sistemastreaming.Networkings.ApiServiceInterface
import com.example.sistemastreaming.Networkings.RetrofitInstance
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {

    private val ApiService by lazy {
        RetrofitInstance.createService(ApiServiceInterface::class.java)
    }
    private lateinit var editTextNome: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var editTextConfirmarSenha: EditText
    private lateinit var buttonCriarConta: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.indexCreateAccount)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextNome = findViewById(R.id.editTextNome)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextSenha = findViewById(R.id.editTextSenha)
        editTextConfirmarSenha = findViewById(R.id.editTextConfirmarSenha)
        buttonCriarConta = findViewById(R.id.buttonCriarConta)

        buttonCriarConta.setOnClickListener {
            val nome = editTextNome.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString()
            val confirmarSenha = editTextConfirmarSenha.text.toString()

            if (validarCampos(nome, email, senha, confirmarSenha)) {
                val contentRequest = PostUser(nome, email, senha)
                lifecycleScope.launch {
                    val tokenJwt = fetchToken()
                    if (tokenJwt.isNotEmpty()) {
                        RetrofitInstance.jwtToken = tokenJwt
                        createUser(contentRequest)

                        Toast.makeText(this@CreateAccount, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateAccount, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
        }
    }
    private fun validarCampos(nome: String, email: String, senha: String, confirmarSenha: String): Boolean {
        if (nome.isEmpty()) {
            editTextNome.error = "Nome é obrigatório"
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Email inválido"
            return false
        }
        if (senha.isEmpty()) {
            editTextSenha.error = "Senha é obrigatória"
            return false
        }
        if (confirmarSenha.isEmpty()) {
            editTextConfirmarSenha.error = "Confirme sua senha"
            return false
        }
        if (senha != confirmarSenha) {
            editTextConfirmarSenha.error = "As senhas não coincidem"
            return false
        }
        return true
    }
    private suspend fun createUser(contentRequest: PostUser) {
        try {
            ApiService.postUser(contentRequest)
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao criar usuário: ${e.message}")
            throw e
        }
    }
    private suspend fun fetchToken(): String {
        var responseToken = ""
        try {
            val tokenResponse = ApiService.getToken()
            responseToken = tokenResponse.token.toString()
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao obter usuários: ${e.message}")
            throw e
        }
        return responseToken
    }

}