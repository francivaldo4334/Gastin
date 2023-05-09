package br.com.fcr.gastin.ui.page

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.utils.Route
import br.com.fcr.gastin.ui.page.components.*

@Composable
fun HomeScreenPage(navController: NavController,onMonthBefore:()->Unit,onMonthNext:()->Unit,onSwitch:(Boolean)->Unit) {
    var openDropDownTop by remember {mutableStateOf(false)}
    var openDropDownDashboard by remember {mutableStateOf(false)}
    var openDropDownEvolucao by remember {mutableStateOf(false)}
    val categorias = listOf(
        Triple("tstee",1,Color(0xFF269FB9)),
        Triple("tstee",2,Color(0xFFA6ED0E)),
        Triple("tstee",3,Color(0xFF0000Ff))
    )
    val values = listOf<Int>(
        1900,
        190,
        100,
        100,
        100,
        100,
        100,
        19,
        10,
    )
    val days = listOf(
        Pair(1900,"seg"),
        Pair(1000,"ter"),
        Pair(1800,"qua"),
        Pair(800,"qui"),
        Pair(400,"sex"),
        Pair(800,"sab"),
        Pair(400,"dom"),
    )//.sortedBy { it.first }.reversed().partitionList()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        top(onMonthBefore,onMonthNext, onOptions = {openDropDownTop = ! openDropDownTop}){
            dropDownMoreOptions(
                customItem = {
                    Row(modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Modo escuro", fontSize = 14.sp)
                        Switch(checked = Constants.IsDarkTheme, onCheckedChange = {
                            onSwitch(it)
                        })
                    }
                },
                listOptions = listOf(
                    Pair("+Adicionar categoria",{}),
                    Pair("+Adicionar despesa",{}),
                    Pair("+Adicionar receita",{}),
                    Pair("-Remover categoria",{}),
                    Pair("-Remover despesa",{}),
                    Pair("-Remover receita",{}),
                    Pair("Como funciona?",{}),
                ),
                enable = openDropDownTop,
                onDismiss = {
                    openDropDownTop = false
                })
        }
        LazyColumn{
            //Informacoes de topo
            item{
                Spacer(modifier = Modifier.size(16.dp))
                informes(100,50,50)
                Spacer(modifier = Modifier.size(32.dp))
            }
            //Visao geral de informacoes
            item {
                visaoGeral(
                    valorReceitas = 100,
                    valorDespesas = 100,
                    onReceitas = {
                        navController.navigate(Route.LISTA_RESEITAS)
                    },
                    onDespesas = {
                        navController.navigate(Route.LISTA_DESPESAS)
                    }
                )
            }
            //Dashboard
            item{
                dashboard(categorias,{
                    openDropDownDashboard = true
                }){
                    dropDownMoreOptions(
                        listOptions = listOf(
                            Pair("Ver categorias",{}),
                            Pair("+Adicionar categoria",{}),
                            Pair("-Remover categoria",{})
                        ),
                        enable = openDropDownDashboard,
                        onDismiss = {
                            openDropDownDashboard = false
                        })
                }
            }
            //Evolucao de despesas
            item {
                evolucaoDespesas(values,days,
                    onClick = {
                        openDropDownEvolucao = true
                    },
                    onBefore = {

                    },
                    onNext = {

                    }){
                    dropDownMoreOptions(
                        listOptions = listOf(
                            Pair("Visualiar por mes",{}),
                            Pair("Visualizar por quinzena",{}),
                            Pair("Visualizar por semana",{})
                        ),
                        enable = openDropDownEvolucao,
                        onDismiss = {
                            openDropDownEvolucao = false
                        })
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
private fun HomeScreenPagePreview(){
    HomeScreenPage(rememberNavController(),{},{}){}
}