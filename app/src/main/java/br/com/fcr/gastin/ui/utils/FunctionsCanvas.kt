package br.com.fcr.gastin.ui.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawPieSlice(
    center:Offset,
    radius:Float,
    startAngle:Float,
    sweepAngle:Float,
    color:Color,
    strokeColor:Color
){
    drawArc(
        color = color,
        startAngle,
        sweepAngle,
        useCenter = true,
        topLeft = Offset(
            x = center.x - radius,
            y = center.y - radius
        )
    )
    drawArc(
        color = strokeColor,
        startAngle,
        sweepAngle,
        useCenter = true,
        topLeft = Offset(
            x = center.x - radius,
            y = center.y - radius
        ),
        style = Stroke(width = 8f)
    )
}