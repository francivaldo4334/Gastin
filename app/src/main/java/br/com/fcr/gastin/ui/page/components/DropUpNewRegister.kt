package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.ui.utils.Tetra

@Composable
fun DropUpNewRegister (enable:Boolean,onDismiss:()->Unit,onActionsResult:(Tetra<Boolean,Int,String,Int>)->Unit){
    val navController = rememberNavController()
    val heightScreen = LocalConfiguration.current.screenHeightDp/2
    val SELECT_TYPE_REGISTER = "SELECT_TYPE_REGISTER"
    val INSERT_VALUES_REGISTER = "INSERT_VALUES_REGISTER"
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightScreen.dp)
                .padding(16.dp)
        ) {
            NavHost(
                navController = navController,
                startDestination = SELECT_TYPE_REGISTER
            ){
                composable(SELECT_TYPE_REGISTER){

                }
                composable(INSERT_VALUES_REGISTER){

                }
            }
        }
    }
}