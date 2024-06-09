package br.com.fcr.gastin.ui.page.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fcr.gastin.R

@Composable
fun DropUpViewCategoria (
    enable:Boolean,
    onDismiss:()->Unit,
    Nome:String,
    Descricao:String,
    CategoriaCor:Color,
    clearValues:()->Unit
){
    BoxDropUpContent(enable = enable, onDismiss = { clearValues();onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.txt_nome)+":")
            Text(text = Nome, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.txt_descricao)+":")
            Text(text = Descricao, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.txt_cor)+":")
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CategoriaCor))
            }
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
    BackHandler(enabled = enable) {
        clearValues()
        onDismiss()
    }
}