package br.com.fcr.gastin.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fcr.gastin.R

@Composable
fun SplashScreenPage(theme:Boolean){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if(theme)
            Icon(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(56.dp),
            )
        else
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(56.dp),
            )
        Icon(
            painter = painterResource(id = R.drawable.logo_fcrp),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(56.dp)
                .padding(bottom = 16.dp)
        )
    }
}