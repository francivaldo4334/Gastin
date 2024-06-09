package br.com.fcr.gastin.ui.widget

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import br.com.fcr.gastin.HomeViewModel
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.toMonetaryString

class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val homeViewModel = HomeViewModel(context.applicationContext as Application)
        provideContent {
            val valorDespesas by homeViewModel.valorDespesas.collectAsState()
            val valorReceitas by homeViewModel.valorReceitas.collectAsState()

            @Composable
            @GlanceComposable
            fun BoxContent(
                modifier: GlanceModifier = GlanceModifier.fillMaxWidth(),
                enablePadding: Boolean = true,
                content: @Composable @GlanceComposable () -> Unit
            ) {
                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(if (enablePadding) 16.dp else 0.dp)
                ) {
                    content()
                }
            }

            @Composable
            @GlanceComposable
            fun item(isDespesas: Boolean, value: Int, onClick: () -> Unit) {
                Row(
                    GlanceModifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                        .padding(vertical = 16.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = GlanceModifier
                                .width(24.dp)
                                .height(24.dp)
                                .background(
                                    if (isDespesas)
                                        Color.Red.copy(0.5f)
                                    else
                                        Color.Green.copy(0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (isDespesas)
                                        R.drawable.ic_minus
                                    else
                                        R.drawable.ic_add
                                ),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                        Spacer(modifier = GlanceModifier.width(16.dp).height(16.dp))
                        Text(
                            text =
                            if (isDespesas)
                                stringResource(id = R.string.txt_despesas)
                            else
                                stringResource(id = R.string.txt_receitas),
                            style = TextDefaults.defaultTextStyle.copy(fontSize = 14.sp),
                        )
                    }
                    Text(
                        text = value.toMonetaryString(),
                        style = TextDefaults.defaultTextStyle.copy(fontSize = 14.sp),
                    )
                }
            }
            // create your AppWidget here
            BoxContent(
                enablePadding = false,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Column(GlanceModifier.fillMaxWidth()) {
                    androidx.compose.material3.Text(
                        text = stringResource(R.string.txt_visao_geral),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                    Spacer(modifier = GlanceModifier.width(16.dp))
                    item(isDespesas = true, value = valorDespesas?:0) {}
                    item(isDespesas = false, value = valorReceitas?:0) {}
                }
            }
        }
    }
}