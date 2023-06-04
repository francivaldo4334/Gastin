package br.com.fcr.gastin.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.ui.page.components.DialogExcluir
import br.com.fcr.gastin.ui.page.components.DropDownMoreOptions
import br.com.fcr.gastin.ui.page.components.DropUpNewCategory
import br.com.fcr.gastin.ui.page.components.DropUpUpdateCategoria
import br.com.fcr.gastin.ui.page.components.DropUpViewCategoria
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel

private var listIdCheckeds by mutableStateOf(listOf<Int>())

var IdViewCategoria by mutableStateOf(0)
var IdUpdateCategoria by mutableStateOf(0)
var openDropUpUpdateCategoria by mutableStateOf(false)
var openDropUpViewCategoria by mutableStateOf(false)
var openDropUpNewCategory by mutableStateOf(false)
var showAllCheckBox by mutableStateOf(false)
@Composable
fun ListCategoriasPage (
    navController: NavController,
    listItem:List<CategoriaViewModel>,
    onDeleteCategoria:(List<Int>)->Unit,
    onNewCategoria:(Categoria)->Unit,
    onUpdateCategoria:(CategoriaViewModel)->Unit,
    onLoadCategoria:(Int,(CategoriaViewModel)->Unit)->Unit
){
    var Nome by remember{ mutableStateOf("") }
    var Descricao by remember{ mutableStateOf("") }
    var CategoriaCor by remember{ mutableStateOf(Color.White) }
    var openDialogExcluir by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var openMoreOptions by remember{ mutableStateOf(false) }
            Text(text = stringResource(R.string.txt_categorias), fontSize = 24.sp, modifier = Modifier.padding(start = 16.dp))
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
                                    openDropUpNewCategory = true
                                    listIdCheckeds = emptyList()
                                }, true),
                                Triple(stringResource(R.string.txt_excluir), {
                                    openDialogExcluir = true
                                }, listIdCheckeds.size > 0),
                                Triple(stringResource(R.string.txt_editar), {
                                    IdUpdateCategoria = listIdCheckeds.first()
                                    onLoadCategoria(IdUpdateCategoria){
                                        CategoriaCor = Color(it.Color)
                                        Nome = it.Name
                                        Descricao = it.Description
                                    }
                                    openDropUpUpdateCategoria = true
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
            .padding(horizontal = 16.dp)
        ){
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(listItem){item->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        listIdCheckeds += item.Id
                                        showAllCheckBox = listIdCheckeds.isNotEmpty()
                                    },
                                    onTap = {
                                        IdViewCategoria = item.Id
                                        onLoadCategoria(item.Id){
                                            Nome = it.Name
                                            Descricao = it.Description
                                            CategoriaCor = Color(it.Color)
                                        }
                                        openDropUpViewCategoria = true
                                    }
                                )
                            }
                            .height(64.dp)
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(item.Color)))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item.Name,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colors.onBackground,
                                    maxLines = 1,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = item.Description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f),
                                    maxLines = 1,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = showAllCheckBox,
                            enter = expandHorizontally() + slideInHorizontally(),
                            exit = shrinkHorizontally() + slideOutHorizontally()
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Checkbox(
                                checked = listIdCheckeds.any { it == item.Id },
                                onCheckedChange = {
                                    if(it)
                                        listIdCheckeds += item.Id
                                    else
                                        listIdCheckeds -= item.Id

                                }
                            )
                        }
                    }
                    Divider()
                }
            }
        }
    }
    DropUpNewCategory(
        enable = openDropUpNewCategory,
        onDismiss = {
            openDropUpNewCategory = false
        }
    ){
        onNewCategoria(it.toModel())
    }
    DropUpViewCategoria(
        enable = openDropUpViewCategoria,
        onDismiss = { openDropUpViewCategoria = false },
        Nome = Nome,
        Descricao = Descricao,
        CategoriaCor = CategoriaCor,
    )
    DropUpUpdateCategoria(
        enable = openDropUpUpdateCategoria,
        onDismiss = { openDropUpUpdateCategoria = false },
        IdCategoria = IdUpdateCategoria,
        onActionsResult = onUpdateCategoria,
        colorSelect = CategoriaCor,
        Nome = Nome,
        Descricao = Descricao,
        oncolorSelect = {CategoriaCor = it},
        onNome = {Nome = it},
        onDescricao = {Descricao = it},
    )
    DialogExcluir(
        enable = openDialogExcluir,
        onDismiss = {
            openDialogExcluir = false
        }
    ) {
        onDeleteCategoria(listIdCheckeds)
        listIdCheckeds = emptyList()
    }
    if(listIdCheckeds.isEmpty()){
        showAllCheckBox = false
    }
    BackHandler {
        if(showAllCheckBox) {
            showAllCheckBox = false
            listIdCheckeds = emptyList()
        }
        else
            navController.popBackStack()
    }
}