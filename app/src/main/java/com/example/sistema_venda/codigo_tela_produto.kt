package com.example.sistema_venda

import SqlLite.data_base
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class codigo_tela_produto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o link com o arquivo xml da tela de venda
        setContentView(R.layout.tela_produto)
        // Tira a bara no topo do app
        supportActionBar?.hide()
        buscar_produtos()
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

    fun buscar_produtos(){
        val db = data_base(context = this)
        val layoutItensVenda = findViewById<LinearLayout>(R.id.itensVenda)
        val textoSemProduto = findViewById<TextView>(R.id.textoSemProduto)
        val entradaNomeProdutoRemover = findViewById<ListView>(R.id.entradaNomeProdutoRemover)
        val listaProdutos = mutableListOf<String>()
        val listaId = mutableListOf<Int>()

        CoroutineScope(Dispatchers.Main).launch {
            while(true) {
                val produtos = db.consulta_produtos()

                if (produtos.isNotEmpty()) {
                    layoutItensVenda.removeView(textoSemProduto)
                    for (produto in produtos) {
                        val idProduto = produto["nome"] as String
                        val hashId = idProduto.hashCode()

                        if(!listaId.contains(hashId)) {
                            // Lista com os nomes dos produtos disponiveis para serem removidos
                            listaProdutos.add(produto["nome"] as String)
                            val adapter = ArrayAdapter(layoutItensVenda.context, android.R.layout.simple_list_item_activated_1, listaProdutos)
                            entradaNomeProdutoRemover.adapter = adapter
                            entradaNomeProdutoRemover.choiceMode = ListView.CHOICE_MODE_SINGLE

                            val novoItem = LinearLayout(layoutItensVenda.context).apply {
                                background = GradientDrawable().apply {
                                    setColor(Color.LTGRAY) // Cor de fundo
                                    cornerRadius =
                                        10 * resources.displayMetrics.density // Define o radius
                                }
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER_VERTICAL
                                layoutParams = LinearLayout.LayoutParams(
                                    // Medida X
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    // Medida Y
                                    (60 * resources.displayMetrics.density).toInt()
                                ).apply {
                                    setMargins(
                                        /* left = */
                                        (0 * resources.displayMetrics.density).toInt(),
                                        /* top = */
                                        (0 * resources.displayMetrics.density).toInt(),
                                        /* right = */
                                        (0 * resources.displayMetrics.density).toInt(),
                                        /* bottom = */
                                        (5 * resources.displayMetrics.density).toInt()
                                    )
                                    setPadding(
                                        /* left = */
                                        (10 * resources.displayMetrics.density).toInt(),
                                        /* top = */
                                        (0 * resources.displayMetrics.density).toInt(),
                                        /* right = */
                                        (0 * resources.displayMetrics.density).toInt(),
                                        /* bottom = */
                                        (0 * resources.displayMetrics.density).toInt()
                                    )
                                }
                                id = hashId
                            }

                            val nomeProduto = TextView(layoutItensVenda.context).apply {
                                text = produto["nome"] as String
                                textSize = 16f
                                setTextColor(Color.BLACK)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }

                            val precoProduto = TextView(layoutItensVenda.context).apply {
                                text = " R$${String.format("%.2f", produto["preco"] as Double)}"
                                textSize = 16f
                                setTextColor(Color.BLACK)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }

                            val layoutInterno = LinearLayout(layoutItensVenda.context).apply {
                                orientation = LinearLayout.HORIZONTAL
                                layoutParams = LinearLayout.LayoutParams(
                                    // Medida X
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    // Medida Y
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                                gravity = Gravity.CENTER_VERTICAL or Gravity.END
                            }

                            val quantidadeEstoque = TextView(layoutItensVenda.context).apply {
                                text = "0"
                                textSize = 16f
                                setTextColor(Color.BLACK)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                            }

                            val botaoEditar = Button(layoutItensVenda.context).apply {
                                setBackgroundResource(R.drawable.lapis)
                                setTextColor(Color.RED)
                                setOnClickListener{
                                    // falta a função
                                }
                                layoutParams = LinearLayout.LayoutParams(
                                    // Medida X
                                    (40 * resources.displayMetrics.density).toInt(),
                                    // Medida Y
                                    (40 * resources.displayMetrics.density).toInt()
                                ).apply {
                                    setMargins(
                                        /* left = */ (20 * resources.displayMetrics.density).toInt(),
                                        /* top = */ (0 * resources.displayMetrics.density).toInt(),
                                        /* right = */ (10 * resources.displayMetrics.density).toInt(),
                                        /* bottom = */ (0 * resources.displayMetrics.density).toInt()
                                    )
                                }
                            }

                            layoutInterno.addView(quantidadeEstoque)
                            layoutInterno.addView(botaoEditar)

                            novoItem.addView(nomeProduto)
                            novoItem.addView(precoProduto)
                            novoItem.addView(layoutInterno)

                            layoutItensVenda.addView(novoItem)
                        }
                        listaId.add(hashId)
                    }

                }
                delay(1000)
            }
        }
    }

    fun funcao_abrir_menu_adicionar_produto(view: View){
        val menuAdicionarProdutos = findViewById<View>(R.id.menuAdicionarProdutos)
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)

        fechar_lateral(menuLateral)
        fade_in(menuAdicionarProdutos)
    }

    fun funcao_abrir_menu_remover_produto(view: View){
        val menuRemoverProdutos = findViewById<View>(R.id.menuRemoverProdutos)
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)

        fechar_lateral(menuLateral)
        fade_in(menuRemoverProdutos)
    }

    fun funcao_adicionar_produto(view: View){
        val entradaNomeProduto = findViewById<EditText>(R.id.entradaNomeProdutoAdicionar)
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
        val menuRemoverProdutos = findViewById<View>(R.id.menuRemoverProdutos)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility == View.VISIBLE) {
            fechar_lateral(menuLateral)
        }
        if (menuAdicionarProdutos.visibility == View.VISIBLE) {
            fade_out(menuAdicionarProdutos)
        }
        if (menuRemoverProdutos.visibility == View.VISIBLE) {
            fade_out(menuRemoverProdutos)
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

    fun funcao_remover_produto(view: View){
        val entradaNomeProdutoRemover = findViewById<ListView>(R.id.entradaNomeProdutoRemover)

        val db = data_base(view.context)
        val sucesso = db.remover_produto(entradaNomeProdutoRemover.)

        if (sucesso) {
            Toast.makeText(view.context, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(view.context, "Erro ao remover produto!", Toast.LENGTH_SHORT).show()
        }
    }

    fun funcao_voltar(view: View){
        finish()
    }

    fun zerar_menus() {
        val entradaNomeProdutoAdicionar = findViewById<EditText>(R.id.entradaNomeProdutoAdicionar)
        val entradaValorProduto = findViewById<EditText>(R.id.entradaValorProduto)
        val checkboxEstoqueControlado = findViewById<CheckBox>(R.id.checkboxEstoqueControlado)
        val entradaQuantidadeProduto = findViewById<EditText>(R.id.entradaQuantidadeProduto)

        entradaNomeProdutoAdicionar.setText("")
        entradaQuantidadeProduto.setText("")
        entradaValorProduto.setText("")
        checkboxEstoqueControlado.isChecked = false

        val entradaNomeProdutoRemover = findViewById<ListView>(R.id.entradaNomeProdutoRemover)
        entradaNomeProdutoRemover.setSelection(0)
    }
}