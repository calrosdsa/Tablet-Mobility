package com.coppernic.mobility.rfid

//class HidImpl (val context: Context) {
//
//    var listeners:MutableList<RfidListener> = mutableListOf()
//
//    var hidIclassInteractor: HidIclassInteractor = HidIclassInteractor(context)
//
//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        @Volatile
//        private var INSTANCE: HidImpl? = null
//        fun getInstance(context: Context) =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: HidImpl(context).also {
//                    INSTANCE = it
//                }
//            }
//    }
//
//    fun subscribe(l: RfidListener){
//        listeners.add(l)
//            Log.d("gta HidImpl - read","on SUBSCRIBE ${listeners.size}")
//
//    }
//
//    fun unsubscribe(l: RfidListener){
//        listeners.remove(l)
    //        Log.d("gta HidImpl - read","on UNSUBSCRIBE ${listeners.size}")
//
//    }
//
//    @SuppressLint("CheckResult")
//    fun setUp() {
//         hidIclassInteractor.power(true)
//            .flatMapCompletable { hidIclassInteractor.setUp() }
//            .andThen(hidIclassInteractor.open())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Log.d("gta HidImpl - read","INIT")
//                for (l in listeners){
//                    l.showWaiting()
//                }
//            }, {
//                for (l in listeners){
//                    l.showError(it.message!!)
//                }
//            })
//
//    }
//
//    fun read() {
//        Log.d("gta HidImpl - read","READY TO READ")
//        val protocolList = Array<FrameProtocol>(1){FrameProtocol.ISO14443A}
//        hidIclassInteractor.disposables.add(hidIclassInteractor.continuousRead(protocolList, false)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.cardSerialNumber != null || it.cardNumber > 0 ) {
//                    for (l in listeners){
//                        l.cardReaded(it)
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
//
//
//
//                    }
//                }
//            }, {
//                Log.d("gta HidImpl - read", it.message!!)
//                Log.d("gta HidImpl - read", "error")
//            }))
//    }
//
//    @SuppressLint("CheckResult")
//    fun dispose() {
//        hidIclassInteractor.disposables.dispose()
//        hidIclassInteractor.dispose()
//        hidIclassInteractor.power(false)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//            }, {
//                Log.d("gta HidImpl - dispose", it.message!!)
//            })
//    }
//
//}