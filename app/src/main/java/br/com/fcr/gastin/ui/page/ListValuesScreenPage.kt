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
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.components.DropDownMoreOptions
import br.com.fcr.gastin.ui.page.components.DropUpNewRegister
import br.com.fcr.gastin.ui.page.components.DropUpUpdateRegister
import br.com.fcr.gastin.ui.page.components.DropUpViewRegister
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyRegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.utils.toMonetaryString

private var listIdCheckeds by mutableStateOf(listOf<Int>())
@Composable
private fun listOptions(listOptions:List<Triple<String,()->Unit,Boolean>>,onDismiss:()->Unit){
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

@Composable
fun ListValuesScreenPage(
    navController: NavController,
    title:String,
    listItem:List<RegistroViewModel>,
    onNewRegister:(RegistroViewModel)->Unit,
    Categorias:List<CategoriaViewModel>
){
    var showAllCheckBox by remember {mutableStateOf(false)}
    var openMoreOptions by remember{ mutableStateOf(false) }
    var openViewItem by remember { mutableStateOf(false) }
    var openUpdateItem by remember { mutableStateOf(false) }
    var openNewItem by remember { mutableStateOf(false) }
    var IdSelect by remember{ mutableStateOf(0) }
    var Categoria by remember { mutableStateOf(CategoriaViewModel(0,"","","",0)) }
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
                                Triple(stringResource(R.string.txt_excluir), {}, listIdCheckeds.size > 0),
                                Triple(stringResource(R.string.txt_editar), {
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
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
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
                                        IdSelect = register.Id
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    maxLines = 1
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
        IdRegister = IdSelect,
        enable = openViewItem,
        onDismiss = { openViewItem = false }
    )
//    DropUpUpdateRegister(
//        enable = openUpdateItem,
//        onDismiss = { openUpdateItem = false },
//        _categoria = Categoria,
//        Categorias = Categorias,
//        registro = IdSelect,
//        onActionsResult = {
//            listIdCheckeds = emptyList()
//        }
//    )
    DropUpNewRegister(
        enable = openNewItem,
        onDismiss = {openNewItem = false},
        onActionsResult = onNewRegister,
        Categorias = Categorias
    )
}
