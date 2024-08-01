package br.com.fcr.gastin.ui.page.components

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fcr.gastin.R
import java.time.format.DateTimeFormatter
import java.util.Locale

class RecurrentFormData(
    isRecurrent: Boolean = false,
    isEverDays: Boolean = false,
    startDate: String? = null,
    endDate: String? = null,
) {
    var isRecurrent = mutableStateOf(isRecurrent)
    var isEverDays = mutableStateOf(isEverDays)
    var startDate = mutableStateOf(startDate)
    var endDate = mutableStateOf(endDate)
    fun isValid(): Pair<Boolean, String> {
        if (isRecurrent.value) {
            if (!isEverDays.value) {
                if (startDate.value == null)
                    return false to "Data de inicio da vigência não pode ser nula."
                if (endDate.value == null)
                    return false to "Data de terminiu da vigência não pode ser nula."
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val startDate = formatter.parse(startDate.value)
                val endDate = formatter.parse(endDate.value)

                if (startDate.after(endDate))
                    return false to "A data inicial não pode ser maior que a data final."
            }
            return true to ""
        }
        isEverDays.value = false
        startDate.value = null
        endDate.value = null
        return true to ""
    }
}

private class DateVisualTransformation(
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        text.text.forEachIndexed { index, char ->
            when (index) {
                2 -> out += "/$char"
                4 -> out += "/$char"
                else -> out += char
            }
        }
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 4) return offset + 1
                return offset + 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return offset - 2
            }
        }
        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrentForm(recurrentForm: RecurrentFormData) {
    var stateDateInit = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )
    var stateDateEnd = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )
    var openDialogDatePickerInit by remember {
        mutableStateOf(false)
    }
    var openDialogDatePickerEnd by remember {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.height(16.dp))
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
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
            Switch(
                checked = recurrentForm.isRecurrent.value,
                onCheckedChange = { recurrentForm.isRecurrent.value = it })
        }
        AnimatedVisibility(
            visible = recurrentForm.isRecurrent.value,
            enter = expandVertically(),
            exit = shrinkVertically()
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
                    Switch(
                        checked = recurrentForm.isEverDays.value,
                        onCheckedChange = { recurrentForm.isEverDays.value = it })
                }
                AnimatedVisibility(
                    visible = !recurrentForm.isEverDays.value,
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
                            val textDateSelector: @Composable RowScope.(date: String?, label: String, onClick: () -> Unit) -> Unit =
                                { date, label, onClick ->
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = label, fontSize = 12.sp)
                                        TextButton(
                                            enabled = !recurrentForm.isEverDays.value,
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
                                                text = date ?: "DD/MM/AAAA"
                                            )
                                        }
                                    }
                                }
                            textDateSelector(
                                recurrentForm.startDate.value,
                                stringResource(R.string.in_cil)
                            ) {
                                openDialogDatePickerInit = true
                            }
                            textDateSelector(
                                recurrentForm.endDate.value,
                                stringResource(R.string.fim)
                            ) {
                                openDialogDatePickerEnd = true
                            }
                        }
                    }
                }
            }
        }
    }
    val datePickerDialog: @Composable (date: String?, onDismissRequest: () -> Unit, state: DatePickerState, title: String, onDate: (String) -> Unit) -> Unit =
        { date, onDismissRequest, state, title, onDate ->
            val focus = remember {
                FocusRequester()
            }
            Dialog(onDismissRequest = onDismissRequest) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var text by remember {
                        mutableStateOf("")
                    }
                    var isValid by remember {
                        mutableStateOf(false)
                    }
                    OutlinedTextField(
                        isError = !isValid,
                        value = text,
                        onValueChange = {
                            if (it.length <= 8) {
                                text = it
                            }
                            if (text.length == 8) {
                                val formatter = SimpleDateFormat("ddMMyyyy")
                                formatter.isLenient = false
                                isValid = try {
                                    formatter.parse(it)
                                    true
                                } catch (e: Exception) {
                                    false
                                }
                                if (isValid) {
                                    val dia = text.substring(0, 2)
                                    val mes = text.substring(2, 4)
                                    val ano = text.subSequence(4, 8)
                                    onDate("$dia/$mes/$ano")
                                    focus.freeFocus()
                                }
                            }
                        },
                        label = { Text("Data (ddMMyyyy)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (isValid) {
                                    focus.freeFocus()
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focus)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        visualTransformation = DateVisualTransformation()
                    )
                    Button(
                        enabled = isValid,
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .padding(horizontal = 12.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    if (openDialogDatePickerInit) {
        datePickerDialog(
            recurrentForm.startDate.value,
            { openDialogDatePickerInit = false },
            stateDateInit,
            stringResource(R.string.date_de_in_cio),
        ) {
            recurrentForm.startDate.value = it
        }
    }
    if (openDialogDatePickerEnd) {
        datePickerDialog(
            recurrentForm.endDate.value,
            { openDialogDatePickerEnd = false },
            stateDateEnd,
            stringResource(R.string.data_de_fim),
        ) {
            recurrentForm.endDate.value = it
        }
    }
}
