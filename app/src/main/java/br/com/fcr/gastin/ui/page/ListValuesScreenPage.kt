package br.com.fcr.gastin.ui.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fcr.gastin.R
import br.com.fcr.gastin.ui.utils.Tetra
import br.com.fcr.gastin.ui.utils.toMonetaryString

@Composable
fun ListValuesScreenPage(title:String,onMoreOptions:()->Unit,listItem:List<Tetra<String,String,Int,Int>>){
    var showAllCheckBox by remember {mutableStateOf(false)}
    var listIdCheckeds by remember { mutableStateOf(mutableListOf<Int>()) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, fontSize = 24.sp)
            IconButton(onClick = onMoreOptions) {
                Icon(painter = painterResource(id = R.drawable.ic_more_options_chorts), contentDescription = "")
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
        ){
            item { 
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(listItem){(descricao,data,value,id)->
                var check by remember{ mutableStateOf(false) }
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                check = true
                                listIdCheckeds+=id
                                showAllCheckBox = listIdCheckeds.isNotEmpty()
                            }
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dolar),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = descricao,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = data,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onBackground.copy(0.5f)
                                )
                            }
                        }
                        Row {
                            if (showAllCheckBox) {
                                Checkbox(
                                    checked = check,
                                    onCheckedChange = {
                                        check = it
                                        if(check)
                                            listIdCheckeds += id
                                        else
                                            listIdCheckeds -=id
                                        Log.d("CHECK",listIdCheckeds.toString())

                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = value.toMonetaryString(), fontSize = 12.sp)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun ListValuesScreenPagePreview(){
    ListValuesScreenPage(
        title = "Despesas",
        onMoreOptions = { /*TODO*/ },
        listItem = listOf(
            Tetra("descricao","01/02/2023",1000,0),
            Tetra("descricao","01/02/2023",1000,1),
            Tetra("descricao","01/02/2023",1000,2),
            Tetra("descricao","01/02/2023",1000,3),
            Tetra("descricao","01/02/2023",1000,4)
        )
    )
}
