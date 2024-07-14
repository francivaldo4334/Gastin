package br.com.fcr.gastin.ui.page.viewmodels

import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.data.database.model.Registro
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.concurrent.thread

data class RegistroViewModel(
    val Id: Int,
    val Description: String,
    val Date: String,
    val Value: Int,
    val CategoriaFk: Int? = null,
    val isRecurrent: Boolean = false,
    val startDate: String? = null,
    val endDate: String? = null,
    val isEverDays: Boolean = false
)
fun EmptyRegistroViewModel():RegistroViewModel{
    return RegistroViewModel(0,"","",0,0)
}
fun EmptyCategoriaViewModel():CategoriaViewModel{
    return CategoriaViewModel(0,"","","",0)
}
fun Registro.toView():RegistroViewModel{
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val dateString = formatter.format(this.CreateAT)
    val startDate = this.startDate?.let { formatter.format(it) }
    val endDate = this.endDate?.let { formatter.format(it) }
    return RegistroViewModel(
        Description = this.Description,
        Date = dateString,
        Value = this.Value,
        CategoriaFk = this.CategoriaFk,
        Id = this.Id,
        startDate = startDate,
        endDate = endDate,
        isRecurrent = this.isRecurrent,
        isEverDays = this.isEverDays
    )
}
fun RegistroViewModel.toModel(isDespesa:Boolean = true): Registro {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val startDate = this.startDate?.let { formatter.parse(it) }
    val endDate = this.endDate?.let { formatter.parse(it) }
    return Registro(
        IsDespesa = isDespesa,
        Id = this.Id,
        Description = this.Description,
        Value = this.Value,
        CategoriaFk = this.CategoriaFk?:1,
        startDate = startDate,
        endDate = endDate,
        isRecurrent = this.isRecurrent,
        isEverDays = this.isEverDays
    )
}