package com.example.sistemastreaming

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sistemastreaming.Models.User
import com.example.sistemastreaming.Networkings.NetworkChecker
import com.example.sistemastreaming.Networkings.RetrofitInstance
import com.example.sistemastreaming.Networkings.ApiServiceInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var CriarConta: TextView
    lateinit var loginBtn: Button

    private val ApiService by lazy {
        RetrofitInstance.createService(ApiServiceInterface::class.java)
    }

    private val networkChecker by lazy {
        NetworkChecker(this,getSystemService(ConnectivityManager::class.java))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInput = findViewById(R.id.inputEmail)
        CriarConta = findViewById(R.id.textView4)
        passwordInput = findViewById(R.id.inputSenha)
        loginBtn = findViewById(R.id.btnLogin)

            CriarConta.setOnClickListener {
                val intent = Intent(this@MainActivity, CreateAccount::class.java)
                startActivity(intent)
            }


        loginBtn.setOnClickListener {
            networkChecker.performAction {
                val emailInput = emailInput.text.toString()
                val passwordInput = passwordInput.text.toString()

                if (emailInput.isNotEmpty() || passwordInput.isNotEmpty()) {
                    lifecycleScope.launch {
                        try {
                            val tokenJwt = fetchToken()
                            if (tokenJwt.isNotEmpty()) {
                                RetrofitInstance.jwtToken = tokenJwt
                                try {
                                    val user = fetchUser(emailInput)
                                    var saltedPassword = sha256(passwordInput + user.salt)
                                    if (user.senha == saltedPassword.uppercase()) {

                                        val intent =
                                            Intent(this@MainActivity, IndexActivity::class.java)
                                        intent.putExtra("USER_NAME", user.name)
                                        intent.putExtra("USER_EMAIL", user.email)
                                        intent.putExtra("TOKEN_JWT", tokenJwt)
                                        intent.putExtra("USER_ID", user.userId)
                                        startActivity(intent)
                                        finish()

                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Senha incorreta!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("API Error", "Erro ao obter o Usuario: ${e.message}")
                                    if (e.message.toString().contains("404")) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Email não existe na base de dados!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        } catch (e: Exception) {
                            Log.e("API Error", "Erro ao obter o token: ${e.message}")
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Preencha todos os campos obrigatórios!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
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

    private suspend fun fetchUser(email: String): User {
        var responseUser: User
        try {
            val response = ApiService.getUser(email)
            responseUser = response
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao obter usuários: ${e.message}")
            throw e
        }
        return responseUser
    }
    fun sha256(input: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(input.toByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }
}