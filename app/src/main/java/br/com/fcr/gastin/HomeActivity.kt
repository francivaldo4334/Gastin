package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route

class HomeActivity : ComponentActivity() {
    @SuppressLint("InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
        val homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository
        )
        setCategoriaDefault(homeViewModel)
        //TODO: listas
        var listDespesas:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listReceita:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listRegistro:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listCategoria:List<CategoriaViewModel> by mutableStateOf(emptyList())
        //TODO: variaveis
        var valorDespesas:Int by mutableStateOf(0)
        var valorReceitas:Int by mutableStateOf(0)
        var CategoriaDefault:CategoriaViewModel = EmptyCategoriaViewModel()
        //TODO: viewModel
        homeViewModel.getDespesas().observe(this){ listDespesas = it.map { it.toView() } }
        homeViewModel.getReceitas().observe(this){ listReceita = it.map { it.toView() } }
        homeViewModel.getRegistros().observe(this){ listRegistro = it.map { it.toView() } }
        homeViewModel.getCategorias().observe(this){ listCategoria = it.map { it.toView() } }
        homeViewModel.getDespesas().observe(this){ valorDespesas = it.sumOf { it.Value } }
        homeViewModel.getReceitas().observe(this){ valorReceitas = it.sumOf { it.Value } }
        homeViewModel.getCategoria(1).observe(this){ CategoriaDefault = it.toView() }
        setContent {
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
                                valorDespesas = valorDespesas,
                                valorReceitas = valorReceitas,
                                onMonthBefore = { /*TODO*/ },
                                onMonthNext = {/*TODO*/},
                                onSwitchTheme = {
                                    editor.putBoolean(Constants.IS_DARKTHEM, it)
                                    editor.apply()
                                    Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
                                },
                                onNewRegister = {},
                            )
                        }
                        composable(Route.LISTA_DESPESAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_despesas),
                                listItem = listDespesas,
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onLoadRegister = { IdRegister,onValue,onDescription,onCategoria->
                                    homeViewModel.loadListRegister(this@HomeActivity,IdRegister,onValue,onDescription,onCategoria)
                                },
                                onLoadCategoria = { IdRegeistro,onValue,onDescripton,onCategoria->
                                    homeViewModel.loadListCategoria(this@HomeActivity,IdRegeistro,onValue,onDescripton,onCategoria)
                                },
                                onNewRegister = {
                                    homeViewModel.setDespesa(it)
                                },
                                onDeleteRegister = {
                                    homeViewModel.deleteRegistros(it)
                                },
                                onActionsResult = {
                                    homeViewModel.updateRegister(it,this@HomeActivity)
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
                                onLoadRegister = { IdRegister,onValue,onDescription,onCategoria->
                                    homeViewModel.loadListRegister(this@HomeActivity,IdRegister,onValue,onDescription,onCategoria)
                                },
                                onLoadCategoria = { IdRegeistro,onValue,onDescripton,onCategoria->
                                    homeViewModel.loadListCategoria(this@HomeActivity,IdRegeistro,onValue,onDescripton,onCategoria)
                                },
                                onNewRegister = {
                                    homeViewModel.setReceita(it)
                                },
                                onDeleteRegister = {
                                    homeViewModel.deleteRegistros(it)
                                },
                                onActionsResult = {
                                    homeViewModel.updateRegister(it,this@HomeActivity)
                                }
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS){
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listCategoria.filter { it.Id != 1 },
                                onDeleteCategoria = {
                                    homeViewModel.deleteCategorias(it)
                                },
                                onNewCategoria = {
                                    homeViewModel.setCategoria(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    fun setCategoriaDefault(homeViewModel:HomeViewModel){
        homeViewModel.getCategoria(1).observe(this){
            if(it == null){
                homeViewModel.setCategoria(
                    Categoria(
                        Name = "Default",
                        Color = 0xFFD4D4D4
                    )
                )
            }
        }
    }
}