package br.com.fcr.gastin

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.data.repository.ICategoriaRepository
import br.com.fcr.gastin.data.repository.IRegistroRepository
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.utils.toMonetaryString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel constructor(
    private val categoriaRepository: ICategoriaRepository,
    private val registroRepository: IRegistroRepository
) : ViewModel(){
    fun getDespesas():LiveData<List<Registro>>{
        return registroRepository.getAllDespesas()
    }
    fun getReceitas():LiveData<List<Registro>>{
        return registroRepository.getAllReceitas()
    }
    fun getCategoria(ID:Int,onREsponse:(Categoria?)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            onREsponse(categoriaRepository.getById(ID))
        }
    }
    fun setCategoria(it:Categoria){
        viewModelScope.launch(Dispatchers.IO) {
            categoriaRepository.insert(it)
        }
        HomeActivity.loadNewInformations()
    }
    fun setDespesa(it:RegistroViewModel){
        viewModelScope.launch(Dispatchers.IO){
            registroRepository.insert(
                Registro(
                    IsDespesa = true,
                    Description = it.Description,
                    Value = it.Value,
                    CategoriaFk = if(it.CategoriaFk==null || it.CategoriaFk == 0) 1 else it.CategoriaFk!!
                )
            )
        }
        HomeActivity.loadNewInformations()
    }
    fun setReceita(it:RegistroViewModel){
        viewModelScope.launch(Dispatchers.IO){
            registroRepository.insert(
                Registro(
                    IsDespesa = false,
                    Description = it.Description,
                    Value = it.Value,
                    CategoriaFk = if(it.CategoriaFk==null || it.CategoriaFk == 0) 1 else it.CategoriaFk!!
                )
            )
        }
        HomeActivity.loadNewInformations()
    }
    fun updateRegister(view:RegistroViewModel){
        getRegistro(view.Id){
            if(it == null)
                return@getRegistro
            val registro = it
            registro.Description = view.Description
            registro.Value = view.Value
            registro.CategoriaFk = if(view.CategoriaFk == null||view.CategoriaFk == 0)1 else view.CategoriaFk
            registroRepository.insert(
                registro
            )
        }
        HomeActivity.loadNewInformations()
    }

    fun getCategorias(onREsponse: (List<Categoria>) -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            onREsponse(categoriaRepository.getAll())
        }
    }

    fun getRegistros(): LiveData<List<Registro>>{
        return registroRepository.getall()
    }
    fun getRegistro(ID: Int,onREsponse: (Registro?) -> Unit){
        viewModelScope.launch(Dispatchers.IO){
            onREsponse(registroRepository.getById(ID))
        }
    }

    fun deleteRegistros(IDs: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            IDs.forEach {
                registroRepository.delete(it)
            }
        }
        HomeActivity.loadNewInformations()
    }

    fun deleteCategorias(IDs: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            IDs.forEach {
                categoriaRepository.delete(it)
            }
        }
        HomeActivity.loadNewInformations()
    }
    fun LoadRegisterView(
        IdRegeistro: Int,
        onValue: (String) -> Unit,
        onDescripton: (String) -> Unit,
        onCategoria: (CategoriaViewModel) -> Unit
    ){
        getRegistro(IdRegeistro){
            if (it == null)
                return@getRegistro
            onValue(it.Value.toString())
            onDescripton(it.Description)
            getCategoria(if (it.CategoriaFk == 0) 1 else it.CategoriaFk){
                if(it != null)
                    onCategoria(it.toView())
            }
        }
    }

    fun loadRegister(
        IdRegister: Int,
        onValue: (String) -> Unit,
        onDescription: (String) -> Unit,
        onCategoria: (String, Color) -> Unit
    ) {
        getRegistro(IdRegister){
            if(it == null)
                return@getRegistro
            onValue(it.Value.toMonetaryString())
            onDescription(it.Description)
            if(it.CategoriaFk != 0){
                getCategoria(it.CategoriaFk){
                    if(it != null)
                        onCategoria(it.Name,Color(it.Color))
                }
            }
        }
    }

    fun getCategoriaInforms(onResponse:(List<Triple<String,Int,Color>>)->Unit) {
        viewModelScope.launch(Dispatchers.IO){
            var categorias = categoriaRepository.getAll()
            var listResponse:List<Triple<String,Int,Color>> = mutableListOf()
            for ( it in categorias){
                var value = registroRepository.getRegistrosByCategoriaId(it.Id)
                listResponse += Triple(it.Name,value,Color(it.Color))
            }
            onResponse( listResponse )
        }
    }

    fun loadCategoria(IdCategoria: Int, onName: (String)->Unit, onDescription: (String) -> Unit, onColor: (Color)->Unit) {
        getCategoria(IdCategoria){
            if(it != null){
                onName(it.Name)
                onDescription(it.Description)
                onColor(Color(it.Color))
            }
        }
    }

    fun updateCategoria(view: CategoriaViewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            categoriaRepository.getById(view.Id).apply {
                if (this == null)
                    return@apply
                val categoria = this
                categoria.Description = view.Description
                categoria.Name = view.Name
                categoria.Color = view.Color
                viewModelScope.launch(Dispatchers.IO) {
                    categoriaRepository.insert(
                        categoria
                    )
                }
            }
        }
        HomeActivity.loadNewInformations()
    }
}