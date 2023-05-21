package br.com.fcr.gastin

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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
    fun getCategoria(ID:Int):LiveData<Categoria>{
        return categoriaRepository.getById(ID)
    }
    fun setCategoria(it:Categoria){
        viewModelScope.launch(Dispatchers.IO) {
            categoriaRepository.insert(it)
        }
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
    }
    fun setRegister(isDespesa:Boolean,it:RegistroViewModel){
        viewModelScope.launch(Dispatchers.IO){
            registroRepository.insert(
                Registro(
                    IsDespesa = isDespesa,
                    Description = it.Description,
                    Value = it.Value,
                    CategoriaFk = if(it.CategoriaFk==null || it.CategoriaFk == 0) 1 else it.CategoriaFk!!
                )
            )
        }
    }
    fun updateRegister(view:RegistroViewModel,owner: LifecycleOwner){
        getRegistro(view.Id).observe(owner){
            if(it == null)
                return@observe
            val registro = it
            registro.Description = view.Description
            registro.Value = view.Value
            registro.CategoriaFk = if(view.CategoriaFk == null||view.CategoriaFk == 0)1 else view.CategoriaFk
            viewModelScope.launch (Dispatchers.IO){
                registroRepository.insert(
                    registro
                )
            }
        }
    }

    fun getCategorias():LiveData<List<Categoria>>{
        return categoriaRepository.getAll()
    }

    fun getRegistros(): LiveData<List<Registro>>{
        return registroRepository.getall()
    }
    fun getRegistro(ID: Int): LiveData<Registro>{
        return registroRepository.getById(ID)
    }

    fun deleteRegistros(IDs: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            IDs.forEach {
                registroRepository.delete(it)
            }
        }
    }

    fun deleteCategorias(IDs: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            IDs.forEach {
                categoriaRepository.delete(it)
            }
        }
    }
    fun LoadRegisterView(
        owner: LifecycleOwner,
        IdRegeistro: Int,
        onValue: (String) -> Unit,
        onDescripton: (String) -> Unit,
        onCategoria: (CategoriaViewModel) -> Unit
    ){
        getRegistro(IdRegeistro).observe(owner) {
            if (it == null)
                return@observe
            onValue(it.Value.toString())
            onDescripton(it.Description)
            getCategoria(if (it.CategoriaFk == 0) 1 else it.CategoriaFk).observe(owner) {
                onCategoria(it.toView())
            }
        }
    }

    fun loadRegister(
        owner: LifecycleOwner,
        IdRegister: Int,
        onValue: (String) -> Unit,
        onDescription: (String) -> Unit,
        onCategoria: (String, Color) -> Unit
    ) {
        getRegistro(IdRegister).observe(owner){
            if(it == null)
                return@observe
            onValue(it.Value.toMonetaryString())
            onDescription(it.Description)
            if(it.CategoriaFk != 0){
                getCategoria(it.CategoriaFk).observe(owner){
                    onCategoria(it.Name,Color(it.Color))
                }
            }
        }
    }

    fun getCategoriaInforms(owner: LifecycleOwner,onResponse:(List<Triple<String,Int,Color>>)->Unit) {
        getCategorias().observe(owner){
            viewModelScope.launch(Dispatchers.IO) {
                onResponse(
                    it.map {
                        var value = registroRepository.getRegistrosByCategoriaId(it.Id).sumOf { it.Value }
                        Triple(it.Name,value,Color(it.Color))
                    }
                )
            }
        }
    }

    fun loadCategoria(owner: LifecycleOwner, IdCategoria: Int, onName: (String)->Unit, onDescription: (String) -> Unit, onColor: (Color)->Unit) {
        getCategoria(IdCategoria).observe(owner){
            if(it != null){
                onName(it.Name)
                onDescription(it.Description)
                onColor(Color(it.Color))
            }
        }
    }

    fun updateCategoria(view: CategoriaViewModel, owner: LifecycleOwner) {
        getCategoria(view.Id).observe(owner){
            if(it == null)
                return@observe
            val categoria = it
            categoria.Description = view.Description
            categoria.Name = view.Name
            categoria.Color = view.Color
            viewModelScope.launch (Dispatchers.IO){
                categoriaRepository.insert(
                    categoria
                )
            }
        }
    }
}