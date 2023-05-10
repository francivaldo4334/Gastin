package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import br.com.fcr.gastin.R

@Composable
fun DropDownMoreOptions(customItem:(@Composable ColumnScope.()->Unit)? = null, listOptions:List<Pair<String,()->Unit>>, enable:Boolean, onDismiss:()->Unit){
    DropdownMenu(expanded = enable, onDismissRequest = onDismiss) {
        if(customItem != null)
            customItem()
        listOptions.forEach {
            Row(modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .clickable { it.second();onDismiss() }
                .padding(start = 16.dp, end = 72.dp),
                verticalAlignment = Alignment.CenterVertically) {
                var text = it.first
                if(it.first.first() == '+') {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "",
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Green),
                        tint = MaterialTheme.colors.background
                    )
                    text = text.removePrefix("+")
                }
                else if(it.first.first() == '-') {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus),
                        contentDescription = "",
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Red),
                        tint = MaterialTheme.colors.background
                    )
                    text = text.removePrefix("-")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = text, fontSize = 14.sp)
            }
        }
    }
}