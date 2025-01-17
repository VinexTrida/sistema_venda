package com.example.sistema_venda

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay

class codigo_tela_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o link com o arquivo xml da tela de venda
        setContentView(R.layout.tela_produto)
        // Tira a bara no topo do app
        supportActionBar?.hide()
    }

    fun fade_in(view: View){
        view.visibility=View.VISIBLE
        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 500
        }

        val animatorSet = AnimatorSet()
        animatorSet.play(fadeIn)
        animatorSet.start()
    }

    fun fade_out(view: View){
        val fadeOut  = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
            duration = 500
        }

        val animatorSet = AnimatorSet()
        animatorSet.play(fadeOut)
        animatorSet.start()
        // Espera acabar a animação para chamar o View.GONE
        fadeOut.addListener(onEnd = {view.visibility = View.GONE})
    }

    fun abrir_lateral(view: View, widthRecebido: Float){
        ObjectAnimator.ofFloat(view, "translationX", -widthRecebido, 0f).apply {
            duration = 300
            start()
        }
        view.visibility = View.VISIBLE
    }

    fun fechar_lateral(view: View){
        ObjectAnimator.ofFloat(view, "translationX", 0f, -view.width.toFloat()).apply {
            duration = 300
            start()
        }.addListener(onEnd = {
            view.visibility = View.GONE
        })
    }

    fun funcao_abrir_menu_adicionar_produto(view: View){
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)

        fechar_lateral(menuLateral)
        fade_in(menuAdicionarProdutos)
    }

    fun funcao_configuracoes(view: View){
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility != View.VISIBLE) {
            fade_in(overlay)

            // Precisa por o tamanho manual, se não, não faz a animação na primeira vez que é executado
            abrir_lateral(menuLateral, 280f)
        }
    }

    fun funcao_fechar(view: View){
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility == View.VISIBLE) {
            // Fechar com animação
            fechar_lateral(menuLateral)
        }

        if (menuAdicionarProdutos.visibility == View.VISIBLE) {
            fade_out(menuAdicionarProdutos)
        }
        fade_out(overlay)

        zerar_menus()
    }

    fun funcao_limitar_estoque(view: View){
        val checkBox = view as CheckBox
        val textoQuantidadeProduto = findViewById<TextView>(R.id.textoQuantidadeProduto)
        val inputQuantidadeProduto = findViewById<EditText>(R.id.entradaQuantidadeProduto)

        if(checkBox.isChecked == true){
            textoQuantidadeProduto.alpha = 1f
            inputQuantidadeProduto.alpha = 1f
            inputQuantidadeProduto.isEnabled = true
        }else{
            inputQuantidadeProduto.setText("")
            textoQuantidadeProduto.alpha = 0.5f
            inputQuantidadeProduto.alpha = 0.5f
            inputQuantidadeProduto.isEnabled = false
        }
    }

    fun funcao_voltar(view: View){
        finish()
    }

    fun zerar_menus() {

        val entradaNomeProduto = findViewById<EditText>(R.id.entradaNomeProduto)
        val entradaValorProduto = findViewById<EditText>(R.id.entradaValorProduto)
        val checkboxEstoqueControlado = findViewById<CheckBox>(R.id.checkboxEstoqueControlado)
        val entradaQuantidadeProduto = findViewById<EditText>(R.id.entradaQuantidadeProduto)

        entradaNomeProduto.setText("")
        entradaQuantidadeProduto.setText("")
        entradaValorProduto.setText("")
        checkboxEstoqueControlado.isChecked = false
    }
}