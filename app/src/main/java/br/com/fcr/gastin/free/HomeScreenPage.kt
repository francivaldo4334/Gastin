package br.com.fcr.gastin.free

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fcr.gastin.free.data.database.model.Categoria
import br.com.fcr.gastin.free.data.database.viewmodel.DashboardWeek
import br.com.fcr.gastin.free.ui.page.components.DropDownMoreOptions
import br.com.fcr.gastin.free.ui.page.components.DropUpNewCategory
import br.com.fcr.gastin.free.ui.page.components.HomeScreenInformes
import br.com.fcr.gastin.free.ui.common.Constants
import br.com.fcr.gastin.free.ui.page.components.HomeScreenDashboard
import br.com.fcr.gastin.free.ui.page.components.HomeScreenDropUpNewRegister
import br.com.fcr.gastin.free.ui.page.components.HomeScreenEvolucaoDespesas
import br.com.fcr.gastin.free.ui.page.components.HomeScreenVisaoGeral
import br.com.fcr.gastin.free.ui.page.components.HomeScreentop
import br.com.fcr.gastin.free.ui.utils.Route
import br.com.fcr.gastin.free.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.free.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.free.ui.page.viewmodels.toModel
import br.com.fcr.gastin.free.ui.utils.toStringDate
import br.com.fcr.gastin.free.ui.utils.toWeekString
import java.lang.Exception
import java.util.Calendar

@Composable
fun HomeScreenPage(
    navController: NavController,
    textMes:String,
    stringYear:String,
    onMonthBefore:()->Unit,
    onMonthNext:()->Unit,
    onWeekBefore:()->Unit,
    onWeekNext:()->Unit,
    onSwitchTheme:(Boolean)->Unit,
    onNewRegister:(Boolean, RegistroViewModel)->Unit,
    onNewCategoria:(Categoria)->Unit,
    categoriasInforms:List<Triple<String,Int,Color>>,
    valorDespesas:Int,
    valorReceitas:Int,
    valorDespesasBusca:Int,
    valorReceitasBusca:Int,
    Categorias:List<CategoriaViewModel>,
    CategoriaDefault: CategoriaViewModel,
    onInformsTotal:(Boolean)->Unit,
    graphicInforms:List<DashboardWeek>,
    onDateUpdate:()->Unit,
    openNewRegister:Boolean?
) {
    val context = LocalContext.current as Activity
    val calendar = Calendar.getInstance()
    var IsDespesa:Boolean? by remember {mutableStateOf(null)}
    var openDropUpNewCategory by remember{ mutableStateOf(false) }
    var openDropDownTop by remember {mutableStateOf(false)}
    var openDropDownDashboard by remember {mutableStateOf(false)}
    var openDropUpNewRegister by remember { mutableStateOf(openNewRegister?:false)}
    val values = graphicInforms.map { it.valor }
    val days = graphicInforms.map {
        calendar.time = it.date
        val week = calendar.get(Calendar.DAY_OF_WEEK)
        Triple(it.valor,week.toWeekString(context),it.date.toStringDate())
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeScreentop(textMes,stringYear,onMonthBefore,onMonthNext, onOptions = {openDropDownTop = ! openDropDownTop}){
            DropDownMoreOptions(
                customItem = {
                    Row(modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.txt_modo_escuro), fontSize = 14.sp)
                        Switch(
                            checked = Constants.IsDarkTheme,
                            onCheckedChange = {
                                onSwitchTheme(it)
                            },
                            colors = SwitchDefaults.colors(
                                uncheckedThumbColor = MaterialTheme.colors.onBackground
                            )
                        )
                    }
                },
                listOptions = listOf(
                    Pair(stringResource(R.string.txt_adicionar_categoria)) {
                        openDropUpNewCategory = true
                    },
                    Pair(stringResource(R.string.txt_adicionar_receita)) {
                        IsDespesa = false;openDropUpNewRegister = true
                    },
                    Pair(stringResource(R.string.txt_adicionar_despesa)) {
                        IsDespesa = true;openDropUpNewRegister = true
                    },
                    Pair(stringResource(R.string.txt_como_funciona)) {
                        navController.navigate(Route.HELP_SCREEN)
                    },
                    Pair(stringResource(R.string.remover_anuncios)) {
                        val packageNameGastinPro = "br.com.fcr.gastin"
                        val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageNameGastinPro")
                        val goToWebMarketIntent = Intent(Intent.ACTION_VIEW, webUri)
                        context.startActivity(goToWebMarketIntent)
                    }
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
                HomeScreenInformes(valorReceitasBusca,valorReceitasBusca - valorDespesasBusca,valorReceitasBusca - valorDespesasBusca)
                Spacer(modifier = Modifier.size(32.dp))
            }
            //Visao geral de informacoes
            item {
                HomeScreenVisaoGeral(
                    valorReceitas = valorReceitas,
                    valorDespesas = valorDespesas,
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
                HomeScreenDashboard(categoriasInforms,{
                    openDropDownDashboard = true
                }){
                    DropDownMoreOptions(
                        customItem = {
                            Row(modifier = Modifier
                                .height(40.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = stringResource(R.string.txt_mostrar_periodo_completo), fontSize = 14.sp)
                                Switch(
                                    checked = Constants.IsTotalPeriod,
                                    onCheckedChange = {
                                        onInformsTotal(it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        uncheckedThumbColor = MaterialTheme.colors.onBackground
                                    )
                                )
                            }
                        },
                        listOptions = listOf(
                            Pair(stringResource(R.string.txt_ver_categorias)) {
                                navController.navigate(Route.LISTA_CATEGORIAS)
                            },
                            Pair(stringResource(R.string.txt_adicionar_categoria),{openDropUpNewCategory = true})
                        ),
                        enable = openDropDownDashboard,
                        onDismiss = {
                            openDropDownDashboard = false
                        })
                }
            }
            //Evolucao de despesas
            item {
                HomeScreenEvolucaoDespesas(values,days,
                    onBefore = onWeekBefore,
                    onNext = onWeekNext)
            }
            item{
                Spacer(modifier = Modifier.size(100.dp))//TODO
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)){
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .border(
                    1.dp,
                    MaterialTheme.colors.onBackground.copy(0.1f),
                    CircleShape
                )
                .clip(CircleShape),
            backgroundColor = MaterialTheme.colors.surface,
            onClick = {
                openDropUpNewRegister = true
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "icone adicioanr",
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
    HomeScreenDropUpNewRegister(
        enable = openDropUpNewRegister,
        IsDespesa = IsDespesa,
        setIsDespesa = {IsDespesa = it},
        onDismiss = {openDropUpNewRegister = false},
        onActionsResult = onNewRegister,
        Categorias = Categorias,
        CategoriaDefault = CategoriaDefault,
        onDateUpdate = onDateUpdate
    )
    DropUpNewCategory(
        enable = openDropUpNewCategory,
        onDismiss = {
            openDropUpNewCategory = false
        }
    ){
        onNewCategoria(it.toModel())
    }
}