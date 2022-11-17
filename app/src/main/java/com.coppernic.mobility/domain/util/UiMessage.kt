package com.coppernic.mobility.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*


data class UiMessage(
    val message: String,
    val id: Long = UUID.randomUUID().mostSignificantBits,
)

fun UiMessage(
    t: Throwable,
    id: Long = UUID.randomUUID().mostSignificantBits,
): UiMessage = UiMessage(
    message = t.message ?: "Error occurred: $t",
    id = id,
)

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }
}
class UiMessageManager2 {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow<UiMessage?>(null)

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value =  message
        }
    }

    suspend fun clearMessage() {
        mutex.withLock {
            _messages.value = null
        }
    }
}