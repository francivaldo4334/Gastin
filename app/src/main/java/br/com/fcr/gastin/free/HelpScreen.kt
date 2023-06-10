package br.com.fcr.gastin.free

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.free.R

@Composable
fun HelpScreen (){
    Column(modifier = Modifier.fillMaxSize(),) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 16.dp),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = stringResource(id = R.string.txt_como_funciona).replace("?", ""),
                fontSize = 18.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.txt_help_user).replace("\\\"","\"").replace("\n","\n\n"),
                fontSize = 14.sp
            )
        }
    }
}