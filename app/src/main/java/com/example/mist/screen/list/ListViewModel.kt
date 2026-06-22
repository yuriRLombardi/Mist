package com.example.mist.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mist.data.local.PlataformRepository
import com.example.mist.navigation.AddEditScreenRoute
import com.example.mist.screen.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val plataformRepository: PlataformRepository
): ViewModel() {
    var isRefreshing by mutableStateOf(false)
        private set

    val consoles = plataformRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            plataformRepository.getAll().first()
            isRefreshing = false
        }
    }

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.AddEdit -> {
                viewModelScope.launch {
                    _uiEvent.send(UIEvent.Navigate(AddEditScreenRoute(event.id)))
                }
            }
            is ListEvent.Delete -> {
                delete(
                    id = event.id
                )
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            plataformRepository.delete(id)
        }
    }
}