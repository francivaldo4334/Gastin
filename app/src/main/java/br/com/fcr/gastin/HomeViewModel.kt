package br.com.fcr.gastin

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.data.repository.ICategoriaRepository
import br.com.fcr.gastin.data.repository.IRegistroRepository
import br.com.fcr.gastin.ui.page.viewmodels.toView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CategoriaEvent{
    data class delete(val id:Int):CategoriaEvent
    data class deleteAll(val ids:List<Int>):CategoriaEvent
    data class update(val categoria: Categoria):CategoriaEvent
    data class get(val id:Int,val onResult:(Categoria?)->Unit):CategoriaEvent
    data class insert(val categoira:Categoria):CategoriaEvent
}
sealed interface RegisterEvent{
    data class delete(val id:Int):RegisterEvent
    data class deleteAll(val ids:List<Int>):RegisterEvent
    data class update(val isDespesa:Boolean,val register: Registro):RegisterEvent
    data class get(val id:Int,val onResult:(Registro?)->Unit):RegisterEvent
    data class insert(val registro:Registro):RegisterEvent
    data class next(val it:Int):RegisterEvent
    data class before(val it:Int):RegisterEvent
}
class HomeViewModel constructor(
    private val categoriaRepository: ICategoriaRepository,
    private val registroRepository: IRegistroRepository,
    private val context:Context
) : ViewModel() {
    //TODO: listas
    val despesas = registroRepository.getAllDespesas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val receitas = registroRepository.getAllReceitas()
        .flatMapLatest { MutableStateFlow(it.map { it.toView() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val categorias =
        categoriaRepository.getAll().flatMapLatest { MutableStateFlow(it.map { it.toView() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    var valorDespesas = registroRepository
        .getAllDespesasValor()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
    var valorReceitas = registroRepository
        .getAllReceitasValor()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
    private val buscaMesAno = MutableStateFlow(Pair(5,2023))
    var valorDespesasBusca = buscaMesAno.flatMapLatest {
        registroRepository.getAllDespesasValorMesAno(it.first,it.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
    var valorReceitasBusca =  buscaMesAno.flatMapLatest {
        registroRepository.getAllReceitasValorMesAno(it.first,it.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
    val categoriasInforms = categoriaRepository.getAllWithTotal().flatMapLatest {
        val list = it.map {
            Triple(
                it.Name,
                it.total,
                Color(it.Color)
            )
        }
        MutableStateFlow(list)
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(),emptyList())
    val stringMonthResourceId = buscaMesAno.flatMapLatest {
        val resp = when(it.first){
            1 -> context.getString(R.string.txt_january)
            2 -> context.getString(R.string.txt_february)
            3 -> context.getString(R.string.txt_march)
            4 -> context.getString(R.string.txt_april)
            5 -> context.getString(R.string.txt_may)
            6 -> context.getString(R.string.txt_june)
            7 -> context.getString(R.string.txt_july)
            8 -> context.getString(R.string.txt_august)
            9 -> context.getString(R.string.txt_september)
            10 -> context.getString(R.string.txt_october)
            11 -> context.getString(R.string.txt_november)
            12 -> context.getString(R.string.txt_december)
            else -> ""
        }
        MutableStateFlow(resp)
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(), "")
    val stringYear = buscaMesAno.flatMapLatest {
        MutableStateFlow(it.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),0)
    fun onEvent(event:RegisterEvent){
        when(event){

            is RegisterEvent.delete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    registroRepository.delete(event.id)
                }
            }
            is RegisterEvent.deleteAll -> {
                viewModelScope.launch(Dispatchers.IO) {
                    registroRepository.deleteAll(event.ids)
                }
            }
            is RegisterEvent.get -> {
                viewModelScope.launch(Dispatchers.IO) {
                    registroRepository.getById(event.id).collectLatest {
                        event.onResult(it)
                    }
                }
            }
            is RegisterEvent.update -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val resgister = event.register
                    resgister.IsDespesa = event.isDespesa
                    registroRepository.update(resgister)
                }
            }
            is RegisterEvent.insert ->{
                viewModelScope.launch(Dispatchers.IO) {
                    registroRepository.insert(event.registro)
                }
            }
            is RegisterEvent.next->{
                var newMes = buscaMesAno.value.first + event.it
                var newAno = buscaMesAno.value.second
                if(newMes > 12){
                    newMes = 1
                    newAno += event.it
                }
                buscaMesAno.value = Pair(
                    newMes,
                    newAno
                )
            }
            is RegisterEvent.before->{
                var newMes = buscaMesAno.value.first - event.it
                var newAno = buscaMesAno.value.second
                if(newMes < 1){
                    newMes = 12
                    newAno -= event.it
                }
                buscaMesAno.value = Pair(
                    newMes,
                    newAno
                )
            }
        }
    }
    fun onEvent(event:CategoriaEvent){
        when(event){
            is CategoriaEvent.get->{
                viewModelScope.launch(Dispatchers.IO) {
                    categoriaRepository.getById(event.id).collectLatest {
                        event.onResult(it)
                    }
                }
            }
            is CategoriaEvent.insert -> {
                viewModelScope.launch(Dispatchers.IO) {
                    categoriaRepository.insert(event.categoira)
                }
            }
            is CategoriaEvent.deleteAll->{
                viewModelScope.launch(Dispatchers.IO){
                    categoriaRepository.deleteAll(event.ids)
                }
            }
            is CategoriaEvent.update->{
                viewModelScope.launch(Dispatchers.IO){
                    categoriaRepository.update(event.categoria)
                }
            }
        }
    }


}