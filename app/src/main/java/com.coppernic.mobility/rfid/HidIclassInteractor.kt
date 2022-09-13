package com.coppernic.mobility.rfid


import android.content.Context
import android.util.Log
import fr.coppernic.sdk.core.Defines
import fr.coppernic.sdk.hid.iclassProx.*
import fr.coppernic.sdk.power.impl.cone.ConePeripheral
import fr.coppernic.sdk.utils.core.CpcResult
import fr.coppernic.sdk.utils.io.InstanceListener
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class HidIclassInteractor(val context: Context) {


    lateinit var reader: Reader

    var disposables = CompositeDisposable()
        get() {
            if (field.isDisposed) {
                // New composite disposable each time we subscribe
                field = CompositeDisposable()
            }
            return field
        }

    fun power(on: Boolean): Single<CpcResult.RESULT> {
        ConePeripheral.RFID_HID_ICLASSPROX_GPIO
        return ConePeripheral.RFID_HID_ICLASSPROX_GPIO.descriptor.power(context, on)
    }

    lateinit var completableEmitter: CompletableEmitter

    fun setUp(): Completable {
        return Completable.create {
            completableEmitter = it
            try {
                Reader.getInstance(context, instanceListener)
            } catch (e: Exception){
                it.onError( Throwable("fallo setUp"))
            }
        }
    }

    fun open(): Completable {
        return Completable.create {
            try {
                var res = reader.open(Defines.SerialDefines.HID_ICLASS_PROX_READER_PORT, BaudRate.B9600)
                if(res != ErrorCodes.ER_OK){
                    Log.d("gta HidImpl - read","FAIL TO OPEN 1  ${res.description}")
                    it.onError( Throwable(res.description))
                }else {
                    res = reader.samCommandSuspendAutonomousMode()
                    if (res != ErrorCodes.ER_OK) {
                        it.onError(Throwable(res.description))
                        Log.d("gta HidImpl - read","FAIL TO OPEN 2  ${res.description}")
                        //it.onError(Throwable("fallo samCommand"))
                    }else {
                        it.onComplete()
                        Log.d("gta HidImpl - read","ON OPEN")

                    }
                }
            } catch (e: Exception){
                it.onError( Throwable("fallo open"))
                Log.d("gta HidImpl - read","FAIL EXCEPTION ON OPEM")

            }
        }
    }

    fun read(frameProtocolList: Array<FrameProtocol>): Single<Card>{
        return Single.create{
            val card = Card()
            val res = reader.samCommandScanFieldForCard(frameProtocolList, card)
            if(res != ErrorCodes.ER_OK){
                notifyError(it, Throwable(res.description))
            }else{
                notifySuccess(it, card)
            }
        }
    }

    fun readLF(): Single<Card>{
        return Single.create{
            val card = Card()
//            Log.d("CARD_READ",card.//)
            val res = reader.samCommandScanAndProcessMedia(card)
            if(res != ErrorCodes.ER_OK){
                notifyError(it, Throwable(res.description))
            }else{
                notifySuccess(it, card)
            }

        }
    }

    fun continuousRead(frameProtocolList: Array<FrameProtocol>, isHf: Boolean): Flowable<Card>{
        return Flowable.interval(100, TimeUnit.MILLISECONDS, Schedulers.single())
            .onBackpressureLatest()
            .flatMapSingle ({
                if(!isHf){
                    return@flatMapSingle readLF().onErrorResumeNext(Single.just(Card()))
                }else {
                    return@flatMapSingle read(frameProtocolList).onErrorResumeNext(Single.just(Card()))
                }
            },false,1)
    }

    fun dispose(){
        disposables.dispose()
        if(this::reader.isInitialized) reader.close()
    }

    private val instanceListener= object: InstanceListener<Reader>{
        override fun onDisposed(p0: Reader) {
        }

        override fun onCreated(hidReader: Reader) {
            reader = hidReader
            if(!completableEmitter.isDisposed) {
                completableEmitter.onComplete()
            }
        }
    }

    fun notifySuccess(emitter: SingleEmitter<Card>, card: Card) {
        if (!emitter.isDisposed) {
            emitter.onSuccess(card)
        }
    }

    fun notifyError(emitter: SingleEmitter<Card>, throwable: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(throwable)
        }
    }

}