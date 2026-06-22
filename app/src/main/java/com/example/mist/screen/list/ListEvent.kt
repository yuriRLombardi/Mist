package com.example.mist.screen.list

sealed interface ListEvent {
        data class Delete(val id: Long) : ListEvent
        data class AddEdit(val id: Long?) : ListEvent
}