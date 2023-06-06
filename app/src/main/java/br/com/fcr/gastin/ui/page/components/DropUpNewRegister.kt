package br.com.fcr.gastin.ui.page.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.utils.MaskTransformation
import br.com.fcr.gastin.ui.utils.Tetra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DropUpNewRegister (
    enable:Boolean,
    Categorias:List<CategoriaViewModel>,
    onDismiss:()->Unit,
    onActionsResult:(RegistroViewModel)->Unit,
    CategoriaDefault:CategoriaViewModel,
    onDateUpdate:()->Unit
){
    val focusManager = LocalFocusManager.current
    var Valor by remember{ mutableStateOf("") }
    var Descricao by remember{ mutableStateOf("") }
    var openDropDownCategoria by remember { mutableStateOf(false) }
    var Categoria by remember{ mutableStateOf(CategoriaDefault) }
    val Density = LocalDensity.current
    BackHandler(enabled = enable) {
        onDismiss()
    }
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                    value = Valor,
                    onValueChange = {
                        Valor = Regex("[^0-9]").replace(it,"")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)}),
                    visualTransformation = if(Valor.isEmpty()) VisualTransformation.None else MaskTransformation()
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
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column{
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
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { openDropDownCategoria = true }
                            .border(4.dp, Color(Categoria.Color), RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            text = Categoria.Name,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Icon(
                            painter = painterResource(
                                id = if(openDropDownCategoria)
                                    R.drawable.ic_arrow_up
                                else
                                    R.drawable.ic_arrow_down
                            ),
                            contentDescription = "",
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = openDropDownCategoria,
                        onDismissRequest = { openDropDownCategoria = false },
                        modifier = Modifier
                            .width(
                                with(Density){
                                    width.toDp()
                                }
                            )
                    ) {
                        Categorias.forEach {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        Categoria = it
                                        openDropDownCategoria = false
                                    }
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = it.Name)
                                Box(modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(it.Color)))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                    TextButton(onClick = {
                        onActionsResult(
                            RegistroViewModel(
                                Id = 0,
                                Description = Descricao,
                                Value = if(Valor.isEmpty()) 0 else Valor.toInt(),
                                CategoriaFk = Categoria.Id,
                                Date = ""
                            )
                        )
                        onDismiss()
                        onDateUpdate()
                    }) {
                        Text(text = stringResource(R.string.txt_salvar))
                    }
                }
            }
        }
    }
}