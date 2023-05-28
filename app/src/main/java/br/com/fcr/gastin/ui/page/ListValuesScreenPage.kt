package br.com.fcr.gastin.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.components.DropDownMoreOptions
import br.com.fcr.gastin.ui.page.components.DropUpNewRegister
import br.com.fcr.gastin.ui.page.components.DropUpUpdateRegister
import br.com.fcr.gastin.ui.page.components.DropUpViewRegister
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyRegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.utils.toMonetaryString

private var listIdCheckeds by mutableStateOf(listOf<Int>())
@Composable
fun listOptions(listOptions:List<Triple<String,()->Unit,Boolean>>,onDismiss:()->Unit){
    listOptions.forEach {
        Row(modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .clickable(enabled = it.third) { it.second();onDismiss() }
            .padding(start = 16.dp, end = 72.dp),
            verticalAlignment = Alignment.CenterVertically) {
            var text = it.first
            if(it.first.first() == '+') {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "",
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Green.copy(if (it.third) 1f else 0.3f)),
                    tint = MaterialTheme.colors.background
                )
                text = text.removePrefix("+")
            }
            else if(it.first.first() == '-') {
                Icon(
                    painter = painterResource(id = R.drawable.ic_minus),
                    contentDescription = "",
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Red.copy(if (it.third) 1f else 0.3f)),
                    tint = MaterialTheme.colors.background
                )
                text = text.removePrefix("-")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, fontSize = 14.sp,color = MaterialTheme.colors.onBackground.copy(if(it.third)  1f else 0.3f))
        }
    }
}
var openUpdateItem by mutableStateOf(false)
var openNewItem by mutableStateOf(false)
@Composable
fun ListValuesScreenPage(
    navController: NavController,
    title:String,
    listItem:List<RegistroViewModel>,
    onNewRegister:(RegistroViewModel)->Unit,
    onDeleteRegister:(List<Int>)->Unit,
    onUpdateRegister:(RegistroViewModel)->Unit,
    onLoadRegister:(Int,(RegistroViewModel)->Unit)->Unit,
    Categorias:List<CategoriaViewModel>,
    CategoriaDefault:CategoriaViewModel,
    onLoadCategory:(Int,(CategoriaViewModel)->Unit)->Unit
){
    var showAllCheckBox by remember {mutableStateOf(false)}
    var openMoreOptions by remember{ mutableStateOf(false) }
    var openViewItem by remember { mutableStateOf(false) }
    var registerId by remember{ mutableStateOf(0) }
    var CategoriaId by remember{ mutableStateOf(0) }
    var Valor by remember{ mutableStateOf("") }
    var Descricao by remember{ mutableStateOf("") }
    var CategoriaCor by remember { mutableStateOf(Color(HomeActivity.CategoriaDefault.Color)) }
    var CategoriaNome by remember { mutableStateOf(HomeActivity.CategoriaDefault.Name)}
    if(listIdCheckeds.isEmpty()){ showAllCheckBox = false }
    BackHandler {
        if(showAllCheckBox) {
            showAllCheckBox = false
            listIdCheckeds = emptyList()
        }
        else
            navController.popBackStack()
    }
    Column(Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 24.sp, modifier = Modifier.padding(start = 16.dp))
            Column {
                IconButton(onClick = {openMoreOptions = true}) {
                    Icon(painter = painterResource(id = R.drawable.ic_more_options_chorts), contentDescription = "")
                }
                DropDownMoreOptions(
                    customItem = {
                        listOptions(
                            listOptions = listOf(
                                Triple(stringResource(R.string.txt_selecionar_tudo), {
                                    showAllCheckBox = true
                                    listIdCheckeds = listItem.map { it.Id }
                                }, true),
                                Triple(stringResource(R.string.txt_adicionar), {
                                    openNewItem = true
                                    listIdCheckeds = emptyList()
                                }, true),
                                Triple(stringResource(R.string.txt_excluir), {
                                    onDeleteRegister(listIdCheckeds)
                                    listIdCheckeds = emptyList()
                                }, listIdCheckeds.size > 0),
                                Triple(stringResource(R.string.txt_editar), {
                                    onLoadRegister(listIdCheckeds.first()){
                                        registerId = it.Id
                                        CategoriaId = it.CategoriaFk?:1
                                        Valor = it.Value.toString()
                                        Descricao = it.Description
                                        onLoadCategory(CategoriaId){
                                            CategoriaCor = Color(it.Color)
                                            CategoriaNome = it.Name
                                        }
                                    }
                                    openUpdateItem = true
                                }, listIdCheckeds.size == 1),
                            ),
                            onDismiss = {
                                openMoreOptions = false
                            }
                        )
                    },
                    listOptions = emptyList(),
                    enable = openMoreOptions) {
                    openMoreOptions = false
                }
            }
        }
        if(listItem.isEmpty())
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = stringResource(R.string.txt_sem_registros))
            }
        else
            LazyColumn(modifier = Modifier
                .fillMaxSize()
            ){
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                items(listItem){ register->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            listIdCheckeds += register.Id
                                            showAllCheckBox = listIdCheckeds.isNotEmpty()
                                        },
                                        onTap = {
                                            onLoadRegister(register.Id) {
                                                Valor = it.Value.toMonetaryString()
                                                Descricao = it.Description
                                                CategoriaId = it.CategoriaFk ?: 1
                                                onLoadCategory(CategoriaId) {
                                                    CategoriaCor = Color(it.Color)
                                                    CategoriaNome = it.Name
                                                }
                                            }
                                            openViewItem = true
                                        }
                                    )
                                }
                                .height(64.dp)
                                .padding(top = 12.dp, bottom = 8.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_dolar),
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = register.Description,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.onBackground.copy(0.5f),
                                        maxLines = 1,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = register.Date,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.onBackground.copy(0.5f),
                                        maxLines = 1
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = register.Value.toMonetaryString(), fontSize = 12.sp)
                                AnimatedVisibility(
                                    visible = showAllCheckBox,
                                    enter = expandHorizontally() + slideInHorizontally(),
                                    exit = shrinkHorizontally() + slideOutHorizontally()
                                ) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Checkbox(
                                        checked = listIdCheckeds.any { it == register.Id },
                                        onCheckedChange = {
                                            if(it)
                                                listIdCheckeds += register.Id
                                            else
                                                listIdCheckeds -= register.Id

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
    DropUpViewRegister(
        enable = openViewItem,
        onDismiss = { openViewItem = false },
        Valor = Valor,
        Descricao = Descricao,
        CategoriaCor = CategoriaCor,
        CategoriaNome = CategoriaNome
    )
    DropUpUpdateRegister(enable = openUpdateItem, onDismiss = { openUpdateItem = false }, Categorias = Categorias, registerId = registerId, CategoriaId = CategoriaId, Valor = Valor, Descricao = Descricao, CategoriaCor = CategoriaCor, CategoriaNome = CategoriaNome, onCategoriaId = { CategoriaId = it }, onValor = { Valor = it }, onDescricao = { Descricao = it }, onCategoriaCor = { CategoriaCor = it }, onCategoriaNome = { CategoriaNome = it }, onActionsResult = { onUpdateRegister(it); listIdCheckeds = emptyList() })
    DropUpNewRegister(
        enable = openNewItem,
        onDismiss = {openNewItem = false},
        onActionsResult = onNewRegister,
        Categorias = Categorias,
        CategoriaDefault = CategoriaDefault
    )
}
