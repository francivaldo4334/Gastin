package br.com.fcr.gastin.ui.page.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.HomeViewModel
import br.com.fcr.gastin.MyApplication
import br.com.fcr.gastin.R
import br.com.fcr.gastin.RegisterEvent
import br.com.fcr.gastin.ui.page.viewmodels.EmptyRegistroViewModel
import br.com.fcr.gastin.ui.page.viewmodels.RegistroViewModel

@Composable
fun DropUpViewRegister(
    enable:Boolean,
    onDismiss:()->Unit,
    Valor:String,
    Descricao:String,
    CategoriaCor:Color,
    CategoriaNome:String
){
    BackHandler(enabled = enable) {
        onDismiss()
    }

    BoxDropUpContent(enable = enable, onDismiss = { onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.txt_valor)+":")
            Text(text = Valor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.txt_descricao)+":")
            Text(text = Descricao, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.txt_categoria)+":")
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(CategoriaCor))
            }
            Text(text = CategoriaNome, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}