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
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.utils.MaskTransformation
import br.com.fcr.gastin.ui.utils.Tetra

@Composable
fun DropUpUpdateRegister (
    enable:Boolean,
    onDismiss:()->Unit,
    Categorias:List<CategoriaViewModel>,
    registerId:Int,
    CategoriaId:Int,
    onCategoriaId:(Int)->Unit,
    Valor:String,
    Descricao:String,
    CategoriaCor:Color,
    CategoriaNome:String,
    onValor:(String)->Unit,
    onDescricao:(String)->Unit,
    onCategoriaCor:(Color)->Unit,
    onCategoriaNome:(String)->Unit,
    onActionsResult:(RegistroViewModel)->Unit
) {
    BackHandler(enabled = enable) {
        onDismiss()
    }
    var openDropDownCategoria by remember { mutableStateOf(false) }
    val focusManeger = LocalFocusManager.current
    val Density = LocalDensity.current
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
                    onValor(Regex("[^0-9]").replace(it,""))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                    onDescricao(it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
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
                        .border(4.dp, CategoriaCor, RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = CategoriaNome,
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
                                    onCategoriaId(it.Id)
                                    onCategoriaCor(Color(it.Color))
                                    onCategoriaNome(it.Name)
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
                            Id = registerId,
                            Description = Descricao,
                            Value = if(Valor.isEmpty()) 0 else Valor.toInt(),
                            CategoriaFk = CategoriaId,
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