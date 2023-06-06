package br.com.fcr.gastin

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.notification.NotificationReceiver
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HomeScreenPage
import br.com.fcr.gastin.ui.page.ListCategoriasPage
import br.com.fcr.gastin.ui.page.ListValuesScreenPage
import br.com.fcr.gastin.ui.page.SplashScreenPage
import br.com.fcr.gastin.ui.page.viewmodels.CategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.EmptyCategoriaViewModel
import br.com.fcr.gastin.ui.page.viewmodels.toModel
import br.com.fcr.gastin.ui.page.viewmodels.toView
import br.com.fcr.gastin.ui.theme.GastinTheme
import br.com.fcr.gastin.ui.utils.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeActivity : ComponentActivity() {
    companion object{
        var CategoriaDefault:CategoriaViewModel = EmptyCategoriaViewModel()
        var CHANNEL_ID = "channel_notification_Gastin_ID"
    }
    private lateinit var navController:NavHostController
    fun scheduleNotification(){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar21 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,21)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }
        val intent21 = Intent(applicationContext, NotificationReceiver::class.java)
        val pendingIntent21 = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent21,
            PendingIntent.FLAG_IMMUTABLE or  PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar21.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent21
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val name = "canal Gastin"
        val desc = "canal de notificacao Gastin"
        val important = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(HomeActivity.CHANNEL_ID,name,important)
        channel.description = desc
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
    @SuppressLint("InternalInsetResource", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository,
            this
        )
        val sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Constants.IsDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARKTHEM,false)
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
            navController = rememberNavController()
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
                    NavHost(modifier = Modifier.padding(top = statusBarHeigth),navController = navController, startDestination = Route.SPLASH_SCREEN){
                        composable(Route.SPLASH_SCREEN){
                            SplashScreenPage(Constants.IsDarkTheme)
                        }
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
                                onWeekBefore = { homeViewModel.onEvent(RegisterEvent.beforeWeek(1)) },
                                onWeekNext = { homeViewModel.onEvent(RegisterEvent.nextWeek(1)) },
                                onSwitchTheme = {
                                    if(it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                    editor.putBoolean(Constants.IS_DARKTHEM, it)
                                    editor.apply()
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
                                onInformsTotal = {
                                    homeViewModel.onEvent(CategoriaEvent.setInformsTotal(it))
                                    Constants.IsTotalPeriod = it
                                }
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
        lifecycleScope.launch(Dispatchers.Main){
            delay(3000)
            navController.navigate(Route.HOME){popUpTo(0)}
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            if(Constants.IsDarkTheme) {
                scheduleNotification()
                editor.putBoolean(Constants.IS_FIRST_TIME,false)
                editor.apply()
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