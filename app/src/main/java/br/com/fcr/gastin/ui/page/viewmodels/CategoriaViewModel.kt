package br.com.fcr.gastin.ui.page.viewmodels

import androidx.compose.ui.graphics.Color
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.database.model.Registro
import java.text.SimpleDateFormat
import java.util.Date

data class CategoriaViewModel(val Id:Int,val Name:String,val Description:String,val Date:String,val Color:Long)
fun Categoria.toView():CategoriaViewModel{
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val dateString = formatter.format(this.CreateAT)
    return CategoriaViewModel(
        Description = this.Description,
        Name = this.Name,
        Date = dateString,
        Color = this.Color,
        Id = this.Id
    )
}
fun CategoriaViewModel.toModel(): Categoria {
    return Categoria(
        Id = this.Id,
        Description = this.Description,
        Color = this.Color,
        Name = this.Name
    )
}