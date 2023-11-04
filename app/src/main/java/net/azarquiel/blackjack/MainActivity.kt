package net.azarquiel.blackjack

import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import net.azarquiel.blackjack.model.Carta
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var random: Random
    private lateinit var llcartas: LinearLayout
    private var titulo = ""
    val mazo = Array(40) {i -> Carta()}
    var imazo = 0
    var palos = arrayOf("clubs", "diamonds", "hearts", "spades")
    var jugador = 0
    var puntos = Array(2){0}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titulo = title.toString()

        random = Random(System.currentTimeMillis())

        llcartas = findViewById<LinearLayout>(R.id.llcartas)
        var ivmazo = findViewById<ImageView>(R.id.ivmazo)
        var btnstop = findViewById<Button>(R.id.btnstop)


        ivmazo.setOnClickListener{ ivmazoOnclick()}
        btnstop.setOnClickListener{ btnstopOnclick()}


        makeMazo()
        newGame()


    }

    private fun makeMazo() {
        var c = 0


        for (p in 0 until 4){
            for(n in 1..10){
                mazo[c] =  Carta(n,p)
                c++
            }
        }

    }

    private fun newGame() {
        imazo = 0
        jugador = 0
        puntos = Array(2){0}

        mazo.shuffle(random)
        llcartas.removeAllViews()
        sacaCarta()
        sacaCarta()



        for (carta in mazo) {
            carta.palo
            Log.d("carmen", "${carta.numero} de ${palos[carta.palo]}")
        }
    }

    private fun btnstopOnclick() {

        if (jugador == 0){
            nextPlayer()


        }else if (jugador == 1){
            gameOver()

        }

    }

    private fun ivmazoOnclick() {
        sacaCarta()

    }

    private fun sacaCarta(){
        val cartaJuego = mazo[imazo]
        val n = if(cartaJuego.numero>7) 10 else cartaJuego.numero
        puntos[jugador]+=n
        imazo++
        val ivCarta = ImageView(this)
        val idimage = resources.getIdentifier("${palos[cartaJuego.palo]}${cartaJuego.numero}", "drawable", packageName)
        ivCarta.setImageResource(idimage)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        ivCarta.layoutParams = lp
        lp.setMargins(8,0,8,0)
        llcartas.addView(ivCarta, 0)
        title = "$titulo - ${puntos[jugador]} puntos"
        //Toast.makeText(this, "${palos[cartaJuego.palo]}${cartaJuego.numero}", Toast.LENGTH_SHORT).show()
        //Log.d("carmen", "${palos[cartaJuego.palo]}${cartaJuego.numero}")
        if (puntos[jugador]>21 && jugador == 0){
            nextPlayer()


        }else if (puntos[jugador]>21 && jugador == 1){
            gameOver()

        }

    }

    private fun gameOver() {
        var msg = ""
        if (puntos[0]>21 && puntos[1]>21){
            msg = "empate"
        }else if(puntos[0]>21){
            msg = "gana jugador 2"
        }else if (puntos[1]>21){
            msg ="gana jugador 1"
        }else if (21-puntos[0]<21-puntos[1]){
            msg="gana jugador 1"
        }else if(21-puntos[0]>21-puntos[1]){
            msg="gana jugador 2"
        }else{
            msg="empate"
        }
        AlertDialog.Builder(this)
            .setTitle("Game Over - $msg")
            .setMessage("Jugador 1: ${puntos[0]} puntos. \n\n Jugador 1: ${puntos[1]} puntos.")
            .setPositiveButton("New Game") { dialog, which ->
                newGame()
        }
        .setNegativeButton("Fin") { dialog, which ->
            finish()
        }
        .show()



    }

    private fun nextPlayer(){
        jugador = 1
        llcartas.removeAllViews()
        sacaCarta()
        sacaCarta()

    }


}