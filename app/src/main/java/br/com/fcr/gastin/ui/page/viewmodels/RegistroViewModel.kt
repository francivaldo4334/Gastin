package br.com.fcr.gastin.ui.page.viewmodels

import br.com.fcr.gastin.data.model.Registro
import java.text.SimpleDateFormat

data class RegistroViewModel(val Id:Int,val Description:String,val Date:String,val Value:Int,val CategoriaFk:Int? = null)
fun EmptyRegistroViewModel():RegistroViewModel{
    return RegistroViewModel(0,"","",0,0)
}
fun Registro.toView():RegistroViewModel{
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val dateString = formatter.format(this.CreateAT)
    return RegistroViewModel(
        Description = this.Description,
        Date = dateString,
        Value = this.Value,
        CategoriaFk = this.CategoriaFk,
        Id = this.Id
    )
}
fun RegistroViewModel.toModel():Registro{
    return Registro(
        Id = this.Id,
        Description = this.Description,
        Value = this.Value,
        CategoriaFk = this.CategoriaFk?:1
    )
}