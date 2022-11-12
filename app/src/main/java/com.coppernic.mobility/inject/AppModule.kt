package com.coppernic.mobility.inject

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import coil.ImageLoader
import com.coppernic.mobility.data.ApiService
import com.coppernic.mobility.domain.extensions.withLocale
import com.coppernic.mobility.util.AppCoroutineDispatchers
import com.coppernic.mobility.util.interfaces.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
//        auth:UserAuth
    ): OkHttpClient = with(OkHttpClient.Builder()) {
        writeTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(10L, TimeUnit.MINUTES)
//            .addInterceptor(TokenInterceptor(auth))
        build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        appPreferences: AppPreferences
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl("${appPreferences.urlServidor}/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            //    .client(OkHttpClient.Builder()
            //      .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build())
            .build()
            .create()
        //.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    )= ImageLoader(context)

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences("prefs",MODE_PRIVATE)
    }


    @Singleton
    @Provides
    @MediumDate
    fun provideMediumDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDateTime
    fun provideDateTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortDate
    fun provideShortDateFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortTime
    fun provideShortTimeFormatter(
        @ApplicationContext context: Context
    ): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(context)
    }



}
