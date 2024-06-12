package br.com.fcr.gastin.ui.widget

import android.app.Application
import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceComposable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import br.com.fcr.gastin.HomeViewModel
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.toMonetaryString

object MyAppWidget : GlanceAppWidget() {

    val countKey = intPreferencesKey("count")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val count = currentState(key = countKey) ?: 0
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Text(
                    text = "0",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White),
                        fontSize = 26.sp
                    )
                )
                Button(
                    text = "Inc",
                    onClick = {
                        actionRunCallback(IncrementActionCallback::class.java)
                    },
                )
            }
        }
    }
}

class SimpleCounterWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = MyAppWidget
}

class IncrementActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context,glanceId) { prefs ->
            val currentCount = prefs[MyAppWidget.countKey]
            if (currentCount != null) {
                prefs[MyAppWidget.countKey] = currentCount + 1
            } else {
                prefs[MyAppWidget.countKey] = 1
            }
        }
        MyAppWidget.update(context,glanceId)
    }
}