package br.com.fcr.gastin.ui.page.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
                .height(56.dp)
                .fillMaxWidth()
                .clickable { it.second();onDismiss() }
                .padding(start = 16.dp, end = 72.dp)
                .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                val text = it.first
                var replace = ""
                if(it.first.contains("{local_offer}")){
                    replace = "{local_offer}"
                    Icon(painter = painterResource(id = R.drawable.ic_local_offer), contentDescription = "Categoria", modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp))
                }
                if(it.first.contains("-")){
                    replace = "-"
                    Icon(painter = painterResource(id = R.drawable.ic_minus), contentDescription = "despesa", modifier = Modifier.size(24.dp))
                }
                if(it.first.contains("+")){
                    replace = "+"
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "receita", modifier = Modifier.size(24.dp))
                }
                if(it.first.contains("{check_list}")){
                    replace = "{check_list}"
                    Icon(painter = painterResource(id = R.drawable.ic_check_all), contentDescription = "receita", modifier = Modifier.size(24.dp).padding(4.dp))
                }
                if(it.first.contains("{list}")){
                    replace = "{list}"
                    Icon(painter = painterResource(id = R.drawable.ic_list), contentDescription = "receita", modifier = Modifier.size(24.dp).padding(4.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = text.replace(replace,""), fontSize = 14.sp)
            }
        }
    }
}