package com.coppernic.mobility.inject

import com.coppernic.mobility.util.impl.AppPreferencesImpl
import com.coppernic.mobility.util.interfaces.AppUtil
import com.coppernic.mobility.util.impl.AppUtilImpl
import com.coppernic.mobility.util.interfaces.AppPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindAppUtil(utilImpl: AppUtilImpl): AppUtil

    @Binds
    @Singleton
    abstract fun bindAppPreferences(appPreferencesImpl: AppPreferencesImpl):AppPreferences
}