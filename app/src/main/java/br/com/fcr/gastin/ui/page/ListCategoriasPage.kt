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
import br.com.fcr.gastin.ui.utils.Tetra
private var listIdCheckeds by mutableStateOf(listOf<Int>())
@Composable
fun ListCategoriasPage (navController: NavController, listItem:List<Tetra<String, String, Int, Int>>){
    var showAllCheckBox by remember { mutableStateOf(false) }
    var openMoreOptions by remember{ mutableStateOf(false) }
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
    Column(Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.txt_categorias), fontSize = 24.sp, modifier = Modifier.padding(start = 16.dp))
            Column {
                IconButton(onClick = {openMoreOptions = true}) {
                    Icon(painter = painterResource(id = R.drawable.ic_more_options_chorts), contentDescription = "")
                }
                DropDownMoreOptions(listOptions = listOf(
                    Pair(stringResource(R.string.txt_selecionar_tudo)){
                        showAllCheckBox = true
                        listIdCheckeds = listItem.map { it.tetra }
                    },
                    Pair(stringResource(R.string.txt_excluir)){},
                    Pair(stringResource(R.string.txt_adicionar)){},
                    Pair(stringResource(R.string.txt_editar)){},
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
            items(listItem){(titulo,descricao,cor,id)->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
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
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(cor)))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = titulo,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colors.onBackground,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = descricao,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f),
                                    maxLines = 1
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
                                checked = listIdCheckeds.any { it == id },
                                onCheckedChange = {
                                    if(it)
                                        listIdCheckeds += id
                                    else
                                        listIdCheckeds -= id

                                }
                            )
                        }
                    }
                    Divider()
                }
            }
        }
    }
}