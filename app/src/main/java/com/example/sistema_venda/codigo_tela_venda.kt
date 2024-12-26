package com.example.sistema_venda

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.print.PrintAttributes.Margins
import android.util.Log
import android.view.Gravity
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

        val layoutInterno = layoutAtual.getChildAt(1) as LinearLayout
        val textoAtual = layoutInterno.getChildAt(2) as TextView
        val novaQuantidade = textoAtual.text.toString().toInt() + quantidadeRecebida
        if(novaQuantidade >= 0) {
            textoAtual.text = novaQuantidade.toString()
        }
    }

    fun criar_itens_venda(){
        // Referencia o layout com o id especificado
        val layout = findViewById<LinearLayout>(R.id.itensVenda)

        for (i in 1..25){
            val novoItem = LinearLayout(this).apply {
                background = GradientDrawable().apply {
                    setColor(Color.LTGRAY) // Cor de fundo
                    cornerRadius = 10 * resources.displayMetrics.density // Define o radius (16dp)
                }
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (60 * resources.displayMetrics.density).toInt()
                ).apply {
                    setMargins(
                        (0 * resources.displayMetrics.density).toInt(),
                        (0 * resources.displayMetrics.density).toInt(),
                        (0 * resources.displayMetrics.density).toInt(),
                        (5 * resources.displayMetrics.density).toInt()
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

            layoutInterno.addView(quantidadeEstoque)
            layoutInterno.addView(botaoMenos)
            layoutInterno.addView(quantidadeProduto)
            layoutInterno.addView(botaoMais)

            novoItem.addView(nomeProduto)
            novoItem.addView(layoutInterno)

            layout.addView(novoItem)
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