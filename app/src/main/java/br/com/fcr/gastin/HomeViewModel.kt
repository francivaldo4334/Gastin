package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.glance.LocalGlanceId
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.database.model.Registro
import br.com.fcr.gastin.data.database.repository.implementation.CategoriaRepository
import br.com.fcr.gastin.data.database.repository.implementation.RegistroRepository
import br.com.fcr.gastin.data.database.viewmodel.DashboardWeek
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.utils.getDatesOfWeek
import br.com.fcr.gastin.ui.utils.toFlowMonth
import br.com.fcr.gastin.ui.utils.toFlowTriper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

enum class GraphicPeriod {
    WEEK
}

sealed interface CategoriaEvent {
    data class delete(val id: Int) : CategoriaEvent
    data class deleteAll(val ids: List<Int>) : CategoriaEvent
    data class update(val categoria: Categoria) : CategoriaEvent
    data class get(val id: Int, val onResult: (Categoria?) -> Unit) : CategoriaEvent
    data class insert(val categoira: Categoria) : CategoriaEvent
    data class setInformsTotal(val it: Boolean) : CategoriaEvent
}

sealed interface RegisterEvent {
    data class delete(val id: Int) : RegisterEvent
    data class deleteAll(val ids: List<Int>) : RegisterEvent
    data class update(val isDespesa: Boolean, val register: Registro) : RegisterEvent
    data class get(val id: Int, val onResult: (Registro?) -> Unit) : RegisterEvent
    data class insert(val registro: Registro) : RegisterEvent
    data class next(val it: Int) : RegisterEvent
    data class before(val it: Int) : RegisterEvent
    data class nextWeek(val it: Int) : RegisterEvent
    data class beforeWeek(val it: Int) : RegisterEvent
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel constructor(
    application: Application
) : AndroidViewModel(application) {
    private var categoriaRepository: CategoriaRepository = (application as MyApplication).categoriaRepository as CategoriaRepository
    private var registroRepository: RegistroRepository = (application as MyApplication).registroRepository as RegistroRepository
    private val context: Context = application.applicationContext
    

    //OBTEM O MES E ANO ATURAIS
    private var mesAno = Pair(0, 0)
    private var semanaAno = Pair(0, 0)
    private val datenow = Date()
    private val calendar = Calendar.getInstance().apply {
        time = datenow
        Log.d("MONTH", get(Calendar.MONTH).toString())
        mesAno = Pair(get(Calendar.MONTH), get(Calendar.YEAR))
        semanaAno = Pair(get(Calendar.WEEK_OF_YEAR), get(Calendar.YEAR))
    }

    //TODO: PERIOD DASHBOARD GRAPHIC
    private val graphicPeriod = MutableStateFlow(GraphicPeriod.WEEK)

    //TODO: IS TOTAL DASHBOAR
    private val isCategoriasInformsTotal = MutableStateFlow(false)

    //TODO: IS TOTAL REGISTROS
    private val isRegistrosInformsTotal = MutableStateFlow(false)

    //TODO: BUSCA POR SEMANA E ANO
    private val buscaSemanaAno = MutableStateFlow(semanaAno)

    //TODO: BUSCA POR MES E ANO
    private val buscaMesAno = MutableStateFlow(mesAno)

    //TODO: lista DESPESAS TOTAL
    val despesas = isRegistrosInformsTotal.flatMapLatest { isTotal ->
        if (isTotal) registroRepository.getAllDespesas()
        else buscaMesAno.flatMapLatest {
            registroRepository.getAllDespesasMesAno(
                it.first,
                it.second
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //TODO: lista RECEITAS TOTAL
    val receitas = isRegistrosInformsTotal.flatMapLatest { isTotal ->
        if (isTotal) registroRepository.getAllReceitas()
        else buscaMesAno.flatMapLatest {
            registroRepository.getAllReceitasMesAno(
                it.first,
                it.second
            )
        }
    }.flatMapLatest { MutableStateFlow(it.map { it.toView() }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //TODO: lista CATEGORIA
    val categorias =
        categoriaRepository.getAll().flatMapLatest { MutableStateFlow(it.map { it.toView() }) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //TODO: DESPESAS VALOR TOTAL
    var valorDespesas = isRegistrosInformsTotal.flatMapLatest { isTotal ->
        if (isTotal) registroRepository.getAllDespesasValor()
        else buscaMesAno.flatMapLatest {
            registroRepository.getAllDespesasValorMesAno(
                it.first,
                it.second
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //TODO: RECEITA VALOR TOTAL
    var valorReceitas = isRegistrosInformsTotal.flatMapLatest { isTotal ->
        if (isTotal) registroRepository.getAllReceitasValor()
        else buscaMesAno.flatMapLatest {
            registroRepository.getAllReceitasValorMesAno(
                it.first,
                it.second
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //TODO: DESPESAS VALOR TOTAL MES ANO
    var valorDespesasBusca = buscaMesAno.flatMapLatest {
        registroRepository.getAllDespesasValorMesAno(
            it.first,
            it.second
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //TODO: DESPESAS VALOR TOTAL MES ANO
    var valorReceitasBusca = buscaMesAno.flatMapLatest {
        registroRepository.getAllReceitasValorMesAno(
            it.first,
            it.second
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    //TODO: INFOR DASHBOARD
    val categoriasInforms = isCategoriasInformsTotal.flatMapLatest { isTotal ->
        val listCatgoria: Flow<List<Categoria>>
        if (isTotal) listCatgoria = categoriaRepository.getAllWithTotal()
        else listCatgoria =
            buscaMesAno.flatMapLatest { categoriaRepository.getAllWithMesAno(it.first, it.second) }
        listCatgoria.flatMapLatest { it.toFlowTriper() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //TODO: INFOR DASHBOARD GRAPHIC
    val graphicInforms = graphicPeriod.flatMapLatest {
        when (it) {
            GraphicPeriod.WEEK -> {
                buscaSemanaAno.flatMapLatest { busca ->
                    registroRepository.getDasboardWeek(busca.first, busca.second)
                        .flatMapLatest { listWeek ->
                            var resp: MutableList<DashboardWeek> = mutableListOf()
                            getDatesOfWeek(busca.second, busca.first).map {
                                DashboardWeek(
                                    0,
                                    it
                                )
                            }.forEach { itResp ->
                                var item = listWeek.firstOrNull {
                                    it.date.day == itResp.date.day &&
                                            it.date.year == itResp.date.year
                                }
                                if (item == null)
                                    resp.add(itResp)
                                else
                                    resp.add(item)
                            }
                            MutableStateFlow(resp)
                        }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //String month
    val stringMonthResourceId = buscaMesAno.flatMapLatest { it.toFlowMonth(context) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    //String year
    val stringYear = buscaMesAno.flatMapLatest { MutableStateFlow(it.second) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun onEvent(event: RegisterEvent) {
        when (event) {

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

            is RegisterEvent.insert -> {
                viewModelScope.launch(Dispatchers.IO) {
                    registroRepository.insert(event.registro)
                }
            }

            is RegisterEvent.next -> {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.MONTH, buscaMesAno.value.first - 1)
                calendar.set(Calendar.YEAR, buscaMesAno.value.second)
                calendar.add(Calendar.MONTH, event.it)
                buscaMesAno.value = Pair(
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR)
                )
            }

            is RegisterEvent.before -> {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.MONTH, buscaMesAno.value.first - 1)
                calendar.set(Calendar.YEAR, buscaMesAno.value.second)
                calendar.add(Calendar.MONTH, -event.it)
                buscaMesAno.value = Pair(
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR)
                )
            }

            is RegisterEvent.nextWeek -> {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.WEEK_OF_YEAR, buscaSemanaAno.value.first)
                calendar.set(Calendar.YEAR, buscaSemanaAno.value.second)
                calendar.add(Calendar.WEEK_OF_YEAR, event.it)
                buscaSemanaAno.value = Pair(
                    calendar.get(Calendar.WEEK_OF_YEAR),
                    calendar.get(Calendar.YEAR)
                )
            }

            is RegisterEvent.beforeWeek -> {
                val calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(Calendar.WEEK_OF_YEAR, buscaSemanaAno.value.first)
                calendar.set(Calendar.YEAR, buscaSemanaAno.value.second)
                calendar.add(Calendar.WEEK_OF_YEAR, -event.it)
                buscaSemanaAno.value = Pair(
                    calendar.get(Calendar.WEEK_OF_YEAR),
                    calendar.get(Calendar.YEAR)
                )
            }
        }
    }

    fun onEvent(event: CategoriaEvent) {
        when (event) {
            is CategoriaEvent.get -> {
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

            is CategoriaEvent.deleteAll -> {
                viewModelScope.launch(Dispatchers.IO) {
                    categoriaRepository.deleteAll(event.ids)
                }
            }

            is CategoriaEvent.update -> {
                viewModelScope.launch(Dispatchers.IO) {
                    categoriaRepository.update(event.categoria)
                }
            }

            is CategoriaEvent.setInformsTotal -> {
                isCategoriasInformsTotal.value = event.it
            }

            is CategoriaEvent.delete -> {}
        }
    }

    fun onDateUpdate() {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.time = date
        buscaMesAno.value = Pair(
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
        buscaSemanaAno.value = Pair(
            calendar.get(Calendar.WEEK_OF_YEAR),
            calendar.get(Calendar.YEAR)
        )
    }
}