package com.coppernic.mobility.inject

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.coppernic.mobility.data.AppDatabase
import com.coppernic.mobility.util.DatabaseTransactionRunner
import com.coppernic.mobility.util.RoomTransactionRunner
import com.coppernic.mobility.util.interfaces.TecluDatabaseDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RoomDatabaseModule{

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        val builder =  Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()

    }

}


@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {
    @Provides
    fun provideConfigDao(db: AppDatabase) = db.configDao()
    @Provides
    fun provideCardHolderDao(db: AppDatabase) = db.cardholderDao()
    @Provides
    fun provideCredentialDap(db: AppDatabase) = db.credentialDao()
    @Provides
    fun provideMarcacionesDao(db: AppDatabase) = db.marcacionDao()
    @Provides
    fun provideCiudadDao(db: AppDatabase) = db.ciudadDao()

    @Provides
    fun provideImageDao(db:AppDatabase) = db.imageDao()
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModuleBinds {
    @Binds
    abstract fun bindNovelDatabase(db: AppDatabase): TecluDatabaseDao


    @Singleton
    @Binds
    abstract fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner

//    @Singleton
//    @Binds
//    abstract fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner
}