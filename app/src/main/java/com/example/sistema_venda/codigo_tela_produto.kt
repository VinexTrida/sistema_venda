package com.example.sistema_venda

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class codigo_tela_produto : AppCompatActivity() {
    fun funcao_voltar(view: View){
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o link com o arquivo xml da tela de venda
        setContentView(R.layout.tela_produto)
        // Tira a bara no topo do app
        supportActionBar?.hide()
    }
}