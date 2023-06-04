package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fcr.gastin.R

@Composable
fun DialogExcluir (enable:Boolean,onDismiss:()->Unit,onConfirm:()->Unit){
    if(enable) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(horizontal = 56.dp)
                        .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                        .background(MaterialTheme.colors.error)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.txt_atencao),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.txt_confirmar_exclucao),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 8.dp),
                        onClick = onDismiss
                    ) {
                        Text(text = stringResource(R.string.txt_cancelar))
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        onClick = { onConfirm();onDismiss }
                    ) {
                        Text(text = stringResource(id = R.string.txt_excluir).replace("-", ""))
                    }
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun DialogExcluirPreview (){
    DialogExcluir(true,{}){}
}
