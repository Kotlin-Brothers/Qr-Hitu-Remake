@file:Suppress("UNUSED_PARAMETER")

package com.example.qr_hitu.presentation.adminScreens.tabLists.missingQrList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qr_hitu.R
import com.example.qr_hitu.functions.MissingQrDocs
import com.example.qr_hitu.functions.delMissing
import com.example.qr_hitu.functions.getMissingQR
import com.example.qr_hitu.presentation.adminScreens.tabLists.EmptyListScreen
import com.example.qr_hitu.presentation.ui.DelDialog


//  Tela com a lista de alertas de falta de QR
@Composable
fun MissingQrList(navController: NavController) {

    //  Estado para guardar a lista de falta de QR
    val (list, setList) = remember { mutableStateOf<List<MissingQrDocs>>(emptyList()) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(true) }
    //  Mostrar Dialog
    val show = remember { mutableStateOf(false) }

    //  Abre Coroutine
    LaunchedEffect(Unit) {
        getMissingQR { result -> //  Chama a função que consegue a lista e guarda no estado
            setList(result)
            setIsLoading(false)
        }
    }

    //  Caso não exista itens a apresentar
    if (list.isEmpty() && !isLoading) {
        EmptyListScreen(text = stringResource(R.string.missingListText))
    } else {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp
                )
            }

        } else {
            //  Cria um lista vertical
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 2.dp),
                columns = GridCells.Fixed(1)
            ) {
                //  Para cada alerta na lista cria um Card com as informações necessárias
                items(list) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { show.value = true },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
                        colors = CardDefaults.cardColors(Color(0xFFd9d9d9))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                        ) {

                            //  Condição para verificar qual icon colocar
                            when (item.machine) {
                                "Projetor" -> {
                                    Icon(Icons.Filled.VideocamOff, "Projector")
                                }

                                "Impressora" -> {
                                    Icon(Icons.Filled.Print, "Impressora")
                                }

                                else -> {
                                    Icon(Icons.Filled.Computer, "Computer")
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .width(280.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = item.machine,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = item.room,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        //  Mostra Dialog
                        if (show.value) {
                            DelDialog(
                                onDialogDismiss = { show.value = false },
                                //  Apaga alerta e atualiza a lista
                                onDeleteClick = {
                                    delMissing(item.room, item.machine); getMissingQR(
                                    setList
                                )
                                },
                                title = stringResource(R.string.delMDtitle),
                                text = stringResource(R.string.delMDtext)
                            )
                        }
                    }
                }
            }
        }

    }
}