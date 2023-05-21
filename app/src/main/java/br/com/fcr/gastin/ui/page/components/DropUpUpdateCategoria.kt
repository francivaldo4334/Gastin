package br.com.fcr.gastin.ui.page.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel

@Composable
fun DropUpUpdateCategoria (
    enable:Boolean, onDismiss:()->Unit,
    IdRegeistro: Int,
    Categorias:List<CategoriaViewModel>,
    onActionsResult:(RegistroViewModel)->Unit,
    onLoadCategoria:(Int,(String)->Unit,(String)->Unit,(CategoriaViewModel)->Unit)->Unit
){
    var colorSelect by remember{ mutableStateOf(Color(0xFFD4D4D4)) }
    var openDropDownColorPicker by remember {mutableStateOf(false)}
    BackHandler(enabled = enable) {
        onDismiss()
    }
    val txtCarregando = stringResource(R.string.txt_carregando)
    var Nome by remember{ mutableStateOf(txtCarregando) }
    var Descricao by remember{ mutableStateOf(txtCarregando) }
    var openDropDownCategoria by remember { mutableStateOf(false) }
    var Categoria by remember{ mutableStateOf(EmptyCategoriaViewModel()) }

    val focusDescricao = remember { FocusRequester() }
    val focusManeger = LocalFocusManager.current
    val Density = LocalDensity.current
    onLoadCategoria(
        IdRegeistro,
        { Nome = it },
        { Descricao = it },
        { Categoria = it }
    )
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_inserir_dados),
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.txt_valor))
                },
                value = Nome,
                onValueChange = {
                    Nome = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardActions = KeyboardActions(onNext = {focusDescricao.requestFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.txt_descricao))
                },
                value = Descricao,
                onValueChange = {
                    Descricao = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusDescricao),
                keyboardActions = KeyboardActions(onDone = {focusManeger.clearFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column( modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { openDropDownColorPicker = true }
                .padding(horizontal = 16.dp)
            ){
                var width by remember{ mutableStateOf(0) }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .onGloballyPositioned {
                            width = it.size.width
                        }
                ) {
                    Text(
                        text = stringResource(R.string.txt_definir_cor),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 32.dp)
                    )
                    Box(modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorSelect))
                }
                DropdownMenu(
                    expanded = openDropDownColorPicker,
                    onDismissRequest = { openDropDownColorPicker = false },
                    modifier = Modifier
                        .width(
                            with(Density){
                                width.toDp()
                            }
                        )
                ) {
                    ColorPicker(
                        width = width,
                        onColorSelected = {
                            colorSelect = it
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                TextButton(onClick = {
                    onActionsResult(
                        RegistroViewModel(
                            Id = IdRegeistro,
                            Description = Descricao,
                            Value = if(Nome.isEmpty()) 0 else Nome.toInt(),
                            CategoriaFk = Categoria.Id,
                            Date = ""
                        )
                    )
                    onDismiss()
                }) {
                    Text(text = stringResource(R.string.txt_salvar))
                }
            }
        }
    }
}