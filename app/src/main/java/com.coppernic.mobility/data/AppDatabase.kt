package com.coppernic.mobility.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coppernic.mobility.data.models.entities.*
import com.coppernic.mobility.util.interfaces.TecluDatabaseDao

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [
    Config::class,
    Cardholder::class,
    Credential::class,
    Marcacion::class,
    Ciudad::class,
    ImageUser::class,
    AccessEntity::class
                     ],
    version = 6
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), TecluDatabaseDao {

//    abstract fun configDao(): ConfigDao
//    abstract fun cardholderDao(): CardholderDao
//    abstract fun credentialDao(): CredentialDao
//    abstract fun marcacionDao(): MarcacionDao
//    abstract fun ciudadDao(): CiudadDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        var INSTANCE: AppDatabase? = null

        const val DATABASE_NAME = "word_database"


        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_database"
                ).fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}