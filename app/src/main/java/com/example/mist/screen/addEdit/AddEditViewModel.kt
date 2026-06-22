package com.example.mist.screen.addEdit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mist.data.local.CorRepository
import com.example.mist.data.local.MarcaRepository
import com.example.mist.data.local.PlataformRepository
import com.example.mist.domain.Cor
import com.example.mist.domain.Marca
import com.example.mist.screen.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val plataformRepository: PlataformRepository,
    private val marcaRepository: MarcaRepository,
    private val corRepository: CorRepository
) : ViewModel() {


    private val _cores = MutableStateFlow<List<Cor>>(emptyList())
    val cores: StateFlow<List<Cor>> = _cores

    private val _marcas = MutableStateFlow<List<Marca>>(emptyList())
    val marcas: StateFlow<List<Marca>> = _marcas

    var nomeConsole by mutableStateOf("")
        private set

    var nomeCor by mutableStateOf("")
        private set

    var nomeMarca by mutableStateOf("")
        private set

    var preco by mutableStateOf("")
        private set

    var corId by mutableLongStateOf(0L)
        private set

    var marcaId by mutableLongStateOf(0L)
        private set

    var ano by mutableStateOf("")
        private set

//    val cores = corRepository.getAll().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )
//
//    val marcas = marcaRepository.getAll().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            corRepository.getAll().collect { corList ->
                _cores.value = corList
                if (corId == 0L && corList.isNotEmpty()) {
                    corId = corList.first().id
                }

            }
        }
            viewModelScope.launch {
                marcaRepository.getAll().collect{ marcaList->
                    _marcas.value = marcaList
                    if (marcaId == 0L && marcaList.isNotEmpty()){
                        marcaId = marcaList.first().id
                    }

                }
        }
        id?.let {
            viewModelScope.launch {
                val console = plataformRepository.getById(id)
                nomeConsole = console?.nome ?: ""
                preco = console?.preco.toString() ?: ""
                corId = console?.cor?.id ?: -1
                marcaId = console?.marca?.id ?: -1
                ano = console?.ano.toString()
                val cor = corRepository.getById(corId)
                nomeCor = cor?.cor ?: ""

                val marca = marcaRepository.getById(marcaId)
                nomeMarca = marca?.marca ?: ""
            }

        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.NameChanged -> {
                nomeConsole = event.name
            }

            is AddEditEvent.PrecoChanged -> {
                preco = event.preco
            }

            AddEditEvent.Save -> {
                savePlataforma()
            }

            is AddEditEvent.YearChanged -> {
                ano = event.ano
            }

            is AddEditEvent.CorChanged -> {
                corId = event.corId
            }

            is AddEditEvent.MarcaChanged -> {
                marcaId = event.marca
            }
        }
    }

    private fun savePlataforma() {
        viewModelScope.launch {
            if (nomeConsole.isBlank()) {
                _uiEvent.send(UIEvent.ShowSnackBar("Nome não pode ser vazio"))
                return@launch
            }
            if (preco.isBlank() || (preco.toDoubleOrNull() == null)){
                _uiEvent.send(UIEvent.ShowSnackBar("Preço inválido"))
                return@launch
            }
            if(ano.isBlank() || !ano.isDigitsOnly()){
                _uiEvent.send(UIEvent.ShowSnackBar("Ano inválido"))
                return@launch
            }
            if (ano.toLong() > 2155 || ano.toLong() < 1901){
                _uiEvent.send(UIEvent.ShowSnackBar("Digite o ano entre 1901 e 2155"))
                return@launch
            }
            plataformRepository.insert(
                id = id,
                nome = nomeConsole,
                preco = preco.toFloat(),
                corId = corId,
                marcaId = marcaId,
                ano = ano.toLong(),
            )

            _uiEvent.send(UIEvent.NavigateBack)
        }
    }
}