package br.com.fcr.gastin

//import br.com.fcr.gastin.data.notification.NotificationReceiver
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeActivity : ComponentActivity() {
    companion object {
        var CategoriaDefault: CategoriaViewModel = EmptyCategoriaViewModel()
        var CHANNEL_ID = "channel_notification_Gastin_ID_2023"
        val NOTIFICATION_ID = 1
    }

    private lateinit var navController: NavHostController

    private fun scheduleNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            clear()
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val intent = Intent(applicationContext, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel = HomeViewModel(
            (applicationContext as MyApplication).categoriaRepository,
            (applicationContext as MyApplication).registroRepository,
            this
        )
        val openNewRegister = intent.extras?.getBoolean(Constants.OPEN_REGISTRO, false)
        var InitRout = Route.SPLASH_SCREEN
        var isSplashScreen = true
        var isDarkTheme by sharedPreferences(Constants.IS_DARKTHEM, false)
        Constants.IsDarkTheme = isDarkTheme
        if (openNewRegister == true)
            InitRout = Route.HOME
        if (openNewRegister == true)
            isSplashScreen = false
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
                val statusBarHeigth = with(LocalDensity.current) {
                    val resourceId =
                        resources.getIdentifier("status_bar_height", "dimen", "android")
                    val heightPixels = resources.getDimensionPixelSize(resourceId)
                    heightPixels.toDp()
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        modifier = Modifier.padding(top = statusBarHeigth),
                        navController = navController,
                        startDestination = InitRout
                    ) {
                        composable(Route.SPLASH_SCREEN) {
                            SplashScreenPage(isDarkTheme)
                        }
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
            if (isSplashScreen) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    navController.navigate(Route.HOME) { popUpTo(0) }
                    isSplashScreen = false
                }
            }
        }
        homeViewModel.onEvent(CategoriaEvent.get(1) {
            it?.let {
                CategoriaDefault = it.toView()
            }
        })
        var isFirstTime by sharedPreferences(Constants.IS_FIRST_TIME, true)
        if (isFirstTime) {
            lifecycleScope.launch(Dispatchers.IO) {
                createNotificationChannel()
                setCategoryDefault(homeViewModel)
                scheduleNotification()
                isFirstTime = false
            }
        }
    }
}