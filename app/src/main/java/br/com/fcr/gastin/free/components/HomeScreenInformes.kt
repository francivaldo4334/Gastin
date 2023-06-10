package br.com.fcr.gastin.free.ui.page.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import br.com.fcr.gastin.free.R
import br.com.fcr.gastin.free.ui.utils.toMonetaryString

@Composable
fun HomeScreenInformes(valorInit:Int, valorSaldo:Int, valorPrevisto:Int){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.txt_recebido), fontSize = 12.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
            Text(text = valorInit.toMonetaryString(), fontSize = 14.sp,color = MaterialTheme.colors.onBackground.copy(0.5f))
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.txt_saldo), fontSize = 14.sp)
            Text(text = valorSaldo.toMonetaryString(), fontSize = 18.sp)
        }
    }
}