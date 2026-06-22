package com.example.mist.screen.addEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mist.data.local.CorRepositoryImpl
import com.example.mist.data.local.MarcaRepositoryImpl
import com.example.mist.data.local.PlataformDatabaseProvider
import com.example.mist.data.local.PlataformaRepositoryImpl
import com.example.mist.domain.Cor
import com.example.mist.domain.Marca
import com.example.mist.screen.UIEvent


@Composable
fun AddEditScreen(
    id: Long?,
    navigateBack: () -> Unit
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

    val repositoryConsole = PlataformaRepositoryImpl(
        dao = database.PlataformDAO(),
        corDAO = database.CorDAO(),
        marcaDAO = database.MarcaDAO(),
        corRepository = repositoryCor,
        marcaRepository = repositoryMarca
    )


    val viewModel = viewModel<AddEditViewModel>() {
        AddEditViewModel(
            id = id,
            plataformRepository = repositoryConsole,
            marcaRepository = repositoryMarca,
            corRepository = repositoryCor
        )
    }


    var nome = viewModel.nomeConsole
    var preco = viewModel.preco
    var corSelectedId = viewModel.corId
    var marcaSelectedId = viewModel.marcaId
    var ano = viewModel.ano

    val cores by viewModel.cores.collectAsState()
    val marcas by viewModel.marcas.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UIEvent.Navigate<*> -> {

                }

                UIEvent.NavigateBack -> {
                    navigateBack()
                }

                is UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message
                    )
                }
            }

        }
    }


    AddEditContent(
        id = id,
        nome = nome,
        preco = preco,
        corSelectedId = corSelectedId,
        marcaSelectedId = marcaSelectedId,
        marca = marcas,
        ano = ano,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        cores = cores
    )
}

@Composable
fun AddEditContent(
    id: Long?,
    nome: String,
    preco: String,
    corSelectedId: Long,
    marcaSelectedId: Long,
    marca: List<Marca>,
    ano: String,
    onEvent: (AddEditEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    cores: List<Cor>
) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        Text(
            if (id != null) "Editar Console" else "Adicionar Console",
            style = TextStyle(
                fontSize = 32.sp
            )
        )
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditEvent.Save)
                }
            ) { Icon(Icons.Default.Check, contentDescription = "Salvar") }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = nome,
                onValueChange = {
                    onEvent(
                        AddEditEvent.NameChanged(it)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Nome") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = preco,
                onValueChange = {
                    onEvent(
                        AddEditEvent.PrecoChanged(it)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Preço: Use '.' como separador") }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = ano,
                onValueChange = {
                    onEvent(
                        AddEditEvent.YearChanged(it)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Ano") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Cores", style = TextStyle(fontSize = 24.sp))
            cores.forEach { cor ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = corSelectedId == cor.id,
                        onClick = {
                            onEvent(
                                AddEditEvent.CorChanged(cor.id)
                            )
                        },
                    )
                    Text(cor.cor)

                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Text("Marcas", style = TextStyle(fontSize = 24.sp))
            marca.forEach { marca ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = marcaSelectedId == marca.id,
                        onClick = {
                            onEvent(
                                AddEditEvent.MarcaChanged(marca.id)
                            )
                        },
                    )
                    Text(marca.marca)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}