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
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route
import br.com.fcr.gastin.ui.utils.Tetra

class HomeActivity : ComponentActivity() {
    companion object {
        lateinit var homeViewModel: HomeViewModel
    }
    @SuppressLint("InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
        homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository
        )
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
        var listDespesas:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listReceita:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listRegistro:List<RegistroViewModel> by mutableStateOf(emptyList())
        var listCategoria:List<CategoriaViewModel> by mutableStateOf(emptyList())
        homeViewModel.getDespesas().observe(this){
            listDespesas = it.map { it.toView() }
        }
        homeViewModel.getReceitas().observe(this){
            listReceita = it.map { it.toView() }
        }
        homeViewModel.getRegistros().observe(this){
            listRegistro = it.map { it.toView() }
        }
        homeViewModel.getCategorias().observe(this){
            listCategoria = it.map { it.toView() }
        }
        setContent {
            GastinTheme(Constants.IsDarkTheme) {//Gestao de gasto
                // A surface container using the 'background' color from the theme
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
                                onMonthBefore = { /*TODO*/ },
                                onMonthNext = {/*TODO*/},
                                onSwitchTheme = {
                                    editor.putBoolean(Constants.IS_DARKTHEM, it)
                                    editor.apply()
                                    Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
                                },
                                onNewRegister = {/*b,it-> homeViewModel.setRegister(b,it) */},
//                                categorias = emptyList(),
//                                registros = listRegistro
                            )
                        }
                        composable(Route.LISTA_DESPESAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_despesas),
                                listItem = listDespesas,
                                onNewRegister = { homeViewModel.setDespesa(it) },
                                Categorias = listCategoria
                            )
                        }
                        composable(Route.LISTA_RESEITAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_receitas),
                                listItem = listReceita,
                                onNewRegister = { homeViewModel.setReceita(it) },
                                Categorias = listCategoria
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS){
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listCategoria.map { Tetra(it.Name,it.Description,it.Color,it.Id) }.filter { it.tetra != 1 }
                            )
                        }
                    }
                }
            }
        }
    }
}