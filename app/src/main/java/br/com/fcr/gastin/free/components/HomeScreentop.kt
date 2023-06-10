package br.com.fcr.gastin.free.ui.page.components

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import br.com.fcr.gastin.free.R

@Composable
fun HomeScreentop(textMes:String,stringYear:String,onMonthBefore:()->Unit, onMonthNext:()->Unit, onOptions:()->Unit, content:@Composable ()->Unit){
    Column() {
        Box(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMonthBefore) {
                    Icon(painter = painterResource(id = R.drawable.ic_left), contentDescription = "")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(132.dp)
                ) {
                    Text(text = textMes, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = stringYear, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp))
                }

                IconButton(onClick = onMonthNext) {
                    Icon(painter = painterResource(id = R.drawable.ic_right), contentDescription = "")
                }
            }
            Column{
                IconButton(onClick = onOptions) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_options_chorts),
                        contentDescription = ""
                    )
                }
                content()
            }
        }
    }
}