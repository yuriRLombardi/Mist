package com.example.mist.screen.addEdit

sealed interface AddEditEvent {
    data class NameChanged(val name: String) : AddEditEvent
    data class YearChanged(val ano: String) : AddEditEvent
    data class PrecoChanged(val preco: String) : AddEditEvent
    data class CorChanged(val corId: Long) : AddEditEvent
    data class MarcaChanged(val marca: Long) : AddEditEvent
    data object Save : AddEditEvent

}