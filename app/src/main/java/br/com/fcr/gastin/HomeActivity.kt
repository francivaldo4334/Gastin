package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import br.com.fcr.gastin.data.model.Registro
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HomeScreenPage
import br.com.fcr.gastin.ui.page.ListCategoriasPage
import br.com.fcr.gastin.ui.page.ListValuesScreenPage
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyRegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route

class HomeActivity : ComponentActivity() {
    companion object{
        var CategoriaDefault:CategoriaViewModel = EmptyCategoriaViewModel()
    }
    @SuppressLint("InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository
        )
        Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
        setCategoriaDefault(homeViewModel)
        setContent {
            //TODO: listas
            val listDespesas by homeViewModel.despesas.collectAsState()
            val listReceita by homeViewModel.receitas.collectAsState()
            val listCategoria by homeViewModel.categorias.collectAsState()
            val categoriasInforms by homeViewModel.categoriasInforms.collectAsState()
            //TODO: variaveis
            val valorDespesas by homeViewModel.valorDespesas.collectAsState()
            val valorReceitas by homeViewModel.valorReceitas.collectAsState()
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
                                onMonthBefore = { /*TODO*/ },
                                onMonthNext = {/*TODO*/},
                                onSwitchTheme = {
                                    editor.putBoolean(Constants.IS_DARKTHEM, it)
                                    editor.apply()
                                    Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
                                },
                                onNewRegister = {isDespesa,item->
                                    homeViewModel.onEvent(RegisterEvent.update(isDespesa,item.toModel()))
                                },
                                onNewCategoria = {

                                },
                                categoriasInforms = categoriasInforms,
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault
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
                                        onResult(it.toView())
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
                                        onResult(it.toView())
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
                                }
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS){
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listCategoria,
                                onDeleteCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.deleteAll(it))
                                },
                                onNewCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.insert(it))
                                },
                                onLoadCategoria = {IdRegeistro,onResult->
                                    homeViewModel.onEvent(CategoriaEvent.get(IdRegeistro){
                                        it?.let { it1 -> onResult(it1.toView()) }
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