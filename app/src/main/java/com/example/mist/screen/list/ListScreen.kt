package com.example.mist.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mist.data.local.CorRepositoryImpl
import com.example.mist.data.local.MarcaRepositoryImpl
import com.example.mist.data.local.PlataformDatabaseProvider
import com.example.mist.data.local.PlataformaRepositoryImpl
import com.example.mist.domain.Console
import com.example.mist.navigation.AddEditScreenRoute
import com.example.mist.screen.UIEvent

@Composable
fun ListScreen(
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {

    val context = LocalContext.current.applicationContext
    val database = PlataformDatabaseProvider.provide(
        context = context
    )

    val repositoryCor = CorRepositoryImpl(
        corDAO = database.CorDAO()
    )
    val repositoryMarca = MarcaRepositoryImpl(
        marcaDAO = database.MarcaDAO()
    )
    val repository = PlataformaRepositoryImpl(
        dao = database.PlataformDAO(),
        corDAO = database.CorDAO(),
        marcaDAO = database.MarcaDAO(),
        corRepository = repositoryCor,
        marcaRepository = repositoryMarca,
    )

    val viewModel = viewModel<ListViewModel>() {
        ListViewModel(
            plataformRepository =  repository
        )
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UIEvent.Navigate<*> -> {
                    when (uiEvent.route) {
                        is AddEditScreenRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                    }
                }

                is UIEvent.ShowSnackBar -> {

                }

                UIEvent.NavigateBack -> {

                }
            }
        }

    }
    val consoles by viewModel.consoles.collectAsState()
    val isRefreshing = viewModel.isRefreshing
    ListScreenContent(
        consoles = consoles,
        onEvent = viewModel::onEvent,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenContent(
    consoles: List<Console>,
    onEvent: (ListEvent) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val state = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            Text(
                "Lista de Consoles",
                style = TextStyle(
                    fontSize = 32.sp,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ListEvent.AddEdit(null))
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = state,
            indicator = {
                Indicator(
                    state = state,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(paddingValues)
                    .fillMaxSize(),
                contentPadding = paddingValues
            ) {
                itemsIndexed(consoles){ index, console ->
                    ConsoleCard(
                        console = console,
                        onEvent = onEvent
                    )

                    if (index < consoles.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConsoleCard(
    console: Console,
    onEvent: (ListEvent) -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                  Text(
                      text = console.nome,
                      style = MaterialTheme.typography.titleLarge
                  )
                Text(
                    text = "Preço: "+console.preco
                )
                Text(
                    text = "Ano: "+console.ano
                )
                Text(
                    text = "Marca: "+console.marca.marca
                )
                Text(
                    text = "Cor: "+console.cor.cor
                )
            }
            Button(
                onClick = {
                    onEvent(
                        ListEvent.AddEdit(
                            id = console.id
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    onEvent(
                        ListEvent.Delete(
                            id = console.id
                        )
                    )
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover",
                )
            }
        }
    }
}
