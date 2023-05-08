package br.com.fcr.gastin.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import br.com.fcr.gastin.R

@Composable
fun SplashScreenPage(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Icon(painter = painterResource(id = R.drawable.logo_fcrp), contentDescription = "")
    }
}