package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BoxContent(modifier: Modifier = Modifier.fillMaxSize(),enablePadding:Boolean = true,content:@Composable ()->Unit){
    Box(modifier = modifier
        .shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            clip = true
        )
        .background(MaterialTheme.colors.surface)
        .border(1.dp, MaterialTheme.colors.onBackground.copy(0.1f), RoundedCornerShape(16.dp))
        .padding(if (enablePadding) 16.dp else 0.dp)
    ){
        content()
    }
}