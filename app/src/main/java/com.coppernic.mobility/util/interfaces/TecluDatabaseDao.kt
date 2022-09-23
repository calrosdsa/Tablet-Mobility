package com.coppernic.mobility.util.interfaces

import com.coppernic.mobility.data.models.dao.*

interface TecluDatabaseDao {
     fun configDao(): ConfigDao
     fun cardholderDao(): CardholderDao
     fun credentialDao(): CredentialDao
     fun marcacionDao(): MarcacionDao
     fun ciudadDao(): CiudadDao
     fun imageDao():ImageDao

     fun accessDao():AccessDao
}