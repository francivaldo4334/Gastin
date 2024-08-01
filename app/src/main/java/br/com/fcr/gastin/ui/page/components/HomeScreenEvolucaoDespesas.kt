package br.com.fcr.gastin.ui.page.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.toMonetaryString

private fun createList(max:Int,min:Int):List<Int>{
    if (min >= max)
        return emptyList()
    val increment = (max - min)/6
    val listResponse = mutableListOf<Int>()
    for (it in min..max step increment){
        listResponse.add(it)
    }
    return listResponse
}
@Composable
fun HomeScreenEvolucaoDespesas(
    listValues: List<Int>,
    listDays: List<Triple<Int, String,String>>,
    onBefore: () -> Unit,
    onNext: () -> Unit
){
    val valueMax = if(listValues.isNotEmpty()) listValues.sortedBy { it }.last() else 0
    val valueMin = if(listValues.isNotEmpty()) listValues.sortedBy { it }.first() else 0
    val simpleList = createList(valueMax,valueMin).distinct().sortedBy { it }.reversed()
    val Density = LocalDensity.current
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
                }
            }
            AnimatedVisibility(visible = simpleList.isEmpty()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp), contentAlignment = Alignment.Center){
                    Text(text = stringResource(id = R.string.txt_sem_registros))
                }
            }
            AnimatedVisibility(visible = simpleList.isNotEmpty()) {
                Column(Modifier.fillMaxWidth()) {
                    val dates = listDays
                    Text(
                        text = "${dates.first().third} - ${dates.last().third}",
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        var height by remember { mutableStateOf(0) }
                        Column(
                            modifier = Modifier.onGloballyPositioned {
                                height = it.size.height
                            }
                        ) {
                            simpleList.forEach {
                                Text(
                                    modifier = Modifier.width(40.dp),
                                    text = it.toMonetaryString(),
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        with(Density) {
                                            height.toDp()
                                        }
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                listDays.forEach {
                                    Box(modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .width(16.dp)
                                        .height(
                                            with(Density) {
                                                ((it.first.toFloat() / valueMax.toFloat()) * height).toDp()
                                            }
                                        )
                                        .background(color = Color(0xFF269FB9))
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listDays.forEach {
                                    Text(
                                        modifier = Modifier.width(16.dp),
                                        text = it.second,
                                        fontSize = 8.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }   
                }
            }

        }
    }
}
