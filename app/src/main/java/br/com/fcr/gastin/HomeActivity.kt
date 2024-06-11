package br.com.fcr.gastin

//import br.com.fcr.gastin.data.notification.NotificationReceiver
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fcr.gastin.data.database.model.Categoria
import br.com.fcr.gastin.data.notification.NotificationReceiver
import br.com.fcr.gastin.data.sharedPreferences
import br.com.fcr.gastin.ui.common.Constants
import br.com.fcr.gastin.ui.page.HelpScreen
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
import java.util.Calendar

class HomeActivity : ComponentActivity() {
    companion object {
        var CategoriaDefault: CategoriaViewModel = EmptyCategoriaViewModel()
        var CHANNEL_ID = "channel_notification_Gastin_ID_2023"
        val NOTIFICATION_ID = 1
    }

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val openNewRegister = intent.extras?.getBoolean(Constants.OPEN_REGISTRO, false)
        var isDarkTheme by sharedPreferences(Constants.IS_DARKTHEM, false)
        Constants.IsDarkTheme = isDarkTheme
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
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        modifier = Modifier.systemBarsPadding(),
                        navController = navController,
                        startDestination = Route.HOME
                    ) {
                        composable(Route.HOME) {
                            HomeScreenPage(
                                isDarkTheme = isDarkTheme,
                                openNewRegister = openNewRegister,
                                navController = navController,
                                valorDespesas = valorDespesas ?: 0,
                                valorReceitas = valorReceitas ?: 0,
                                valorDespesasBusca = valorDespesasBusca ?: 0,
                                valorReceitasBusca = valorReceitasBusca ?: 0,
                                graphicInforms = graphicInforms,
                                textMes = stringMonth,
                                stringYear = stringYear.toString(),
                                onMonthBefore = { homeViewModel.onEvent(RegisterEvent.before(1)) },
                                onMonthNext = { homeViewModel.onEvent(RegisterEvent.next(1)) },
                                onWeekBefore = { homeViewModel.onEvent(RegisterEvent.beforeWeek(1)) },
                                onWeekNext = { homeViewModel.onEvent(RegisterEvent.nextWeek(1)) },
                                onSwitchTheme = {
                                    if (it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                    isDarkTheme = it
                                    Constants.IsDarkTheme = isDarkTheme
                                },
                                onNewRegister = { isDespesa, item ->
                                    homeViewModel.onEvent(
                                        RegisterEvent.insert(
                                            item.toModel(
                                                isDespesa
                                            )
                                        )
                                    )
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
                                },
                                onDateUpdate = {
                                    homeViewModel.onDateUpdate()
                                }
                            )
                        }
                        composable(Route.LISTA_DESPESAS) {
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_despesas),
                                listItem = listDespesas.map { it.toView() },
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onLoadRegister = { IdRegeistro, onResult ->
                                    homeViewModel.onEvent(RegisterEvent.get(IdRegeistro) {
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
                                    homeViewModel.onEvent(RegisterEvent.update(true, it.toModel()))
                                },
                                onLoadCategory = { id, onResult ->
                                    homeViewModel.onEvent(CategoriaEvent.get(id) {
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                },
                                onDateUpdate = {
                                    homeViewModel.onDateUpdate()
                                }
                            )
                        }
                        composable(Route.LISTA_RESEITAS) {
                            ListValuesScreenPage(
                                navController = navController,
                                title = getString(R.string.txt_receitas),
                                listItem = listReceita,
                                Categorias = listCategoria,
                                CategoriaDefault = CategoriaDefault,
                                onLoadRegister = { IdRegeistro, onResult ->
                                    homeViewModel.onEvent(RegisterEvent.get(IdRegeistro) {
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
                                    homeViewModel.onEvent(RegisterEvent.update(false, it.toModel()))
                                },
                                onLoadCategory = { id, onResult ->
                                    homeViewModel.onEvent(CategoriaEvent.get(id) {
                                        it?.let {
                                            onResult(it.toView())
                                        }
                                    })
                                },
                                onDateUpdate = {
                                    homeViewModel.onDateUpdate()
                                }
                            )
                        }
                        composable(Route.LISTA_CATEGORIAS) {
                            ListCategoriasPage(
                                navController = navController,
                                listItem = listCategoria.filter { it.Id != 1 },
                                onDeleteCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.deleteAll(it))
                                },
                                onNewCategoria = {
                                    homeViewModel.onEvent(CategoriaEvent.insert(it))
                                },
                                onLoadCategoria = { IdRegeistro, onResult ->
                                    homeViewModel.onEvent(CategoriaEvent.get(IdRegeistro) {
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
                        composable(Route.HELP_SCREEN) {
                            HelpScreen()
                        }
                    }
                }
            }
        }
        homeViewModel.onEvent(CategoriaEvent.get(1) {
            it?.let {
                CategoriaDefault = it.toView()
            }
        })
        setCategoryDefault(homeViewModel)

        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent == null){
            createNotificationChannel()
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (Calendar.getInstance().after(calendar)) {
            calendar.set(Calendar.DAY_OF_YEAR, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Gatin_Pro"
            val descriptionText = "canal de notificacao Gastin"
            val important = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(CHANNEL_ID, channelName, important).apply {
                    description = descriptionText
                }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setCategoryDefault(homeViewModel: HomeViewModel) {
        homeViewModel.onEvent(CategoriaEvent.get(1) {
            if (it == null) {
                homeViewModel.onEvent(
                    CategoriaEvent.insert(
                        Categoria(
                            Name = getString(R.string.txt_indefinido),
                            Color = 0xFFD4D4D4
                        )
                    )
                )
            }
        })
    }
}