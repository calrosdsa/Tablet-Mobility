package com.coppernic.mobility.rfid

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.coppernic.sdk.hid.iclassProx.FrameProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HidImpl @Inject constructor(
    @ApplicationContext private val context: Context
):Hid {
    override var listeners:MutableList<RfidListener> = mutableListOf()
    override var hidIclassInteractor: HidIclassInteractor = HidIclassInteractor(context)

//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        @Volatile
//        private var INSTANCE: HidImpl? = null
//        fun getInstance(context: Context)
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: HidImpl().also {
//                    INSTANCE = it
//                }
//            }
//    }

    override fun subscribe(l: RfidListener){
        listeners.add(l)
        Log.d("gta HidImpl - read","on SUBSCRIBE ${listeners.size}")

    }

    override fun unsubscribe(l: RfidListener){
        listeners.remove(l)
                Log.d("gta HidImpl - read","on UNSUBSCRIBE ${listeners.size}")
    }

    @SuppressLint("CheckResult")
    override fun setUp() {
        Log.d("gta HidImpl - read","INIT SET_UP")
        hidIclassInteractor.power(true)
            .flatMapCompletable { hidIclassInteractor.setUp() }
            .andThen(hidIclassInteractor.open())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (l in listeners){
                    l.showWaiting()
                }
            }, {
                for (l in listeners){
                    l.showError(it.message!!)
                }
            })

    }

    override fun read() {
        Log.d("gta HidImpl - read","Ready to Read")
//        val protocolList = Array<FrameProtocol>(1){ FrameProtocol.ISO14443A}
        val protocolList = Array(1){ FrameProtocol.ISO14443B}
        hidIclassInteractor.disposables.add(hidIclassInteractor.continuousRead(protocolList, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.cardSerialNumber != null || it.cardNumber > 0 ) {
                    for (l in listeners){
                        l.cardReaded(it)
//                        Log.d("gta HidImpl - read", "CardNumber ${it.cardNumber}")
//                        Log.d("gta HidImpl - read", "${it.atr}")
//                        Log.d("gta HidImpl - read", "${it.cardSerialNumber}")
//                        Log.d("gta HidImpl - read", "${it.ced}")
//                        Log.d("gta HidImpl - read", "${it.cid}")
//                        Log.d("gta HidImpl - read", "Facility Code ${it.facilityCode}")
//                        Log.d("gta HidImpl - read", "${it.frameProtocol}")
//                        Log.d("gta HidImpl - read", "${it.fwi}")
//                        Log.d("gta HidImpl - read", "Media ${it.media}")
//                        Log.d("gta HidImpl - read", "${it.sak}")
                    }
                }
            }, {
                Log.d("gta HidImpl - read", it.message!!)
                Log.d("gta HidImpl - read", "error")
            }))
    }

    override fun dispose() {
        hidIclassInteractor.disposables.dispose()
        hidIclassInteractor.dispose()
        val d = hidIclassInteractor.power(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                Log.d("gta HidImpl - dispose", it.message!!)
            })
    }

}