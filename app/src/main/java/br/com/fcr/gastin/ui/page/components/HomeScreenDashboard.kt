package br.com.fcr.gastin.ui.page.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.common.Constants

@Composable
fun HomeScreenDashboard(categorias:List<Triple<String,Int, Color>>, onClickMore:()->Unit, content:@Composable ()->Unit){
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
                Text(
                    text =
                        if(Constants.IsTotalPeriod)stringResource(id = R.string.txt_despesas_totais)
                        else stringResource(id = R.string.txt_despesas),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
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
            if(categorias.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.txt_sem_registros))
                }
            }
            else {
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    PizzaChart(
                        categorias,
                        modifier = Modifier.size(size.dp)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Column {
                        categorias.forEach {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
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
                                        fontSize = 14.sp,
                                        maxLines = 1
                                    )
                                }
                                var valorPorcentagen =
                                    (it.second.toFloat() / totalPizzas.toFloat() * 100).toInt()
                                Text(
                                    text = "% ${valorPorcentagen.toString().padStart(3, ' ')}",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 14.sp,
                                    maxLines = 1
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
}