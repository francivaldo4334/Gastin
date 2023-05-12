package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HomeScreenPage
import br.com.fcr.gastin.ui.page.ListCategoriasPage
import br.com.fcr.gastin.ui.page.ListValuesScreenPage
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route
import br.com.fcr.gastin.ui.utils.Tetra

class HomeActivity : ComponentActivity() {
    @SuppressLint("InternalInsetResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
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
                                onNewRegister = {

                                }
                            )
                        }
                        composable(Route.LISTA_DESPESAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_despesas),
                                listItem = listOf(
                                    Tetra("descricao","01/02/2023",1000,0),
                                    Tetra("descricao","01/02/2023",1000,1),
                                    Tetra("descricao","01/02/2023",1000,2),
                                    Tetra("descricao","01/02/2023",1000,3),
                                    Tetra("descricao","01/02/2023",1000,4)
                                )
                            )
                        }
                        composable(Route.LISTA_RESEITAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_receitas),
                                listItem = listOf(
                                    Tetra("descricao","01/02/2023",1000,0),
                                    Tetra("descricao","01/02/2023",1000,1),
                                    Tetra("descricao","01/02/2023",1000,2),
                                    Tetra("descricao","01/02/2023",1000,3),
                                    Tetra("descricao","01/02/2023",1000,4)
                                )
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS){
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listOf(
                                    Tetra("Titulo","descricao",(0xffF00FFF).toInt(),0),
                                    Tetra("Titulo","descricao",(0xffFFFFFF).toInt(),1),
                                    Tetra("Titulo","descricao",(0xff00FFFF).toInt(),2),
                                    Tetra("Titulo","descricao",(0xff00FF0F).toInt(),3),
                                    Tetra("Titulo","descricao",(0xffFFF00F).toInt(),4)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}