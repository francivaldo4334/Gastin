package br.com.fcr.gastin.ui.page.components

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.HomeActivity
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.MaskTransformation
import br.com.fcr.gastin.ui.utils.Tetra

@Composable
fun DropUpViewRegister(idRegister:Int,enable:Boolean,onDismiss:()->Unit){
    BackHandler(enabled = enable) {
        onDismiss()
    }
    val txtCarregando = stringResource(R.string.txt_carregando)
    val Valor by remember{ mutableStateOf(txtCarregando) }
    val Descricao by remember{ mutableStateOf(txtCarregando) }
    val CategoriaCor by remember { mutableStateOf(Color(0xFFFF00ff)) }
    val CategoriaNome by remember {mutableStateOf(txtCarregando)}
    BoxDropUpContent(enable = enable, onDismiss = onDismiss) {
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