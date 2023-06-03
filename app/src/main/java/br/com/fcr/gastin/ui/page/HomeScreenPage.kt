package br.com.fcr.gastin.ui.page

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
import br.com.fcr.gastin.R
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.viewmodel.DashboardWeek
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.utils.Route
import br.com.fcr.gastin.ui.page.components.*
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel
import br.com.fcr.gastin.ui.utils.toWeekString
import java.util.Calendar

@Composable
fun HomeScreenPage(
    navController: NavController,
    textMes:String,
    stringYear:String,
    onMonthBefore:()->Unit,
    onMonthNext:()->Unit,
    onSwitchTheme:(Boolean)->Unit,
    onNewRegister:(Boolean,RegistroViewModel)->Unit,
    onNewCategoria:(Categoria)->Unit,
    categoriasInforms:List<Triple<String,Int,Color>>,
    valorDespesas:Int,
    valorReceitas:Int,
    valorDespesasBusca:Int,
    valorReceitasBusca:Int,
    Categorias:List<CategoriaViewModel>,
    CategoriaDefault: CategoriaViewModel,
    onInformsTotal:(Boolean)->Unit,
    graphicInforms:List<DashboardWeek>
) {
    val calendar = Calendar.getInstance()
    var IsDespesa:Boolean? by remember {mutableStateOf(null)}
    var openDropUpNewCategory by remember{ mutableStateOf(false) }
    var openDropDownTop by remember {mutableStateOf(false)}
    var openDropDownDashboard by remember {mutableStateOf(false)}
    var openDropDownEvolucao by remember {mutableStateOf(false)}
    var openDropUpNewRegister by remember { mutableStateOf(false) }
    val values = graphicInforms.map { it.valor }
    val context = LocalContext.current
    val days = graphicInforms.map {
        calendar.time = it.date
        val week = calendar.get(Calendar.DAY_OF_WEEK)
        Pair(it.valor,week.toWeekString(context))
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
                        Switch(checked = Constants.IsDarkTheme, onCheckedChange = {
                            onSwitchTheme(it)
                        })
                    }
                },
                listOptions = listOf(
                    Pair(stringResource(R.string.txt_adicionar_categoria),{openDropUpNewCategory = true}),
                    Pair(stringResource(R.string.txt_adicionar_receita),{IsDespesa= false;openDropUpNewRegister = true}),
                    Pair(stringResource(R.string.txt_adicionar_despesa),{IsDespesa= true;openDropUpNewRegister = true}),
                    Pair(stringResource(R.string.txt_como_funciona),{}),
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
                        listOptions = listOf(
                            Pair(stringResource(R.string.txt_ver_categorias)) {
                                navController.navigate(Route.LISTA_CATEGORIAS)
                            },
                            Pair(stringResource(R.string.txt_mostrar_por_mes),{ onInformsTotal(false) }),
                            Pair(stringResource(R.string.txt_mostrar_periodo_completo),{ onInformsTotal(true) }),
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
                    onClick = {
                        openDropDownEvolucao = true
                    },
                    onBefore = {

                    },
                    onNext = {

                    }){
                    DropDownMoreOptions(
                        listOptions = listOf(
                            Pair(stringResource(R.string.txt_visualizar_por_mes),{}),
                            Pair(stringResource(R.string.txt_visualizar_por_quinzena),{}),
                            Pair(stringResource(R.string.txt_visualizar_por_semana),{})
                        ),
                        enable = openDropDownEvolucao,
                        onDismiss = {
                            openDropDownEvolucao = false
                        })
                }
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
        CategoriaDefault = CategoriaDefault
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