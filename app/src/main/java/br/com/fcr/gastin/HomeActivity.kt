package br.com.fcr.gastin

import android.content.res.Resources.Theme
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HomeScreenPage
import br.com.fcr.gastin.ui.page.ListValuesScreenPage
import br.com.fcr.gastin.ui.page.SplashScreenPage
import br.com.fcr.gastin.ui.page.components.dropDownMoreOptions
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route
import br.com.fcr.gastin.ui.utils.Tetra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Constants.IsDarkTheme = sharedPreferences.getBoolean("theme",false)
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
                            HomeScreenPage(navController,onMonthBefore = { /*TODO*/ }, onMonthNext = {/*TODO*/}){
                                editor.putBoolean("theme", it)
                                editor.apply()
                                Constants.IsDarkTheme = sharedPreferences.getBoolean("theme",false)
                            }
                        }
                        composable(Route.LISTA_DESPESAS){
                            ListValuesScreenPage(
                                navController = navController,
                                title = "Despesas",
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
                                title = "Receitas",
                                listItem = listOf(
                                    Tetra("descricao","01/02/2023",1000,0),
                                    Tetra("descricao","01/02/2023",1000,1),
                                    Tetra("descricao","01/02/2023",1000,2),
                                    Tetra("descricao","01/02/2023",1000,3),
                                    Tetra("descricao","01/02/2023",1000,4)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}