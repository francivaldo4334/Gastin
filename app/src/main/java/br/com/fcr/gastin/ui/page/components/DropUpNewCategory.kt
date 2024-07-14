package br.com.fcr.gastin.ui.page.components

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.utils.colorToLongHex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("WeekBasedYear")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropUpNewCategory(
    enable: Boolean, onDismiss: () -> Unit, onActionsResult: (CategoriaViewModel) -> Unit
) {
    val context = LocalContext.current
    val focusManeger = LocalFocusManager.current
    val density = LocalDensity.current
    var name by remember { mutableStateOf("") }
    var descripton by remember { mutableStateOf("") }
    var colorSelect by remember { mutableStateOf(Color(0xFFD4D4D4)) }
    var openDropDownColorPicker by remember { mutableStateOf(false) }
    val focusValue = remember { FocusRequester() }
    val focusDesc = remember { FocusRequester() }
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
        LaunchedEffect(key1 = Unit) {
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
                        Text(text = stringResource(R.string.txt_nome))
                    },
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusValue),
                    shape = RoundedCornerShape(16.dp),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManeger.clearFocus()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(700)
                            focusDesc.requestFocus()
                        }
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    label = {
                        Text(text = stringResource(R.string.txt_descricao))
                    },
                    value = descripton,
                    onValueChange = {
                        descripton = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusDesc),
                    keyboardActions = KeyboardActions(onDone = { focusManeger.clearFocus() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { openDropDownColorPicker = true }
                    .padding(end = 16.dp)) {
                    var width by remember { mutableStateOf(0) }
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .onGloballyPositioned {
                                width = it.size.width
                            }) {
                        Text(
                            text = stringResource(R.string.txt_definir_cor),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(colorSelect)
                        )
                    }
                    DropdownMenu(
                        expanded = openDropDownColorPicker,
                        onDismissRequest = { openDropDownColorPicker = false },
                        modifier = androidx.compose.ui.Modifier.width(with(density) {
                            width.toDp()
                        })
                    ) {
                        ColorPicker(width = width, onColorSelected = {
                            colorSelect = it
                        })
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    TextButton(onClick = {
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.txt_salvar))
                    }
                }
            }
        }
    }
    BackHandler(enabled = enable) {
        onDismiss()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DropUpNewCategoryPreview() {
    DropUpNewCategory(enable = true, onDismiss = { /*TODO*/ }) {

    }
}