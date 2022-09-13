package com.coppernic.mobility.domain.util

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    operator fun invoke(
        params: P,
        timeoutMs: Long = defaultTimeoutMs,
    ): Flow<InvokeStatus> = flow {
        try {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        }catch(e:HttpException){
            emit(InvokeError(e))
        } catch (t: TimeoutCancellationException) {
            emit(InvokeError(t))
        }
    }.catch { t -> emit(InvokeError(t)) }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

abstract class PagingInteractor<P : PagingInteractor.Parameters<T>, T : Any> : SubjectInteractor<P, PagingData<T>>() {
    interface Parameters<T : Any> {
        val pagingConfig: PagingConfig
    }
}

abstract class SuspendingWorkInteractor<P : Any, T> : SubjectInteractor<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}

abstract class SubjectInteractor<P : Any, T> {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .debounce(100)
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}

//abstract class SubjectInteractorWithOutParams< T> {
//    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
//    // suspending. This means that we can't suspend while flatMapLatest cancels any
//    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
//    // instead, resulting in mostly the same result.
//    private val paramState = MutableSharedFlow<Any>(
//        replay = 1,
//        extraBufferCapacity = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//
//    val flow = paramState
//        .distinctUntilChanged()
//        .flatMapLatest { createObservable() }
//        .distinctUntilChanged()
//
//    operator fun invoke() {
//        paramState.tryEmit(Any())
//    }
//
//    protected abstract fun createObservable(): Flow<T>
//}

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()