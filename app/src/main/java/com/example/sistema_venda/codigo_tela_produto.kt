package com.example.sistema_venda

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class codigo_tela_produto : AppCompatActivity() {
    fun funcao_adicionar(view: View){
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)

        // Fechar com animação
        ObjectAnimator.ofFloat(menuLateral, "translationX", 0f, -menuLateral.width.toFloat()).apply {
            duration = 300
            start()
        }.addListener(onEnd = {
            menuLateral.visibility = View.GONE
        })

        menuAdicionarProdutos.visibility = View.VISIBLE

        // Animação de "fade in" (opcional)
        val fadeIn = ObjectAnimator.ofFloat(menuAdicionarProdutos, "alpha", 0f, 1f).apply {
            duration = 500
        }

        val animatorSet = AnimatorSet()
        animatorSet.play(fadeIn)
        animatorSet.start()
    }

    fun funcao_configuracoes(view: View){
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility != View.VISIBLE) {
            // Abrir com animação
            menuLateral.visibility = View.VISIBLE
            overlay.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(menuLateral, "translationX", -menuLateral.width.toFloat(), 0f).apply {
                duration = 300
                start()
            }
        }
    }

    fun funcao_fechar(view: View){
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility == View.VISIBLE) {
            // Fechar com animação
            ObjectAnimator.ofFloat(menuLateral, "translationX", 0f, -menuLateral.width.toFloat()).apply {
                duration = 300
                start()
            }.addListener(onEnd = {
                menuLateral.visibility = View.GONE
            })
        }

        if (menuAdicionarProdutos.visibility == View.VISIBLE) {
            // Animação de "fade out" (opcional)
            val fadeOut  = ObjectAnimator.ofFloat(menuAdicionarProdutos, "alpha", 1f, 0f).apply {
                duration = 500
            }

            val animatorSet = AnimatorSet()
            animatorSet.play(fadeOut )
            animatorSet.start()
            // Espera acabar a animação para chamar o View.GONE
            fadeOut.addListener(onEnd = {menuAdicionarProdutos.visibility = View.GONE})
        }
        overlay.visibility = View.GONE
    }

    fun funcao_limitar_estoque(view: View){

    }

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