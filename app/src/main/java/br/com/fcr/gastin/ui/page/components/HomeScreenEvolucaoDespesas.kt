package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.toMonetaryString

@Composable
fun HomeScreenEvolucaoDespesas(listValues:List<Int>, listDays:List<Pair<Int,String>>, onClick:()->Unit, onBefore:()->Unit, onNext:()->Unit, content:@Composable ()->Unit){
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
                Text(text = stringResource(R.string.txt_evolucao_das_despesas), fontSize = 14.sp, modifier = Modifier.padding(top = 16.dp, start = 16.dp))
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
