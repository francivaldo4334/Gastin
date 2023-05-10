package br.com.fcr.gastin.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.components.DropDownMoreOptions
import br.com.fcr.gastin.ui.utils.Tetra
import br.com.fcr.gastin.ui.utils.toMonetaryString
private var listIdCheckeds by mutableStateOf(listOf<Int>())
@Composable
fun ListValuesScreenPage(navController: NavController,title:String,listItem:List<Tetra<String,String,Int,Int>>){
    var showAllCheckBox by remember {mutableStateOf(false)}
    var openMoreOptions by remember{ mutableStateOf(false) }
    BackHandler {
        if(showAllCheckBox) {
            showAllCheckBox = false
            listIdCheckeds = emptyList()
        }
        else
            navController.popBackStack()
    }
    Column(Modifier.fillMaxSize()){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, fontSize = 24.sp, modifier = Modifier.padding(start = 16.dp, top = 16.dp))
            Column {
                IconButton(onClick = {openMoreOptions = true}) {
                    Icon(painter = painterResource(id = R.drawable.ic_more_options_chorts), contentDescription = "")
                }
                DropDownMoreOptions(listOptions = listOf(
                    Pair("Selecionar tudo"){
                        showAllCheckBox = true
                        listIdCheckeds = listItem.map { it.tetra }
                    },
                    Pair("-Excluir"){},
                    Pair("+Adicionar"){},
                    Pair("Editar"){},
                ), enable = openMoreOptions) {
                    openMoreOptions = false
                }
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ){
            item { 
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(listItem){(descricao,data,value,id)->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        Log.d("TESTE", "OK")
                                        listIdCheckeds += id
                                        showAllCheckBox = listIdCheckeds.isNotEmpty()
                                    }
                                )
                            }
                            .height(64.dp)
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dolar),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = descricao,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = data,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f),
                                    maxLines = 1
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = value.toMonetaryString(), fontSize = 12.sp)
                            AnimatedVisibility(
                                visible = showAllCheckBox,
                                enter = expandHorizontally(
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                ),
                                exit = shrinkHorizontally(
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                )
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = listIdCheckeds.any { it == id },
                                    onCheckedChange = {
                                        if(it)
                                            listIdCheckeds += id
                                        else
                                            listIdCheckeds -= id
                                        Log.d("CHECK",listIdCheckeds.toString())

                                    }
                                )
                            }
                        }
                    }
                    Divider()
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun ListValuesScreenPagePreview(){
    ListValuesScreenPage(
        navController = rememberNavController(),
        title = "Despesas",
        listItem = listOf(
            Tetra("descricao","01/02/2023",1000,0),
            Tetra("descricao","01/02/2023",1000,1),
            Tetra("descricao","01/02/2023",1000,2),
            Tetra("descricao","01/02/2023",1000,3),
            Tetra("descricao","01/02/2023",1000,4)
        )
    )
}
