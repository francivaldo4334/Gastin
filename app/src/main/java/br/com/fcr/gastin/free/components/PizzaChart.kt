package br.com.fcr.gastin.free.ui.page.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.free.ui.utils.drawPieSlice
@Composable
fun PizzaChart(pizzalist:List<Triple<String,Int, Color>>, modifier: Modifier = Modifier.fillMaxSize()){
    val totalPizzas = pizzalist.sumOf { it.second }
    var currentAngle = 0f
    val strokeColor = MaterialTheme.colors.background
    Canvas(modifier = modifier){
        if(pizzalist.isEmpty()){
            drawPieSlice(
                center = this.center,
                radius = this.size.width/2,
                startAngle = currentAngle,
                sweepAngle = 360f,
                color = Color(0xFFD4D4D4),
                strokeColor = strokeColor
            )
        }
        else
        pizzalist.forEach {(flavor,quantity,color)->
            val sweepAngle = quantity.toFloat()/totalPizzas.toFloat() * 360f
            drawPieSlice(
                center = this.center,
                radius = this.size.width/2,
                startAngle = currentAngle,
                sweepAngle = sweepAngle,
                color = color,
                strokeColor = strokeColor
            )
            currentAngle += sweepAngle
        }
    }
}