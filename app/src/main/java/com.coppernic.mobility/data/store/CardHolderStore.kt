    package com.coppernic.mobility.data.store

import android.annotation.SuppressLint
import android.content.Context
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.data.models.entities.Cardholder
import com.coppernic.mobility.data.models.dao.CardholderDao
import com.coppernic.mobility.data.models.dao.ImageDao
import com.coppernic.mobility.data.models.entities.ImageUser
import com.coppernic.mobility.util.interfaces.AppUtil
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.coppernic.mobility.data.result.mapper.toCardHolderEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

typealias CardHolderStore = Store<Unit, List<Cardholder>>

@InstallIn(SingletonComponent::class)
@Module
object CardHolderModule{

    @SuppressLint("SuspiciousIndentation")
    @Provides
    @Singleton
    fun provideCardHolderStore (
        apiService: ApiService,
        cardholderDao: CardholderDao,
        appUtil: AppUtil,
        imageDao: ImageDao,
        @ApplicationContext context:Context
    ):CardHolderStore = StoreBuilder.from(
        fetcher = Fetcher.of {
            apiService.getCardHolders().data
        },
        sourceOfTruth = SourceOfTruth.of(
          reader = {
              cardholderDao.getCardholder().map {entries ->
                  when {
                      entries.isEmpty() -> null
                      // If the request is expired, our data is stale
                      //         lastRequestStore.isRequestExpired(Duration.ofHours(3)) -> null
                      // Otherwise, our data is fresh and valid
                      else -> entries
                  }
              }
                   },
          writer ={_:Unit,entries->
             val entriesR = entries.filter { it.unidadOrganizativa != "Visita" }.map {
                 try {
                 imageDao.insert(ImageUser(
                     userGui = it.guid,
                     nombre = "${it.firstName}  ${it.lastName}",
                     picture =  appUtil.getImageBitmap(context,"http://172.20.10.55:91/imagenes/${it.picture}"),
                 ))
                 }catch (e:Throwable){
                     imageDao.insert(ImageUser(
                         userGui = it.guid,
                         nombre = "${it.firstName}  ${it.lastName}",
                         picture =  appUtil.getImageBitmap(context,"https://thumbs.dreamstime.com/b/vector-de-perfil-avatar-predeterminado-foto-usuario-medios-sociales-icono-183042379.jpg"),
                     ))
//                     Log.d("CARD_HOLDER_RESULTS",e.localizedMessage?:"Desconocido")
                 }
                it.toCardHolderEntity()
              }
              cardholderDao.deleteAll()
              cardholderDao.insertAll(entriesR)
//              entries.map {
//                  cardholderDao.insert(
//                      Cardholder(
//            guid = "guid",
//            firstName = "amsamksmas",
//            lastName = "asmkmaksmkas",
//            ci = "121201",
//            descriptions = "21k2 121",
//            empresa = "smamkams",
//            estado = "smamsas",
//        )
//                  )
//               }
//                      single_entry
//                  picture = appUtil.getImageBitmap(context,it.picture),
//                      fecha = currentDate

//              cardholderDao.insertAll(cardResults)
          },
          delete = {
                   cardholderDao.deleteAll()
          },
          deleteAll = cardholderDao::deleteAll
        )
    ).build()
}
