package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.drawPieSlice
import br.com.fcr.gastin.ui.utils.toMonetaryString

@Composable
fun top(onMonthBefore:()->Unit,onMonthNext:()->Unit,onOptions:()->Unit,content:@Composable ()->Unit){
    var textMes by remember{ mutableStateOf("Janeiro") }
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp), contentAlignment = Alignment.CenterEnd){
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMonthBefore) {
                    Icon(painter = painterResource(id = R.drawable.ic_left), contentDescription = "")
                }
                Text(text = textMes, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
                IconButton(onClick = onMonthNext) {
                    Icon(painter = painterResource(id = R.drawable.ic_right), contentDescription = "")
                }
            }
            Column{
                IconButton(onClick = onOptions) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_options_chorts),
                        contentDescription = ""
                    )
                }
                content()
            }
        }
    }
}
@Composable
fun dropDownMoreOptions(customItem:(@Composable ColumnScope.()->Unit)? = null, listOptions:List<Pair<String,()->Unit>>, enable:Boolean, onDismiss:()->Unit){
    DropdownMenu(expanded = enable, onDismissRequest = onDismiss) {
        if(customItem != null)
            customItem()
        listOptions.forEach {
            Box(modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .clickable { it.second;onDismiss() }
                .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart) {
                Text(text = it.first, fontSize = 14.sp)
            }
        }
    }
}
@Composable
fun informes(valorInit:Int,valorSaldo:Int,valorPrevisto:Int){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Recebido", fontSize = 12.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
            Text(text = valorInit.toMonetaryString(), fontSize = 14.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Saldo", fontSize = 14.sp)
            Text(text = valorSaldo.toMonetaryString(), fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Previsto", fontSize = 12.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
            Text(text = valorPrevisto.toMonetaryString(), fontSize = 14.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
        }
    }
}
@Composable
fun visaoGeral(valorReceitas:Int,valorDespesas:Int,onReceitas:()->Unit,onDespesas:()->Unit){
    @Composable
    fun item(isDespesas:Boolean,value:Int,onClick:()->Unit){
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDespesas)
                            Color.Red.copy(0.5f)
                        else
                            Color.Green.copy(0.5f)
                    ))
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text =
                    if(isDespesas)
                        "Despesas"
                    else
                        "Receitas",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
            Text(
                text = value.toMonetaryString(),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp)
        }
    }
    BoxContent(
        enablePadding = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Visao geral", fontSize = 14.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp))
            Spacer(modifier = Modifier.size(16.dp))
            item(isDespesas = true, value = valorDespesas,onDespesas)
            item(isDespesas = false, value = valorReceitas,onReceitas)
        }
    }
}
@Composable
fun dashboard(categorias:List<Triple<String,Int, Color>>, onClickMore:()->Unit, content:@Composable ()->Unit){
    var size = LocalConfiguration.current.screenWidthDp/2
    size = size - (3*16)
    val totalPizzas = categorias.sumOf { it.second }
    BoxContent(
        enablePadding = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Row (
                Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = "Despesas", fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp, start = 16.dp))
                Column(){
                    IconButton(
                        onClick = onClickMore
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_more_options_chorts),
                            contentDescription = ""
                        )
                    }
                    content()
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                pizzaChart(
                    categorias,
                    modifier = Modifier.size(size.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    categorias.forEach {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(it.third)
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(
                                    text = it.first,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 14.sp
                                )
                            }
                            var valorPorcentagen = (it.second.toFloat()/totalPizzas.toFloat() * 100).toInt()
                            Text(
                                text = "% ${valorPorcentagen.toString().padStart(3,' ')}",
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}
@Composable
fun pizzaChart(pizzalist:List<Triple<String,Int, Color>>, modifier: Modifier = Modifier.fillMaxSize()){
    val totalPizzas = pizzalist.sumOf { it.second }
    var currentAngle = 0f
    val strokeColor = MaterialTheme.colors.background
    Canvas(modifier = modifier){
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
@Composable
fun evolucaoDespesas(listValues:List<Int>, listDays:List<Pair<Int,String>>, onClick:()->Unit, onBefore:()->Unit, onNext:()->Unit, content:@Composable ()->Unit){
    val valueMax = listValues.sortedBy { it }.last()
    BoxContent(
        enablePadding = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Evolucao das despesas", fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp, start = 16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBefore) {
                        Icon(painter = painterResource(id = R.drawable.ic_left), contentDescription = "")
                    }
                    IconButton(onClick = onNext) {
                        Icon(painter = painterResource(id = R.drawable.ic_right), contentDescription = "")
                    }
                    Column{
                        IconButton(
                            onClick = onClick,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_more_options_chorts),
                                contentDescription = ""
                            )
                        }
                        content()
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    listValues.forEach {
                        Text(text = it.toMonetaryString(), fontSize = 10.sp)
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
                listDays.forEach{
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .width(16.dp)
                            .height(((it.first.toFloat() / valueMax.toFloat()) * 180).dp)
                            .background(color = Color(0xFF269FB9)))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = it.second, fontSize = 14.sp)
                    }
                }
            }

        }
    }
}
