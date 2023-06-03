package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.data.model.Categoria
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HomeScreenPage
import br.com.fcr.gastin.ui.page.ListCategoriasPage
import br.com.fcr.gastin.ui.page.ListValuesScreenPage
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat

class HomeActivity : ComponentActivity() {
    companion object{
        var CategoriaDefault:CategoriaViewModel = EmptyCategoriaViewModel()
    }
    @SuppressLint("InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository,
            this
        )
        Constants.IsDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        setCategoriaDefault(homeViewModel)
        homeViewModel.onEvent(CategoriaEvent.get(1){
            it?.let {
                CategoriaDefault = it.toView()
            }
        })
        setContent {
            //TODO: listas
            val listDespesas by homeViewModel.despesas.collectAsState()
            val listReceita by homeViewModel.receitas.collectAsState()
            val listCategoria by homeViewModel.categorias.collectAsState()
            val categoriasInforms by homeViewModel.categoriasInforms.collectAsState()
            //TODO: variaveis
            val valorDespesas by homeViewModel.valorDespesas.collectAsState()
            val valorReceitas by homeViewModel.valorReceitas.collectAsState()
            val valorDespesasBusca by homeViewModel.valorDespesasBusca.collectAsState()
            val valorReceitasBusca by homeViewModel.valorReceitasBusca.collectAsState()
            val stringMonth by homeViewModel.stringMonthResourceId.collectAsState()
            val stringYear by homeViewModel.stringYear.collectAsState()
            val graphicInforms by homeViewModel.graphicInforms.collectAsState()
            GastinTheme(Constants.IsDarkTheme) {//Gestao de gasto
                val statusBarHeigth = with(LocalDensity.current){
                    val resourceId = resources.getIdentifier("status_bar_height","dimen","android")
                    val heightPixels = resources.getDimensionPixelSize(resourceId)
                    heightPixels.toDp()
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(modifier = Modifier.padding(top = statusBarHeigth),navController = navController, startDestination = Route.HOME){
                        composable(Route.HOME){
                            HomeScreenPage(
                                navController = navController,
                                valorDespesas = valorDespesas?:0,
                                valorReceitas = valorReceitas?:0,
                                valorDespesasBusca = valorDespesasBusca?:0,
                                valorReceitasBusca = valorReceitasBusca?:0,
                                graphicInforms = graphicInforms,
                                textMes = stringMonth,
                                stringYear = stringYear.toString(),
                                onMonthBefore = { homeViewModel.onEvent(RegisterEvent.before(1)) },
                                onMonthNext = { homeViewModel.onEvent(RegisterEvent.next(1)) },
                                onSwitchTheme = {
                                    if(it)
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                    else
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                    Constants.IsDarkTheme = it
                                },
                                onNewRegister = {isDespesa,item->
                                    homeViewModel.onEvent(RegisterEvent.insert(item.toModel(isDespesa)))
                                },
                                onNewCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.insert(it))
                                },
                                categoriasInforms = categoriasInforms,
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onInformsTotal = { homeViewModel.onEvent(CategoriaEvent.setInformsTotal(it))}
                            )
                        }
                        composable(Route.LISTA_DESPESAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_despesas),
                                listItem = listDespesas.map { it.toView() },
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onLoadRegister = { IdRegeistro,onResult->
                                     homeViewModel.onEvent(RegisterEvent.get(IdRegeistro){
                                         it?.let {
                                             onResult(it.toView())
                                         }
                                    })
                                },
                                onNewRegister = {
                                    homeViewModel.onEvent(RegisterEvent.insert(it.toModel()))
                                },
                                onDeleteRegister = {
                                    homeViewModel.onEvent(RegisterEvent.deleteAll(it))
                                },
                                onUpdateRegister = {
                                    homeViewModel.onEvent(RegisterEvent.update(true,it.toModel()))
                                },
                                onLoadCategory = {id,onResult ->
                                    homeViewModel.onEvent(CategoriaEvent.get(id){
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                }
                            )
                        }
                        composable(Route.LISTA_RESEITAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_receitas),
                                listItem = listReceita,
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onLoadRegister = { IdRegeistro,onResult->
                                    homeViewModel.onEvent(RegisterEvent.get(IdRegeistro){
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                },
                                onNewRegister = {
                                    homeViewModel.onEvent(RegisterEvent.insert(it.toModel(false)))
                                },
                                onDeleteRegister = {
                                    homeViewModel.onEvent(RegisterEvent.deleteAll(it))
                                },
                                onUpdateRegister = {
                                    homeViewModel.onEvent(RegisterEvent.update(false,it.toModel()))
                                },
                                onLoadCategory = {id,onResult ->
                                    homeViewModel.onEvent(CategoriaEvent.get(id){
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                }
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS){
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listCategoria.filter { it.Id != 1 },
                                onDeleteCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.deleteAll(it))
                                },
                                onNewCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.insert(it))
                                },
                                onLoadCategoria = {IdRegeistro,onResult->
                                    homeViewModel.onEvent(CategoriaEvent.get(IdRegeistro){
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                },
                                onUpdateCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.update(it.toModel()))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    fun setCategoriaDefault(homeViewModel:HomeViewModel){
        homeViewModel.onEvent(CategoriaEvent.get(1){
            if(it == null){
                homeViewModel.onEvent(CategoriaEvent.insert(
                    Categoria(
                        Name = getString(R.string.txt_indefinido),
                        Color = 0xFFD4D4D4
                    )
                ))
            }
        })
    }
}