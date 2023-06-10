package br.com.fcr.gastin.free.ui.page.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.free.R
import br.com.fcr.gastin.free.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.free.ui.utils.colorToLongHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DropUpUpdateCategoria (
    enable:Boolean, onDismiss:()->Unit,
    IdCategoria: Int,
    onActionsResult:(CategoriaViewModel)->Unit,
    colorSelect:Color,
    Nome:String,
    Descricao:String,
    oncolorSelect:(Color)->Unit,
    onNome:(String)->Unit,
    onDescricao:(String)->Unit,
){
    var openDropDownColorPicker by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current
    val focusDesc = remember { FocusRequester() }
    val focusName = remember { FocusRequester() }
    val Density = LocalDensity.current
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
        LaunchedEffect(key1 = Unit) {
            delay(500)
            focusName.requestFocus()
        }
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
                    Text(text = stringResource(R.string.txt_nome))
                },
                value = Nome,
                onValueChange = {
                    onNome(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusName),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(700)
                        focusDesc.requestFocus()
                    }
                })
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.txt_descricao))
                },
                value = Descricao,
                onValueChange = {
                    onDescricao(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusDesc),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
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
                            oncolorSelect(it)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                TextButton(onClick = {
                    onActionsResult(
                        CategoriaViewModel(
                            Id = IdCategoria,
                            Name = Nome,
                            Description = Descricao,
                            Date = "",
                            Color = colorToLongHex(colorSelect)
                        )
                    )
                    onDismiss()
                }) {
                    Text(text = stringResource(R.string.txt_salvar))
                }
            }
        }
    }
    BackHandler(enabled = enable) {
        onDismiss()
    }
}