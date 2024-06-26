package br.com.fcr.gastin.ui.page.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun BoxDropUpContent (enable:Boolean, onDismiss:()->Unit,content:@Composable BoxScope.()->Unit){

    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = enable

    if (expandedStates.currentState || expandedStates.targetState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(false, onClick = {})
                    .background(Color.Black.copy(0.7f))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom
            ) {
                if(enable) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onDismiss()
                                }
                            )
                        })
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                        .background(MaterialTheme.colorScheme.background),
                    content = content
                )
            }
    }
}