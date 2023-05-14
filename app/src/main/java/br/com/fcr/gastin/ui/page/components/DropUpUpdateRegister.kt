package br.com.fcr.gastin.ui.page.components

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.R
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.utils.MaskTransformation
import br.com.fcr.gastin.ui.utils.Tetra

@Composable
fun DropUpUpdateRegister (enable:Boolean, onDismiss:()->Unit,IdRegeistro: Int,Categorias:List<CategoriaViewModel>,onActionsResult:(RegistroViewModel)->Unit) {
    BackHandler(enabled = enable) {
        onDismiss()
    }
    val txtCarregando = stringResource(R.string.txt_carregando)
    var Valor by remember{ mutableStateOf(txtCarregando) }
    var Descricao by remember{ mutableStateOf(txtCarregando) }
    var openDropDownCategoria by remember { mutableStateOf(false) }
    var Categoria by remember{ mutableStateOf(EmptyCategoriaViewModel())}

    val focusDescricao = remember {FocusRequester()}
    val focusManeger = LocalFocusManager.current
    val Density = LocalDensity.current
    val owner = LocalLifecycleOwner.current
    HomeActivity.homeViewModel.getRegistro(IdRegeistro).observe(owner){
        if(it == null)
            return@observe
        Valor = it.Value.toString()
        Descricao = it.Description
        HomeActivity.homeViewModel.getCategoria(if(it.CategoriaFk == 0) 1 else it.CategoriaFk).observe(owner){
            Categoria = it.toView()
        }
    }
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
                value = Valor,
                onValueChange = {
                    Valor = Regex("[^0-9]").replace(it,"")
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardActions = KeyboardActions(onNext = {focusDescricao.requestFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
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
                    .fillMaxWidth()
                    .focusRequester(focusDescricao),
                keyboardActions = KeyboardActions(onDone = {focusManeger.clearFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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
                            Id = IdRegeistro,
                            Description = Descricao,
                            Value = if(Valor.isEmpty()) 0 else Valor.toInt(),
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