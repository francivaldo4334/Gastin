package br.com.fcr.gastin.ui.page.viewmodels

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.database.model.Registro
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

data class CategoriaViewModel(
    val Id: Int,
    val Name: String,
    val Description: String,
    val Date: String,
    val Color: Long,
    val isRecurrent: Boolean = false,
    val isEverDays: Boolean = false,
    val startDate: String? = null,
    val endDate:String? = null
)
@SuppressLint("SimpleDateFormat")
fun Categoria.toView():CategoriaViewModel{
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val dateString = formatter.format(this.CreateAT)
    val startDate = this.startDate?.let { formatter.format(it) }
    val endDate = this.endDate?.let { formatter.format(it) }
    return CategoriaViewModel(
        Description = this.Description,
        Name = this.Name,
        Date = dateString,
        Color = this.Color,
        Id = this.Id,
        isRecurrent = this.isRecurrent,
        isEverDays = this.isEverDays,
        startDate = startDate,
        endDate = endDate,
    )
}
fun CategoriaViewModel.toModel(): Categoria {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val startDate = this.startDate?.let { formatter.parse(it) }
    val endDate = this.endDate?.let { formatter.parse(it) }
    return Categoria(
        Id = this.Id,
        Description = this.Description,
        Color = this.Color,
        Name = this.Name,
        isRecurrent = this.isRecurrent,
        isEverDays = this.isEverDays,
        startDate = startDate,
        endDate = endDate,
    )
}