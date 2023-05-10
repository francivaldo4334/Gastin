package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import br.com.fcr.gastin.ui.utils.toMonetaryString

@Composable
fun HomeScreenVisaoGeral(valorReceitas:Int, valorDespesas:Int, onReceitas:()->Unit, onDespesas:()->Unit){
    @Composable
    fun item(isDespesas:Boolean,value:Int,onClick:()->Unit){
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDespesas)
                            Color.Red.copy(0.5f)
                        else
                            Color.Green.copy(0.5f)
                    ))
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text =
                    if(isDespesas)
                        "Despesas"
                    else
                        "Receitas",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            }
            Text(
                text = value.toMonetaryString(),
                fontWeight = FontWeight.Light,
                fontSize = 14.sp)
        }
    }
    BoxContent(
        enablePadding = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Visao geral", fontSize = 14.sp, modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp))
            Spacer(modifier = Modifier.size(16.dp))
            item(isDespesas = true, value = valorDespesas,onDespesas)
            item(isDespesas = false, value = valorReceitas,onReceitas)
        }
    }
}