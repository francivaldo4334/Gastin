package br.com.fcr.gastin.ui.page.components

import android.graphics.RectF
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.pow

fun distance(offset0: Offset, offset1: Offset): Float {
    return kotlin.math.sqrt((offset0.x - offset0.y).pow(2) + (offset1.x - offset1.y).pow(2))
}
//androidx.compose.ui.graphics
@Composable
fun ColorPicker(
    initialColor: Color,
    width:Int,
    onColorSelected: (Color) -> Unit
) {
    val density = LocalDensity.current.density
    val size = (width/density)-(2*(56))
    var Y1 by remember { mutableStateOf(24f) }
    var Y2 by remember { mutableStateOf(24f) }
    var X3 by remember { mutableStateOf(size*density-24f) }
    var Y3 by remember { mutableStateOf(24f) }
    val pointerSize = 24f*1.2f
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center) {
        Canvas(modifier = Modifier
            .size(size.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newY = Y3 + dragAmount.y
                    val newX = X3 + dragAmount.x
                    val size1 = size * density
                    if (newY > 24 && newY < size1 - 24)
                        Y3 = newY
                    if(newX > 24 && newX < size1 - 24)
                        X3 = newX
                }
            }){
            drawRoundRect(
                color = Color.Red,
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawCircle(
                radius = pointerSize,
                color =  Color.Blue,
                center = Offset(X3,Y3)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Canvas(modifier = Modifier
            .height(size.dp)
            .width(24.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newY = Y1 + dragAmount.y
                    val size1 = size * density
                    if (newY > 24 && newY < size1 - 24)
                        Y1 = newY
                }
            }){
            drawRoundRect(
                color = Color.Red,
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawCircle(
                radius = pointerSize,
                color =  Color.Blue,
                center = Offset(this.size.width/2, Y1)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Canvas(modifier = Modifier
            .height(size.dp)
            .width(24.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val newY = Y2 + dragAmount.y
                    val size1 = size * density
                    if (newY > 24 && newY < size1 - 24)
                        Y2 = newY
                }
            }
        ){
            drawRoundRect(
                color = Color.Red,
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawCircle(
                radius = pointerSize,
                color =  Color.Blue,
                center = Offset(this.size.width/2, Y2)
            )
        }
    }
//    var radius:Float = 0f
//    var bitmap by remember {
//        mutableStateOf(
//            ImageBitmap(10,10,ImageBitmapConfig.Argb8888)
//        )
//    }
//    Canvas(modifier = Modifier
//        .size(width.dp)
//        .pointerInput(Unit){
//            this.detectTapGestures {
//                
//            }
//        }
//        .background(Color.Black)
//    ) {
//        bitmap = ImageBitmap(this.size.width.toInt(),this.size.height.toInt(),ImageBitmapConfig.Argb8888)
//        radius = width.toFloat()/density
//        val canvas = Canvas(bitmap)
//        val paintSweep = Paint()
//        val paintRadial = Paint()
//        paintSweep.shader = SweepGradientShader(
//            center = center,
//            colors = listOf(
//                Color.Blue,
//                Color.Red,
//                Color.Green,
//                Color.Blue
//            )
//        )
//        paintRadial.shader = RadialGradientShader(
//            center = center,
//            colors = listOf(
//                Color.White,
//                Color.Transparent
//            ),
//            radius = radius/1.5f
//        )
//        canvas.drawCircle(center,radius,paintSweep)
//        canvas.drawCircle(center,radius,paintRadial)
//        drawImage(bitmap)
//    }
}