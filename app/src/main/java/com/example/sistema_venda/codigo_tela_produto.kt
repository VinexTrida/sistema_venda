package com.example.sistema_venda

import SqlLite.data_base
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
import android.widget.Toast
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

//-------------------------------------------------------------------------------------------------------------------------------

    fun funcao_abrir_menu_adicionar_produto(view: View){
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)

        fechar_lateral(menuLateral)
        fade_in(menuAdicionarProdutos)
    }

    fun funcao_adicionar_produto(view: View){
        val entradaNomeProduto = findViewById<EditText>(R.id.entradaNomeProduto)
        val entradaValorProduto = findViewById<EditText>(R.id.entradaValorProduto)
        val checkboxEstoqueControlado = findViewById<CheckBox>(R.id.checkboxEstoqueControlado)
        val entradaQuantidadeProduto = findViewById<EditText>(R.id.entradaQuantidadeProduto)

        val nome = entradaNomeProduto.text.toString()
        val valor = entradaValorProduto.text.toString().toDoubleOrNull() ?: 0.0
        var quantidade = 0
        var inerente = 1
        val emUso = 0
        val posicao = 0
        val combo = 0
        val caixas = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,"

        if(checkboxEstoqueControlado.isChecked){
            quantidade = entradaQuantidadeProduto.toString().toInt()
            inerente = 0
        }

        val db = data_base(view.context)
        val sucesso = db.inserir_produto(nome, valor, quantidade, inerente, emUso, posicao, combo, caixas)

        if (sucesso) {
            Toast.makeText(view.context, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(view.context, "Erro ao inserir produto!", Toast.LENGTH_SHORT).show()
        }
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