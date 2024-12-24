package com.example.sistema_venda

import android.graphics.Color
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.Toast

class `codigo_tela_venda` : AppCompatActivity(){

    fun funcao_sair(view: View) {
        val botao = view as Button
        botao.text = "novo texto"
    }

    fun alterar_valor(layoutIdRecebido: Int, quantidadeRecebida: Int){
        val layoutAtual = findViewById<LinearLayout>(layoutIdRecebido)

        val textoAtual = layoutAtual.getChildAt(3) as TextView
        val novaQuantidade = textoAtual.text.toString().toInt() + quantidadeRecebida
        if(novaQuantidade >= 0) {
            textoAtual.text = novaQuantidade.toString()
        }
    }

    fun criar_itens_venda(){
        // Referencia o layout com o id especificado
        val layout = findViewById<LinearLayout>(R.id.itensVenda)

        for (i in 1..5){
            val novoItem = LinearLayout(this).apply {
                setBackgroundColor(Color.LTGRAY)
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (60 * resources.displayMetrics.density).toInt()
                )
                id = View.generateViewId()
                Margins(0, 10, 0, 0)
            }

            val nomeProduto = TextView(this).apply {
                text = "Teste estatico"
                textSize = 16f
                setTextColor(Color.BLACK)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
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
            }

            novoItem.addView(nomeProduto)
            novoItem.addView(quantidadeEstoque)
            novoItem.addView(botaoMenos)
            novoItem.addView(quantidadeProduto)
            novoItem.addView(botaoMais)

            findViewById<LinearLayout>(R.id.itensVenda).addView(novoItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o link com o arquivo xml da tela de venda
        setContentView(R.layout.tela_venda)
        // Tira a bara no topo do app
        supportActionBar?.hide()
        // Chama a funcao q ira criar a lista de vendas
        criar_itens_venda()
    }
}