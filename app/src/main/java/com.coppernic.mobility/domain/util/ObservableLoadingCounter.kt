package com.coppernic.mobility.domain.util

import com.coppernic.mobility.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
//    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess ->{ counter.removeLoader() }
        is InvokeError -> {
     //       logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
    }
}

suspend fun <T>Flow<Resource<T>>.collectData(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
    data: MutableStateFlow<T?>,
) = collect{ results->
    when(results){
        is Resource.Loading ->{
            counter.addLoader()
        }
        is Resource.Success ->{
            results.data?.let { data.emit(it) }
            counter.removeLoader()
        }
        is Resource.Error ->{
            counter.removeLoader()
            results.message?.let{
                uiMessageManager?.emitMessage(UiMessage(it))
            }
        }
    }
}
