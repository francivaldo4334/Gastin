package br.com.fcr.gastin.ui.page.components

import android.graphics.Color.HSVToColor
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
fun rgbToHex(R: Int, G: Int, B: Int): Color {
    return Color(R,G,B)
}
fun mergeHexColors(color1: Long, color2: Long): Long {
    val mergedColor = (color1 and 0x00FFFFFF) or (color2 and 0xFF000000)
    return mergedColor
}
@Composable
fun ColorPicker(
    width:Int,
    onColorSelected: (Color) -> Unit
) {
    val density = LocalDensity.current.density
    val size = (width/density)-72
    var colorMapOffset by rememberSaveable { mutableStateOf(0f) }
    var colorMapHeight by rememberSaveable { mutableStateOf(size * density) }
    var saturation by rememberSaveable { mutableStateOf(1f) }
    var lightness by rememberSaveable { mutableStateOf(1f) }
    var color by remember{ mutableStateOf(
        Color.Red
    ) }
    color = getSelectedColor(
        colorMapOffset = colorMapOffset,
        colorMapHeight = colorMapHeight,
        saturation = saturation,
        lightness = lightness
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center) {
        ScrollRect(
            size = size,
            colorBackground = getSelectedColor(
                colorMapOffset = colorMapOffset,
                colorMapHeight = colorMapHeight,
                saturation = 1f,
                lightness = 1f
            ),
            colorSelected = color,
            onValueX = {
                saturation = it
                onColorSelected(color)
            },
            onValueY = {
                lightness = it
                onColorSelected(color)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        ScrollBar(
            size = size,
            onValue = { _colorMapOffset,_colorMapHeight->
                colorMapOffset = _colorMapOffset
                colorMapHeight = _colorMapHeight
                onColorSelected(color)
            }
        )
    }
}
private fun calculateCorrectOffset(selectorOffset:Float,selectorRadius:Float):Float{
    return selectorOffset + selectorRadius
}
private fun getSelectedColor(
    colorMapOffset:Float, colorMapHeight:Float, saturation:Float, lightness:Float
):Color{
    val hue = (colorMapOffset/colorMapHeight)*360
    return Color(
        HSVToColor(
            floatArrayOf(
                hue,
                saturation,
                lightness
            )
        )
    )
}
private fun createColorMap(colorMapHeight:Float):Brush{
    val colors = mutableListOf<Color>()
    for (   i in 0..360){
        val saturation = 1f
        val lightness = 1f
        val hsv = HSVToColor(
            floatArrayOf(
                i.toFloat(),
                saturation,
                lightness
            )
        )
        colors.add(Color(hsv))
    }
    return Brush.verticalGradient(
        colors = colors,
        startY = 0f,
        endY = colorMapHeight
    )
}
@Composable
fun ScrollRect(size:Float,colorBackground:Color, colorSelected:Color, onValueX: (Float) -> Unit, onValueY: (Float) -> Unit){
    val density = LocalDensity.current.density
    var positionXLightBlack by remember { mutableStateOf(size*density-24f) }
    var positionYLightBlack by remember { mutableStateOf(24f) }
    val pointerSize = 24f*1.2f
    Canvas(modifier = Modifier
        .size(size.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val newY = positionYLightBlack + dragAmount.y
                val newX = positionXLightBlack + dragAmount.x
                val size1 = size * density
                if (newY > 24 && newY < size1 - 24)
                    positionYLightBlack = newY
                if (newX > 24 && newX < size1 - 24)
                    positionXLightBlack = newX
                val percentualX = positionXLightBlack / size1
                onValueX(percentualX)
                val percentualY = positionYLightBlack / size1
                onValueY( 1f - percentualY)
            }
        }){
        drawRoundRect(
            color = colorBackground,
            cornerRadius = CornerRadius(20f)
        )
        drawRoundRect(
            brush = Brush.horizontalGradient(listOf(
                Color(0xFFFFFFFF),
                        Color(0)
            )),
            cornerRadius = CornerRadius(20f)
        )
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(
                Color(0),
                Color(0xFF000000),
            )),
            cornerRadius = CornerRadius(20f)
        )
        drawCircle(
            radius = pointerSize + 4f,
            color =  Color.White,
            center = Offset(positionXLightBlack,positionYLightBlack),
        )
        drawCircle(
            radius = pointerSize,
            color =  colorSelected,
            center = Offset(positionXLightBlack,positionYLightBlack),
        )
    }
}
@Composable
fun ScrollBar(size:Float,onValue:(Float,Float)->Unit){
    val pointerWidth = 64f
    val pointHeight = 40f
    val density = LocalDensity.current.density
    var positionBar by remember { mutableStateOf(0f) }

    Canvas(modifier = Modifier
        .height(size.dp)
        .width(24.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val height = with(density) {
                    size.dp.toPx()
                }
                val newY = positionBar + dragAmount.y
                if (newY > 0 && newY < height - pointHeight) {
                    positionBar = newY
                }

                onValue(
                    calculateCorrectOffset(
                        selectorOffset = newY - (height / 2),
                        selectorRadius = height / 2
                    ),
                    height,
                )
            }
        }){
        drawRoundRect(
            brush = createColorMap(this.size.height),
            cornerRadius = CornerRadius(20f, 20f)
        )
        drawRoundRect(
            cornerRadius = CornerRadius(8f),
            size = Size(pointerWidth,pointHeight),
            color =  Color(0xFFB4B4B4),
            topLeft = Offset(((24f*density) - pointerWidth)/2, positionBar),
        )
    }
}