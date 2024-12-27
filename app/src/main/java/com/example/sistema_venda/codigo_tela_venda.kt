package com.example.sistema_venda

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.core.animation.addListener
import com.example.sistema_venda.SqlLite.data_base

class `codigo_tela_venda` : AppCompatActivity(){

    fun alterar_valor(layoutIdRecebido: Int, quantidadeRecebida: Int){
        val layoutAtual = findViewById<LinearLayout>(layoutIdRecebido) ?: return

        val layoutInterno = layoutAtual.getChildAt(1) as? LinearLayout ?: return
        val textoAtual = layoutInterno.getChildAt(2) as? TextView ?: return
        val novaQuantidade = textoAtual.text.toString().toInt() + quantidadeRecebida
        if(novaQuantidade >= 0) {
            textoAtual.text = novaQuantidade.toString()
        }
    }

    fun busca_BD(caixa: Int){
        val DBHelper = data_base(this)
        val db = DBHelper.writableDatabase

        var nome = ""
        var preco = 0.00
        var quantidade = 0
        var combo = 0
        try {
            // Comando que gera retorno
            val retornoBD = db.rawQuery("SELECT (nome, preco, quantidade, combo) " +
                    "FROM produtos " +
                    "WHERE emUso = 1 and (quantidade > 0 or inerente = 1) " +
                    "and (caixa LIKE '${caixa},%' or caixa LIKE '%,${caixa},%') " +
                    "ORDER BY posicao", null)

            if(retornoBD.moveToFirst()){
                do{
                    nome = retornoBD.getString(retornoBD.getColumnIndexOrThrow("nome"))
                    preco = retornoBD.getDouble(retornoBD.getColumnIndexOrThrow("preco"))
                    quantidade = retornoBD.getInt(retornoBD.getColumnIndexOrThrow("quantidade"))
                    combo = retornoBD.getInt(retornoBD.getColumnIndexOrThrow("combo"))
                } while (retornoBD.moveToNext())
            }
        } catch (e: Exception){
            println("O erro foi ${e.message}")
            Toast.makeText(this, "Ocorreu um erro, tente novamente!", Toast.LENGTH_LONG).show()
        }

        // Chama a funcao q ira criar a lista de vendas
        criar_itens_venda(nome, preco, quantidade)
    }

    fun criar_itens_venda(nomeRecebido: String, precoRecebido: Double, quantidadeRecebida: Int){
        // Referencia o layout com o id especificado
        val layout = findViewById<LinearLayout>(R.id.itensVenda)

        for (i in 1..25){
            val novoItem = LinearLayout(this).apply {
                background = GradientDrawable().apply {
                    setColor(Color.LTGRAY) // Cor de fundo
                    cornerRadius = 10 * resources.displayMetrics.density // Define o radius
                }
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (60 * resources.displayMetrics.density).toInt()
                ).apply {
                    setMargins(
                        /* left = */ (0 * resources.displayMetrics.density).toInt(),
                        /* top = */ (0 * resources.displayMetrics.density).toInt(),
                        /* right = */ (0 * resources.displayMetrics.density).toInt(),
                        /* bottom = */ (5 * resources.displayMetrics.density).toInt()
                    )
                    setPadding(
                        /* left = */ (10 * resources.displayMetrics.density).toInt(),
                        /* top = */ (0 * resources.displayMetrics.density).toInt(),
                        /* right = */ (0 * resources.displayMetrics.density).toInt(),
                        /* bottom = */ (0 * resources.displayMetrics.density).toInt()
                    )
                }
                id = View.generateViewId()
            }

            val nomeProduto = TextView(this).apply {
                text = "Teste estatico"
                textSize = 16f
                setTextColor(Color.BLACK)
                textAlignment = View.TEXT_ALIGNMENT_CENTER

            }

            val layoutInterno = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                gravity = Gravity.END
            }

            val quantidadeEstoque = TextView(this).apply {
                text = "0"
                textSize = 16f
                setTextColor(Color.BLACK)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

            val botaoMenos = Button(this).apply {
                text = "-"
                textSize = 26f
                setTextColor(Color.RED)
                setOnClickListener{
                    alterar_valor(novoItem.id, -1)
                }
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = LinearLayout.LayoutParams(
                    (50 * resources.displayMetrics.density).toInt(),
                    (50 * resources.displayMetrics.density).toInt()
                ).apply {
                    setMargins(
                        /* left = */ (20 * resources.displayMetrics.density).toInt(),
                        /* top = */ (0 * resources.displayMetrics.density).toInt(),
                        /* right = */ (10 * resources.displayMetrics.density).toInt(),
                        /* bottom = */ (0 * resources.displayMetrics.density).toInt()
                    )
                }
            }

            val quantidadeProduto = TextView(this).apply {
                text = "0"
                textSize = 16f
                setTextColor(Color.BLACK)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

            val botaoMais = Button(this).apply {
                text = "+"
                textSize = 26f
                setTextColor(Color.GREEN)
                setOnClickListener{
                    alterar_valor(novoItem.id, 1)
                }
                setBackgroundColor(Color.TRANSPARENT)
                layoutParams = LinearLayout.LayoutParams(
                    (50 * resources.displayMetrics.density).toInt(),
                    (50 * resources.displayMetrics.density).toInt()
                ).apply {
                    setMargins(
                        /* left = */ (10 * resources.displayMetrics.density).toInt(),
                        /* top = */ (0 * resources.displayMetrics.density).toInt(),
                        /* right = */ (10 * resources.displayMetrics.density).toInt(),
                        /* bottom = */ (0 * resources.displayMetrics.density).toInt()
                    )
                }

            }

            layoutInterno.addView(quantidadeEstoque)
            layoutInterno.addView(botaoMenos)
            layoutInterno.addView(quantidadeProduto)
            layoutInterno.addView(botaoMais)

            novoItem.addView(nomeProduto)
            novoItem.addView(layoutInterno)

            layout.addView(novoItem)
        }
    }

    fun funcao_configuracoes(view: View){
        val menuLateral = findViewById<LinearLayout>(R.id.menuLateral)
        val overlay = findViewById<View>(R.id.overlay)

        if (menuLateral.visibility == View.VISIBLE) {
            // Fechar com animação
            ObjectAnimator.ofFloat(menuLateral, "translationX", 0f, -menuLateral.width.toFloat()).apply {
                duration = 300
                start()
            }.addListener(onEnd = {
                menuLateral.visibility = View.GONE
                overlay.visibility = View.GONE
            })
        } else {
            // Abrir com animação
            menuLateral.visibility = View.VISIBLE
            overlay.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(menuLateral, "translationX", -menuLateral.width.toFloat(), 0f).apply {
                duration = 300
                start()
            }
        }
    }

    fun funcao_sair(view: View) {
        val botao = view as Button
        botao.text = "novo texto"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o link com o arquivo xml da tela de venda
        setContentView(R.layout.tela_venda)
        // Tira a bara no topo do app
        supportActionBar?.hide()

        busca_BD(1)
    }
}