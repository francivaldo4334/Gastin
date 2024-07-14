package br.com.fcr.gastin.ui.page.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.utils.MaskTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
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
    val focusDesc = remember { FocusRequester() }
    val focusValue = remember { FocusRequester() }
    var Valor by remember{ mutableStateOf("") }
    var Descricao by remember{ mutableStateOf("") }
    var openDropDownCategoria by remember { mutableStateOf(false) }
    var Categoria by remember{ mutableStateOf(CategoriaDefault) }
    val Density = LocalDensity.current
    var isRecurrent by remember {
        mutableStateOf(false)
    }
    var initialDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    var endDate by remember {
        mutableStateOf<LocalDate?>(null)
    }
    var isEverDays by remember {
        mutableStateOf(true)
    }
    var stateDateInit = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )
    var stateDateEnd = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )
    val localOnDismiss = {
        onDismiss.invoke()
        initialDate = null
        endDate = null
        isRecurrent = false
        isEverDays = true
    }
    var openDialogDatePickerInit by remember {
        mutableStateOf(false)
    }
    var openDialogDatePickerEnd by remember {
        mutableStateOf(false)
    }

    BoxDropUpContent(enable = enable, onDismiss = localOnDismiss) {
        LaunchedEffect(key1 = Unit){
            delay(500)
            focusValue.requestFocus()
        }
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
                        .fillMaxWidth()
                        .focusRequester(focusValue),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(700)
                            focusDesc.requestFocus()
                        }
                    }),
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
                        .focusRequester(focusDesc),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recorr_ncia),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Switch(checked = isRecurrent, onCheckedChange = { isRecurrent = it })
                    }
                    AnimatedVisibility(
                        visible = isRecurrent, enter = expandVertically(), exit = shrinkVertically()
                    ) {
                        Column {
                            Divider()
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.todos_os_dias),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                                Switch(checked = isEverDays, onCheckedChange = { isEverDays = it })
                            }
                            AnimatedVisibility(
                                visible = !isEverDays,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(Modifier.fillMaxWidth()) {
                                    Divider()
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stringResource(R.string.vig_ncia),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        val textDateSelector: @Composable RowScope.(date: LocalDate?, label: String, onClick: () -> Unit) -> Unit =
                                            { date, label, onClick ->
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = label, fontSize = 12.sp)
                                                    TextButton(
                                                        enabled = !isEverDays,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .border(
                                                                2.dp,
                                                                MaterialTheme.colorScheme.onBackground.copy(
                                                                    0.5f
                                                                ),
                                                                OutlinedTextFieldDefaults.shape,
                                                            ),
                                                        onClick = onClick,
                                                        shape = OutlinedTextFieldDefaults.shape,
                                                    ) {
                                                        Text(
                                                            text = date?.format(
                                                                DateTimeFormatter.ofPattern("dd/MM/YYYY")
                                                            ) ?: "DD/MM/AAAA"
                                                        )
                                                    }
                                                }
                                            }
                                        textDateSelector(
                                            initialDate,
                                            stringResource(R.string.in_cil)
                                        ) {
                                            openDialogDatePickerInit = true
                                        }
                                        textDateSelector(endDate, stringResource(R.string.fim)) {
                                            openDialogDatePickerEnd = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                                Date = "",
                                isRecurrent=isRecurrent,
                                startDate=initialDate?.format(DateTimeFormatter.ofPattern("YYYY-mm-dd")),
                                endDate=endDate?.format(DateTimeFormatter.ofPattern("YYYY-mm-dd")),
                                isEverDays=isEverDays
                            )
                        )
                        localOnDismiss()
                        onDateUpdate()
                    }) {
                        Text(text = stringResource(R.string.txt_salvar))
                    }
                }
            }
        }
    }
    BackHandler(enabled = enable) {
        localOnDismiss()
    }
    val datePickerDialog: @Composable (onDismissRequest: () -> Unit, state: DatePickerState, title: String, onDate: (LocalDate) -> Unit) -> Unit =
        { onDismissRequest, state, title, onDate ->
            Dialog(onDismissRequest = onDismissRequest) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DatePicker(
                        dateValidator = {
                            val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDate(date)
                            true
                        },
                        state = state,
                        showModeToggle = false,
                        headline = {
                            Text(
                                text = title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp
                            )
                        },
                        title = null
                    )
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .fillMaxWidth().padding(bottom = 12.dp).padding(horizontal = 12.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    if (openDialogDatePickerInit) {
        datePickerDialog(
            { openDialogDatePickerInit = false },
            stateDateInit,
            stringResource(R.string.date_de_in_cio),
        ) {
            initialDate = it
        }
    }
    if (openDialogDatePickerEnd) {
        datePickerDialog(
            { openDialogDatePickerEnd = false },
            stateDateEnd,
            stringResource(R.string.data_de_fim),
        ) {
            endDate = it
        }
    }
}