package br.com.fcr.gastin.ui.page.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.ui.utils.drawPieSlice

@Composable
fun PizzaChart(
    fatias: List<Triple<String, Int, Color>>,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val totalValor = fatias.sumOf { it.second }
    val fundoCor = MaterialTheme.colorScheme.background
    Canvas(modifier = modifier) {
        val raio = size.width / 2
        var anguloAtual = 0f

        if (fatias.isEmpty()) {
            drawPieSlice(
                center = center,
                radius = raio,
                startAngle = 0f,
                sweepAngle = 360f,
                color = Color(0xFFD4D4D4),
                strokeColor = fundoCor
            )
            return@Canvas
        }

        fatias.forEach { (_, quantidade, cor) ->
            Log.d("ITEM_PIZZA", "anguloAtual: ${quantidade.toFloat() / totalValor}:  totalValor: ${totalValor}" )
            val angulo = if (totalValor > 0) {
                (quantidade.toFloat() / totalValor) * 360f
            } else {
                0f
            }
            drawPieSlice(
                center = center,
                radius = raio,
                startAngle = anguloAtual,
                sweepAngle = angulo,
                color = cor,
                strokeColor = fundoCor
            )

            anguloAtual += angulo
        }
    }
}
