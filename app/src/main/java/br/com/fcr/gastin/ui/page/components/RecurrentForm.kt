package br.com.fcr.gastin.ui.page.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fcr.gastin.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
}

private fun timestampToDate(timestamp: Long): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        formatter.format(localDateTime)
    } else {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(timestamp)
        formatter.format(date)
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
    val datePickerDialog: @Composable (onDismissRequest: () -> Unit, state: DatePickerState, title: String, onDate: (String) -> Unit) -> Unit =
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
                            onDate(timestampToDate(it))
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
            { openDialogDatePickerInit = false },
            stateDateInit,
            stringResource(R.string.date_de_in_cio),
        ) {
            recurrentForm.startDate.value = it
        }
    }
    if (openDialogDatePickerEnd) {
        datePickerDialog(
            { openDialogDatePickerEnd = false },
            stateDateEnd,
            stringResource(R.string.data_de_fim),
        ) {
            recurrentForm.endDate.value = it
        }
    }
}
